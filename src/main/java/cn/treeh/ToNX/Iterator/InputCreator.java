package cn.treeh.ToNX.Iterator;

import java.io.File;
import java.io.IOException;

public class InputCreator {

    public static InputIterator openInputIterator(String file) throws IOException {
        return openInputIterator(file, "\t", -1);
    }
    public static InputIterator openInputIterator(String file, String s) throws IOException {
        return openInputIterator(file, s, -1);
    }
    public static InputIterator openInputIterator(String file, int cols) throws IOException {
        return openInputIterator(file, "\t", cols);
    }
    public static InputIterator openInputIterator(String file, String s, int cols) throws IOException {
        File temp = new File(file);
        if(!temp.exists())
            return null;
        if(temp.isDirectory())
            return null;
        if(file.endsWith(".gz")){
            return new GzInputIterator(file, s, cols);
        }
        return new TextInputIterator(file, s, cols);
    }
    public static InputIterator openInputIterator(File file) throws IOException {
        return openInputIterator(file, "\t", -1);
    }
    public static InputIterator openInputIterator(File file, String s) throws IOException {
        return openInputIterator(file, s, -1);
    }
    public static InputIterator openInputIterator(File file, int cols) throws IOException {
        return openInputIterator(file, "\t", cols);
    }
    public static InputIterator openInputIterator(File file, String s, int cols) throws IOException {
        return openInputIterator(file.getAbsolutePath(), s, cols);
    }
}
