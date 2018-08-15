package com.itweber.jgetinfo;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemCall {

    private static String[] getShellCommand(String cmd) {
        String[] ret = null;
        if (System.getProperty("os.name").equals("Linux")) {
            ret = new String[]{getShell(), "-c", cmd};
        }
        if (System.getProperty("os.name").equals("AIX")) {
            ret = new String[]{getShell(), "-c", cmd};
        }
        if (System.getProperty("os.name").equals("HPUX")) {

            ret = new String[]{getShell(), "-c", cmd};
        }
        if (System.getProperty("os.name").equals("Solaris")) {
            ret = new String[]{getShell(), "-c", cmd};
        }
        if (System.getProperty("os.name").equals("SunOS")) {
            ret = new String[]{getShell(), "-c", cmd};
        }
        if (System.getProperty("os.name").contains("Windows")) {
            String windir = System.getenv("WINDIR");
            ret = new String[]{windir + "\\System32\\cmd.exe", "/c",
                "\"" + cmd + "\""};
        }
        return ret;
    }

    public static String getShell() {
        if (new File("/bin/bash").exists()) {
            return "/bin/bash";
        } else if (new File("/bin/ksh").exists()) {
            return "/bin/ksh";
        } else {
            System.out.println("No suitable shell found. This program needs bash or ksh installed.");
            System.exit(1);
            return "";
        }
    }

    public static String runCMD(String cmd) {
        String line = "";
        ProcessBuilder pb = new ProcessBuilder(getShellCommand(cmd));
        try {
            Process p = pb.start();
            Scanner s = new Scanner(p.getInputStream()).useDelimiter("\\Z");
            line = s.next();
            s.close();
        } catch (IOException e) {
            Logger.getLogger(SystemCall.class.getName()).log(Level.SEVERE, null, e);
        } catch (NoSuchElementException e) {
            return new String("");
        }
        return line;
    }

    public static String runCMD(String cmd, SystemVariable[] sysEnv) {
        String line = "";
        ProcessBuilder pb = new ProcessBuilder(getShellCommand(cmd));
        Map<String, String> env = pb.environment();
        for (int i = 0; i < sysEnv.length; i++) {
            env.put(sysEnv[i].getName(), sysEnv[i].getValue());
        }
        try {
            Process p = pb.start();
            Scanner s = new Scanner(p.getInputStream()).useDelimiter("\\Z");
            line = s.next();
            s.close();
        } catch (IOException e) {
            Logger.getLogger(SystemCall.class.getName()).log(Level.SEVERE, null, e);
        } catch (NoSuchElementException e) {
            return new String("");
        }
        return line;
    }

    public static String runCMD(String cmd, SystemVariable sysEnv) {
        String line = "";
        ProcessBuilder pb = new ProcessBuilder(getShellCommand(cmd));
        Map<String, String> env = pb.environment();
        try {
            env.put(sysEnv.getName(), sysEnv.getValue());
        } catch (NullPointerException e) {
        }
        try {
            Process p = pb.start();
            Scanner s = new Scanner(p.getInputStream()).useDelimiter("\\Z");
            line = s.next();
            s.close();
        } catch (IOException e) {
            Logger.getLogger(SystemCall.class.getName()).log(Level.SEVERE, null, e);
        } catch (NoSuchElementException e) {
            return new String("");
        }
        return line;
    }

    public static String runJavaCMD(String cmd) {
        String ret = "";
        try {
            String java_home = System.getenv("JAVA_HOME");
            String exe = "java";
            if (OSSpecific.getOSType().contains("Windows")) {
                exe = "java.exe";
            }
            String java_exe = java_home + "/bin/" + exe;
            if (!new File(java_exe).exists()) {
                String[] files2 = FileHelper.getFilesFromFolder("."
                        + File.separator + "bin");
                for (String s : files2) {
                    if (new File(s).getName().equals(exe)) {
                        java_exe = s;
                    }
                }
                String[] files3 = FileHelper.getFilesFromFolder("."
                        + File.separator + "bin64");
                for (String s : files3) {
                    if (new File(s).getName().equals(exe)) {
                        java_exe = s;
                    }
                }
            }
            if (java_home == null) {
                java_home = java_exe.replace(File.separator + "bin"
                        + File.separator + exe, "");
                System.out.println("JAVA_HOME: " + java_home);
            }
            if (FileHelper.existFile(java_home)) {
                SystemVariable sv = new SystemVariable("JAVA_HOME", java_home);
                ret += SystemCall.runCMD("\"" + java_exe + "\" " + cmd + " 2>&1", sv);
            } else {
                ret += SystemCall.runCMD("\"" + java_exe + "\" " + cmd + " 2>&1");
            }
        } catch (NullPointerException e) {
        }

        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String runJavaCMD(String cmd, String java_home) {
        String ret = "";
        try {
            String java_exe = java_home + "/bin/java";
            SystemVariable sv = new SystemVariable("JAVA_HOME", java_home);
            ret += SystemCall.runCMD("\"" + java_exe + "\" " + cmd + " 2>&1", sv);
        } catch (NullPointerException e) {
        }

        ret += System.getProperty("line.separator");
        return ret;
    }
}
