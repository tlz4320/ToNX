package cn.treeh.ToNX.Output;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class DefaultOutputFile implements OutputFile {
    BufferedWriter writer;
    public DefaultOutputFile(String file){
        try {
            if(file.endsWith("gz"))
                writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(file))));
            else
                writer = new BufferedWriter(new FileWriter(file));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public void close() throws IOException {
        writer.close();
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void write(byte b) {
        try {
            writer.write(b + "");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void write(int i) {
        try {
            writer.write(i + "");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void write(long d) {
        try {
            writer.write(d + "");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void write(double d) {
        try {
            writer.write(d + "");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void write(float f) {
        try {
            writer.write(f + "");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void write(char c) {
        try {
            writer.write(c);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void write(String str) {
        try {
            writer.write(str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
