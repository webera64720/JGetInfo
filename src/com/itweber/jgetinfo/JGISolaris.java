package com.itweber.jgetinfo;

import java.io.File;

public class JGISolaris extends OSSpecific {

    public static String getPatches() {
        String ret = "### Patch INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("showrev -a");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getEnv() {
        String ret = "### ENV INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("env");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getPsrinfo() {
        String ret = "### PSRINFO INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("psrinfo -pv");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getCPUInfo() {
        String ret = "### CPU INFO ###" + System.getProperty("line.separator");
        ret += FileHelper.readFile("/proc/cpuinfo");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getMemInfo() {
        String ret = "### MEM INFO ###" + System.getProperty("line.separator");
        ret += FileHelper.readFile("/proc/meminfo");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getFreeDiscSpace() {
        String ret = "### HD INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("df -kh");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getNetConnections() {
        String ret = "### NET INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("/sbin/ifconfig");
        ret += System.getProperty("line.separator");
        ret += SystemCall.runCMD("/sbin/ip link");
        ret += System.getProperty("line.separator");
        ret += SystemCall.runCMD("netstat -an");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getLinuxRelease() {
        String ret = "### SUN INFO ###" + System.getProperty("line.separator");
        if (new File("/etc/release").exists()) {
            ret += FileHelper.readFile("/etc/release");
        }
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getLimits() {
        String ret = "### HARD LIMITS ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("ulimit -aH");
        ret += "\n";
        ret += "### SOFT LIMITS ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("ulimit -aS");
        ret += System.getProperty("line.separator");
        try {
            int openFiles = Integer.parseInt(SystemCall.runCMD("ulimit -n"));
            if (openFiles < 8192) {
                System.out.println("Recommendation: Limit for Open Files ("
                        + openFiles
                        + ") is too less. Please increase it at minimum to 8192.");
            }
        } catch (NumberFormatException e) {
        }
        return ret;
    }

    public static String getMessages() {
        String ret = "### DMESG INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("dmesg");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getJavaInfo() {
        String ret = "### JAVA INFO ###" + System.getProperty("line.separator");
        try {
            String java_home = System.getenv("JAVA_HOME");
            String java_exe = java_home + "/bin/java";
            SystemVariable sv = new SystemVariable("JAVA_HOME", java_home);
            ret += SystemCall.runCMD(java_exe + " -version 2>&1", sv);
            ret += System.getProperty("line.separator") + System.getProperty("line.separator")
                    + "JAVA_HOME=" + java_home;
        } catch (NullPointerException e) {
        }
        ret += System.getProperty("line.separator");
        return ret;
    }
}
