package cn.treeh.ToNX.Iterator;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class GzInputIterator extends DefaultInputIterator{

    @Override
    public void open(String file, String s, int cols) throws IOException {
        this.file = file;
        sep = s;
        this.cols = cols;
        reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));
    }
    public GzInputIterator(String file, String s, int cols) throws IOException {
        super(file, s, cols);
    }

}
