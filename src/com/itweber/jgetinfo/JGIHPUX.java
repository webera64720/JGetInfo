package com.itweber.jgetinfo;

public class JGIHPUX extends OSSpecific {

    public static String getCPUInfo() {
        String ret = "### CPU INFO ###" + System.getProperty("line.separator");
        ret += SystemCall
                .runCMD("echo 'selclass qualifier cpu;info;wait;infolog' |/usr/sbin/cstm");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getMEMInfo() {
        String ret = "### MEM INFO ###" + System.getProperty("line.separator");
        ret += SystemCall
                .runCMD("echo 'selclass qualifier memory;info;wait;infolog' |/usr/sbin/cstm");
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

    public static String getBundles() {
        String ret = "### BUNDLE INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("/usr/sbin/swlist -l bundle");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getPatches() {
        String ret = "### PATCHES INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("/usr/sbin/swlist -l patch");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getSWAPInfo() {
        String ret = "### SWAP INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("/usr/sbin/swapinfo -a");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getErrorLogs() {
        String ret = "### ERROR LOGS ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("cat /var/adm/syslog/syslog.log");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getFreeDiscSpace() {
        String ret = "### HD INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("bdf");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getEnv() {
        String ret = "### ENV INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("env");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getLimits() {
        String ret = "### HARD LIMITS ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("ulimit -aH");
        ret += System.getProperty("line.separator");
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
