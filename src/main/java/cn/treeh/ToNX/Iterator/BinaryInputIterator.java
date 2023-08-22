package cn.treeh.ToNX.Iterator;

import java.io.*;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

public class BinaryInputIterator implements Iterator<Integer> {
    InputStream inputStream;

    public BinaryInputIterator(String file) throws IOException {
        if (file.endsWith("gz"))
            inputStream = new GZIPInputStream(new FileInputStream(file));
        else
            inputStream = new FileInputStream(file);
    }

    Integer i = null;

    public boolean hasNextInt() {
        if (i != null)
            return true;
        try {
            i = inputStream.read();
            i = (i << 8) + inputStream.read();
            i = (i << 8) + inputStream.read();
            i = (i << 8) + inputStream.read();
        } catch (Exception e) {
            i = null;
            return false;
            //"Means no left"
        }
        return true;
    }

    public Integer nextInt() {
        if (hasNextInt()) {
            Integer res = i;
            i = null;
            return res;
        }
        return null;
    }

    Long l = null;

    public boolean hasNextLong() {
        if (l != null)
            return true;

        if (hasNextInt())
            l = (long) nextInt();
        else
            return false;
        if (hasNextInt())
            l = (l << 32) + nextInt();
        else
            return false;

        return true;
    }

    public Long nextLong() {
        if (hasNextLong()) {
            Long res = l;
            l = null;
            return res;
        }
        return null;
    }

    public boolean hasNextDouble() {
        return hasNextLong();
    }

    public Double nextDouble() {
        if (hasNextDouble())
            return Double.longBitsToDouble(nextLong());
        return null;
    }

    public boolean hasNextFloat() {
        return hasNextInt();

    }

    public Float nextFloat() {
        if (hasNextInt())
            return Float.intBitsToFloat(nextInt());
        return null;
    }

    Character c = null;

    public boolean hasNextChar() {
        if (i != null)
            return true;
        try {
            c = (char) inputStream.read();
            c = (char) ((c << 8) + inputStream.read());
        } catch (Exception e) {
            c = null;
            return false;
            //"Means no left"
        }
        return true;
    }

    public Character nextChar() {
        if (hasNextChar()) {
            Character res = c;
            c = null;
            return res;
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        try {
            return inputStream.available() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Integer next() {
        try {
            if (hasNext())
                return inputStream.read();
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
