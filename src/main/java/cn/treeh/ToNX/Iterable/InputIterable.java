package cn.treeh.ToNX.Iterable;

import cn.treeh.ToNX.Iterator.InputCreator;
import cn.treeh.ToNX.Iterator.InputIterator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.Consumer;

public class InputIterable implements Iterable<String[]> {
    InputIterator inputIterator;
    public InputIterable(String file) throws IOException {
        this(file, "\t", -1);
    }
    public InputIterable(String file, String s, int cols) throws IOException{
        inputIterator = InputCreator.openInputIterator(file, s, cols);
    }
    public InputIterable(String file, int cols) throws IOException{
        this(file, "\t", cols);
    }
    public InputIterable(String file, String s) throws IOException{
        this(file, s, -1);
    }
    @Override
    public Iterator<String[]> iterator() {
        return inputIterator;
    }
}
