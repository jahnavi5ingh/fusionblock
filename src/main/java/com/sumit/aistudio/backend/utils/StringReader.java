package com.sumit.aistudio.backend.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StringReader implements  Reader {

    private final String[] lines;
    private  int index;
    String path;
    String data;
    public StringReader(String path,String data) throws FileNotFoundException {
        this.path = path;
       this.data = data;
       lines = data.split("\n");
       this.index = 0;
    }

    @Override
    public String readLine() throws IOException {
        if(index>=lines.length) return null;
        return lines[index++];
    }

    @Override
    public void close() {

    }
}
