package com.itweber.jgetinfo;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TempFile {

    File tempFile;

    public TempFile(String Name) {
        try {
            this.tempFile = File.createTempFile(Name + "_", null);
            System.out.println(this.tempFile.getAbsolutePath());
            this.tempFile.deleteOnExit();
        } catch (IOException ex) {
            Logger.getLogger(TempFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
