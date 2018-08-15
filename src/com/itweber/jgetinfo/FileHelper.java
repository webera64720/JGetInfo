package com.itweber.jgetinfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHelper {

    public static String readFile(String fullPath) {
        String ret = "";
        if (existFile(fullPath)) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(fullPath));
            } catch (FileNotFoundException e) {
                Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, e);
            }
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.getProperty("line.separator"));
                    line = br.readLine();
                }
                ret = sb.toString();
            } catch (IOException e) {
                Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        } else {
            ret = "File " + fullPath + " doesn't exist.";
        }
        return ret;
    }
    
    public static String grepFile(String fullPath, String searchString) {
        String ret = "";
        if (existFile(fullPath)) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(fullPath));
            } catch (FileNotFoundException e) {
                Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, e);
            }
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    if(line.contains(searchString)){
                        sb.append(line);
                    }
                    line = br.readLine();
                }
                ret = sb.toString();
            } catch (IOException e) {
                Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        } else {
            ret = "File " + fullPath + " doesn't exist.";
        }
        return ret;
    }

    public static boolean existFile(String fullPath) {
        File f = new File(fullPath);
        return f.exists();
    }

    public static void deleteIfExist(String path) {
        if (new File(path).exists()) {
            System.out.println("Deleting " + path);
            new File(path).delete();
        }
    }

    public static String[] getFilesFromFolder(String Dir) {
        File dir = new File(Dir);
        File[] childs = dir.listFiles();
        ArrayList<String> fileList = new ArrayList<String>();
        if (childs != null) {
            for (File f : childs) {
                if (f.isDirectory()) {
                    fileList.add(f.getAbsolutePath() + File.separator);
                    String[] child2 = getFilesFromFolder(f.getAbsolutePath());
                    for (String s : child2) {
                        fileList.add(s);
                    }
                } else {
                    fileList.add(f.getAbsolutePath());
                }
            }
        }
        String[] ret = new String[fileList.size()];
        for (int i = 0; i < fileList.size(); i++) {
            ret[i] = fileList.get(i);
        }
        return ret;
    }

    public static void copyFile(String Source, String Target) throws FileNotFoundException, IOException {
        File s = new File(Source);
        File t = new File(Target);
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(s);
            os = new FileOutputStream(t);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static void createFolder(String path) {
        new File(path).mkdir();
    }

    public static void delete(String path) {
        new File(path).delete();
    }
}
