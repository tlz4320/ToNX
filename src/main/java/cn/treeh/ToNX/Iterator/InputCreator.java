package cn.treeh.ToNX.Iterator;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class InputCreator {
    public static Iterator openInputIterator(String file, String s, boolean byteInput) throws IOException {
        if(byteInput)
            return new BinaryInputIterator(file);
        return openInputIterator(file, s);
    }

    public static Iterator openInputIterator(String file, int cols, boolean byteInput) throws IOException {
        if(byteInput)
            return new BinaryInputIterator(file);
        return openInputIterator(file,cols);
    }


    public static Iterator openInputIterator(String file, String s, int cols, boolean byteInput) throws IOException {
        if(byteInput)
            return new BinaryInputIterator(file);
        return openInputIterator(file, s, cols);
    }
    public static Iterator openInputIterator(String file, boolean byteInput) throws IOException {
        if(byteInput)
            return new BinaryInputIterator(file);
        return openInputIterator(file);
    }
    public InputCreator() {
    }

    public static InputIterator openInputIterator(String file) throws IOException {
        return openInputIterator((String)file, "\t", -1);
    }

    public static InputIterator openInputIterator(String file, String s) throws IOException {
        return openInputIterator((String)file, s, -1);
    }

    public static InputIterator openInputIterator(String file, int cols) throws IOException {
        return openInputIterator(file, "\t", cols);
    }

    public static InputIterator openInputIterator(String file, String s, int cols) throws IOException {
        File temp = new File(file);
        if (!temp.exists()) {
            return null;
        } else if (temp.isDirectory()) {
            return null;
        } else {
            return (InputIterator)(file.endsWith(".gz") ? new GzInputIterator(file, s, cols) : new TextInputIterator(file, s, cols));
        }
    }

    public static InputIterator openInputIterator(File file) throws IOException {
        return openInputIterator((File)file, "\t", -1);
    }

    public static InputIterator openInputIterator(File file, String s) throws IOException {
        return openInputIterator((File)file, s, -1);
    }

    public static InputIterator openInputIterator(File file, int cols) throws IOException {
        return openInputIterator(file, "\t", cols);
    }

    public static InputIterator openInputIterator(File file, String s, int cols) throws IOException {
        return openInputIterator(file.getAbsolutePath(), s, cols);
    }
}
