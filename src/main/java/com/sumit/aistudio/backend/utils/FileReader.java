package com.sumit.aistudio.backend.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader implements  Reader {

    String path;
    BufferedReader reader;
    public FileReader(String path) throws FileNotFoundException {
        this.path = path;
         reader = new BufferedReader(new java.io.FileReader(path));
    }

    @Override
    public String readLine() throws IOException {
        return reader.readLine();
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
