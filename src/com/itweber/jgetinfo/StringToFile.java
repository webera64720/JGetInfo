package com.itweber.jgetinfo;

import java.io.IOException;
import java.io.PrintWriter;

public class StringToFile {

    protected StringToFile() {

    }

    public static void write(String fileName, String str) throws IOException {
        PrintWriter out = new PrintWriter(fileName);
        out.write("Created with JGetInfo V" + JGetInfo.VERSION
                + System.getProperty("line.separator") + System.getProperty("line.separator"));
        out.write(str);
        out.close();
    }
}
