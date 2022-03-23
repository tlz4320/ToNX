package cn.treeh.ToNX.util;

import sun.nio.cs.ThreadLocalCoders;
import sun.nio.cs.UTF_8;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class ParseUtil {
    private static final long L_ENCODED = 0xF800802DFFFFFFFFL;

    // highMask((char)0x7F, (char)0x7F) | highMask("=;?/# <>%\"{}|\\^[]`");
    private static final long H_ENCODED = 0xB800000178000000L;

    private static byte unescape(String s, int i) {
        return (byte) Integer.parseInt(s, i + 1, i + 3, 16);
    }
    public static String decode(String s) {
        int n = s.length();
        if ((n == 0) || (s.indexOf('%') < 0))
            return s;

        StringBuilder sb = new StringBuilder(n);
        ByteBuffer bb = ByteBuffer.allocate(n);
        CharBuffer cb = CharBuffer.allocate(n);
        CharsetDecoder dec = ThreadLocalCoders.decoderFor(UTF_8.INSTANCE)
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);

        char c = s.charAt(0);
        for (int i = 0; i < n;) {
            assert c == s.charAt(i);
            if (c != '%') {
                sb.append(c);
                if (++i >= n)
                    break;
                c = s.charAt(i);
                continue;
            }
            bb.clear();
            int ui = i;
            for (;;) {
                assert (n - i >= 2);
                try {
                    bb.put(unescape(s, i));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException();
                }
                i += 3;
                if (i >= n)
                    break;
                c = s.charAt(i);
                if (c != '%')
                    break;
            }
            bb.flip();
            cb.clear();
            dec.reset();
            CoderResult cr = dec.decode(bb, cb, true);
            if (cr.isError())
                throw new IllegalArgumentException("Error decoding percent encoded characters");
            cr = dec.flush(cb);
            if (cr.isError())
                throw new IllegalArgumentException("Error decoding percent encoded characters");
            sb.append(cb.flip().toString());
        }

        return sb.toString();
    }
    public static URL fileToEncodedURL(File file)
            throws MalformedURLException
    {
        String path = file.getAbsolutePath();
        path = sun.net.www.ParseUtil.encodePath(path);
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith("/") && file.isDirectory()) {
            path = path + "/";
        }
        return new URL("file", "", path);
    }
    private static boolean match(char c, long lowMask, long highMask) {
        if (c < 64)
            return ((1L << c) & lowMask) != 0;
        if (c < 128)
            return ((1L << (c - 64)) & highMask) != 0;
        return false;
    }
    private static int escape(char[] cc, char c, int index) {
        cc[index++] = '%';
        cc[index++] = Character.forDigit((c >> 4) & 0xF, 16);
        cc[index++] = Character.forDigit(c & 0xF, 16);
        return index;
    }
    private static String encodePath(String path, int index, char sep) {
        char[] pathCC = path.toCharArray();
        char[] retCC = new char[pathCC.length * 2 + 16 - index];
        if (index > 0) {
            System.arraycopy(pathCC, 0, retCC, 0, index);
        }
        int retLen = index;

        for (int i = index; i < pathCC.length; i++) {
            char c = pathCC[i];
            if (c == sep)
                retCC[retLen++] = '/';
            else {
                if (c <= 0x007F) {
                    if (c >= 'a' && c <= 'z' ||
                            c >= 'A' && c <= 'Z' ||
                            c >= '0' && c <= '9') {
                        retCC[retLen++] = c;
                    } else if (match(c, L_ENCODED, H_ENCODED)) {
                        retLen = escape(retCC, c, retLen);
                    } else {
                        retCC[retLen++] = c;
                    }
                } else if (c > 0x07FF) {
                    retLen = escape(retCC, (char)(0xE0 | ((c >> 12) & 0x0F)), retLen);
                    retLen = escape(retCC, (char)(0x80 | ((c >>  6) & 0x3F)), retLen);
                    retLen = escape(retCC, (char)(0x80 | ((c >>  0) & 0x3F)), retLen);
                } else {
                    retLen = escape(retCC, (char)(0xC0 | ((c >>  6) & 0x1F)), retLen);
                    retLen = escape(retCC, (char)(0x80 | ((c >>  0) & 0x3F)), retLen);
                }
            }
            //worst case scenario for character [0x7ff-] every single
            //character will be encoded into 9 characters.
            if (retLen + 9 > retCC.length) {
                int newLen = retCC.length * 2 + 16;
                if (newLen < 0) {
                    newLen = Integer.MAX_VALUE;
                }
                char[] buf = new char[newLen];
                System.arraycopy(retCC, 0, buf, 0, retLen);
                retCC = buf;
            }
        }
        return new String(retCC, 0, retLen);
    }
    private static int firstEncodeIndex(String path) {
        int len = path.length();
        for (int i = 0; i < len; i++) {
            char c = path.charAt(i);
            // Ordering in the following test is performance sensitive,
            // and typically paths have most chars in the a-z range, then
            // in the symbol range '&'-':' (includes '.', '/' and '0'-'9')
            // and more rarely in the A-Z range.
            if (c >= 'a' && c <= 'z' ||
                    c >= '&' && c <= ':' ||
                    c >= 'A' && c <= 'Z') {
                continue;
            } else if (c > 0x007F || match(c, L_ENCODED, H_ENCODED)) {
                return i;
            }
        }
        return -1;
    }
    public static String encodePath(String path, boolean flag) {
        if (flag && File.separatorChar != '/') {
            return encodePath(path, 0, File.separatorChar);
        } else {
            int index = firstEncodeIndex(path);
            if (index > -1) {
                return encodePath(path, index, '/');
            } else {
                return path;
            }
        }
    }
}
