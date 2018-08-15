package com.itweber.jgetinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCreator {

    public static void addToZipFile(String fileName, ZipOutputStream zos)
            throws IOException {
        File file = new File(fileName);
        if (!file.isDirectory()) {
            FileInputStream fis = new FileInputStream(file);
            String currentPath = new File(".").getAbsolutePath();
            String relativePath = fileName.replace(currentPath, "");
            ZipEntry zipEntry = new ZipEntry(relativePath.substring(1,
                    relativePath.length()));
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            try {
                while ((length = fis.read(bytes)) >= 0) {
                    zos.write(bytes, 0, length);
                }
            } catch (IOException e) {
                Logger.getLogger(ZipCreator.class.getName()).log(Level.SEVERE, "Could not add File "
                        + file.getAbsolutePath() + " to the Zip file.", e);
            }
            zos.closeEntry();
            fis.close();
        }
    }

}
