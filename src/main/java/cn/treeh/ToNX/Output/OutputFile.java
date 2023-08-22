package cn.treeh.ToNX.Output;

import java.io.Closeable;
import java.io.Flushable;
import java.io.OutputStream;

public interface OutputFile extends Closeable, Flushable {
    void write(byte b);

    void write(int i);

    void write(long d);

    void write(double d);

    void write(float f);

    void write(char c);

    void write(String str);
}
