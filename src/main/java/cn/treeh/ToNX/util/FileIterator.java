package cn.treeh.ToNX.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class FileIterator implements Iterator{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileIterator.class);
    BufferedReader reader;
    String buffered_line;
    public FileIterator(File infile){

        if(!infile.isFile()) {
            LOGGER.error("File not exists");
            throw new RuntimeException("File not exists");
        }
        try {
            reader = new BufferedReader(new FileReader(infile));
            buffered_line = reader.readLine();
        }catch (Exception e){
            LOGGER.error(e.toString());
            throw new RuntimeException(e);
        }
    }
    public FileIterator(String file){
        this(new File(file));
    }


    @Override
    public boolean hasNext() {
        return buffered_line != null;
    }

    @Override
    public String next() {
        String line = null;
        if(buffered_line != null) {
            line = buffered_line;
            try {
                buffered_line = reader.readLine();
            } catch (IOException e) {
                LOGGER.error(e.toString());
                throw new RuntimeException(e);
            }
        }
        return line;
    }
}
