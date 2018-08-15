package com.itweber.jgetinfo;

import java.io.File;
import java.util.Map;

public class JGIWindows extends OSSpecific {

    public static String getSystemInfo() {
        String ret = "### SYS INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("systeminfo");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getJavaInfo() {
        String ret = "### JAVA INFO ###" + System.getProperty("line.separator");
        String java_home = System.getenv("JAVA_HOME");
        String java_exe = "";
        if (java_home == null) {
            if (FileHelper.existFile("." + File.separator + "cmplst.txt")) {
                String[] files = FileHelper.getFilesFromFolder("."
                        + File.separator + "bin64");
                for (String s : files) {
                    if (s.contains("java.exe")) {
                        System.out.println("Retrieve Java Version from: " + s);
                        java_exe = s;
                    }
                }
                if (java_exe.equals("")) {
                    String[] files2 = FileHelper.getFilesFromFolder("."
                            + File.separator + "bin");
                    for (String s : files2) {
                        if (s.contains("java.exe")) {
                            System.out.println("JAVA is set to: " + s);
                            java_exe = s;
                        }
                    }
                }
            }
        } else {
            java_exe = java_home + "\\bin\\java.exe";
        }
        SystemVariable sv = new SystemVariable("JAVA_HOME", java_home);
        ret += SystemCall.runCMD("\"" + java_exe + " -version 2>&1\"", sv);
        ret += System.getProperty("line.separator") + System.getProperty("line.separator") + "JAVA_HOME=" + java_home;
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getNetworkInfo() {
        String ret = "";
        ret += SystemCall.runCMD("netstat -a");
        ret += System.getProperty("line.separator");
        ret += SystemCall.runCMD("ipconfig /all");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getEnv() {
        Map<String, String> env = System.getenv();
        String ret = "";
        for (Map.Entry<String, String> entry : env.entrySet()) {
            ret += entry.getKey() + " = " + entry.getValue() + System.getProperty("line.separator");
        }
        return ret;
    }

    public static String getFreeDiskSpace(String driveLetter) {
        String ret = "### Free disk information ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("fsutil volume diskfree " + driveLetter);
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getIISConfig() {
        String ret = "### IIS Configuration ###" + System.getProperty("line.separator");
        if (new File(System.getenv("WINDIR") + "\\system32\\inetsrv\\appcmd.exe").exists()) {
            ret += SystemCall.runCMD("%windir%\\system32\\inetsrv\\appcmd list config /config /xml");
        } else {
            ret += "%windir%\\system32\\inetsrv\\appcmd.exe not found!";
        }
        ret += System.getProperty("line.separator");
        return ret;
    }
}
