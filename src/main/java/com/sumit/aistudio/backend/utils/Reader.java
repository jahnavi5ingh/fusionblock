package com.sumit.aistudio.backend.utils;

import java.io.IOException;

public interface Reader {
    String readLine() throws IOException;

    void close();
}
