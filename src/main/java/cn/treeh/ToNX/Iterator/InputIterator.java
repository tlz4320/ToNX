package cn.treeh.ToNX.Iterator;

import cn.treeh.ToNX.Exception.FormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public interface InputIterator extends Iterator<String[]> {
    public String getThisLine();
    @Override
    public boolean hasNext();
    @Override
    public String[] next();
    public void close() throws IOException;
}
