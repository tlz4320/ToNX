package cn.treeh.ToNX.Output;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class BinaryOutputFile implements OutputFile {
    OutputStream outputStream;

    public BinaryOutputFile(String file) {
        try {
            if (file.endsWith("gz"))
                outputStream = new GZIPOutputStream(new FileOutputStream(file));
            else
                outputStream = new FileOutputStream(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void write(byte b) {
        try {
            outputStream.write(b);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void write(int i) {
        write((byte) (i & 0xFF));
        i = i >> 8;
        write((byte) (i & 0xFF));
        i = i >> 8;
        write((byte) (i & 0xFF));
        i = i >> 8;
        write((byte) (i & 0xFF));
    }

    public void write(long d) {
        write((int) d);
        write((int) (d >> 32));
    }

    public void write(double d) {
        write(Double.doubleToLongBits(d));
    }

    public void write(float f) {
        write(Float.floatToIntBits(f));
    }

    public void write(char c) {
        write((byte) c);
        write((byte) (c >> 8));
    }

    public void write(String str) {
        final int strlen = str.length();
        int utflen = strlen; // optimized for ASCII

        for (int i = 0; i < strlen; i++) {
            int c = str.charAt(i);
            if (c >= 0x80 || c == 0)
                utflen += (c >= 0x800) ? 2 : 1;
        }

        if (utflen > 65535 || /* overflow */ utflen < strlen)
            throw new RuntimeException("String too long");

        final byte[] bytearr;
        bytearr = new byte[utflen + 2];
        int count = 0;
        bytearr[count++] = (byte) ((utflen >>> 8) & 0xFF);
        bytearr[count++] = (byte) ((utflen) & 0xFF);

        int i = 0;
        for (i = 0; i < strlen; i++) { // optimized for initial run of ASCII
            int c = str.charAt(i);
            if (c >= 0x80 || c == 0) break;
            bytearr[count++] = (byte) c;
        }

        for (; i < strlen; i++) {
            int c = str.charAt(i);
            if (c < 0x80 && c != 0) {
                bytearr[count++] = (byte) c;
            } else if (c >= 0x800) {
                bytearr[count++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                bytearr[count++] = (byte) (0x80 | ((c >> 6) & 0x3F));
                bytearr[count++] = (byte) (0x80 | ((c) & 0x3F));
            } else {
                bytearr[count++] = (byte) (0xC0 | ((c >> 6) & 0x1F));
                bytearr[count++] = (byte) (0x80 | ((c) & 0x3F));
            }
        }
        try {
            outputStream.write(bytearr, 0, utflen + 2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }
}
