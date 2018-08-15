package com.itweber.jgetinfo;

import java.io.File;

public class JGILinux extends OSSpecific {

    public JGILinux() {

    }

    public static String getSeLinux() {
        String ret = "### SELINUX INFO ###" + System.getProperty("line.separator");
        ret += FileHelper.readFile("/etc/selinux/config");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getLimits() {
        String ret = "### File Max - System wide ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("cat /proc/sys/fs/file-max");
        ret = "### HARD LIMITS ###" + System.getProperty("line.separator");
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

    public static String getPatches() {
        String ret = "### RPM/APT INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("rpm -qa");
        ret += SystemCall.runCMD("dpkg -l");
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
        if (new File("/sbin/ip").exists()) {
            ret += SystemCall.runCMD("/sbin/ip a");
            ret += System.getProperty("line.separator");
            ret += SystemCall.runCMD("/sbin/ip link");
            ret += System.getProperty("line.separator");
        } else if (new File("/sbin/ifconfig").exists()) {
            ret += SystemCall.runCMD("/sbin/ifconfig");
            ret += System.getProperty("line.separator");
        }
        if (new File("/usr/sbin/ss").exists()) {
            ret += SystemCall.runCMD("ss -tulpen");
            ret += System.getProperty("line.separator");
        } else if (new File("/usr/bin/netstat").exists()) {
            ret += SystemCall.runCMD("netstat -tulpen");
            ret += System.getProperty("line.separator");
        }
        return ret;
    }

    public static String getLinuxRelease() {
        String ret = "### LINUX INFO ###" + System.getProperty("line.separator");
        ret += System.getProperty("line.separator");
        ret += "Release file:" + System.getProperty("line.separator");
        if (new File("/etc/redhat-release").exists()) {
            ret += FileHelper.readFile("/etc/redhat-release");
        }
        if (new File("/etc/SuSE-release").exists()) {
            ret += FileHelper.readFile("/etc/SuSE-release");
        }
        if (new File("/etc/os-release").exists()) {
            ret += FileHelper.readFile("/etc/os-release");
        }
        ret += System.getProperty("line.separator");
        ret += "Kernel:" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("uname -a");
        ret += System.getProperty("line.separator") + System.getProperty("line.separator");
        ret += "LSB Release:" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("lsb_release -a");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getMessages() {
        String ret = "### DMESG INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("dmesg");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getEnv() {
        String ret = "### ENV INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("env");
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

    public static String getLocaleInformation() {
        String ret = "### Locale INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("locale -a");
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getOpenFiles() {
        String ret = "### OpenFiles ###" + System.getProperty("line.separator");
        try {
            ret += SystemCall.runCMD("for i in $(ps -aef | grep java | awk '{print $2}'); do xargs --null --max-args=1 echo < /proc/$i/cmdline && echo \"\" && xargs --null --max-args=1 echo < /proc/$i/environ && echo \"\" &&  echo OpenFiles && ls -1 /proc/$i/fd | wc -l && echo \"\" && echo \"\";done");
            ret += SystemCall.runCMD("for i in $(ps -aef | grep BIBusTKServerMain | awk '{print $2}'); do xargs --null --max-args=1 echo < /proc/$i/cmdline && echo \"\" && xargs --null --max-args=1 echo < /proc/$i/environ && echo \"\" &&  echo OpenFiles && ls -1 /proc/$i/fd | wc -l && echo \"\" && echo \"\";done");
            ret += SystemCall.runCMD("for i in $(ps -aef | grep BmtMDProviderMain | awk '{print $2}'); do xargs --null --max-args=1 echo < /proc/$i/cmdline && echo \"\" && xargs --null --max-args=1 echo < /proc/$i/environ && echo \"\" &&  echo OpenFiles && ls -1 /proc/$i/fd | wc -l && echo \"\" && echo \"\";done");
            ret += SystemCall.runCMD("for i in $(ps -aef | grep cgsServer.sh | awk '{print $2}'); do xargs --null --max-args=1 echo < /proc/$i/cmdline && echo \"\" && xargs --null --max-args=1 echo < /proc/$i/environ && echo \"\" &&  echo OpenFiles && ls -1 /proc/$i/fd | wc -l && echo \"\" && echo \"\";done");
            ret += SystemCall.runCMD("for i in $(ps -aef | grep cogbootstrapser | awk '{print $2}'); do xargs --null --max-args=1 echo < /proc/$i/cmdline && echo \"\" && xargs --null --max-args=1 echo < /proc/$i/environ && echo \"\" &&  echo OpenFiles && ls -1 /proc/$i/fd | wc -l && echo \"\" && echo \"\";done");
            ret += "\n";
        } catch (NullPointerException e) {
        }
        ret += System.getProperty("line.separator");
        return ret;
    }

    public static String getLddStatus(String cogroot) {
        String ret = "### LDD ###" + System.getProperty("line.separator");
        SystemVariable sv = new SystemVariable("LD_LIBRARY_PATH", cogroot + "/bin64");
        try {
            //ret += SystemCall.runCMD("ldd "+cogroot+"/cgi-bin/mod2*.so");
            ret += "ldd " + cogroot + "/bin64/BIBusTKServerMain" + System.getProperty("line.separator");
            ret += SystemCall.runCMD("ldd " + cogroot + "/bin64/BIBusTKServerMain");
            ret += "\n";
            ret += "ldd " + cogroot + "/bin64/cogbootstrapservice" + System.getProperty("line.separator");
            ret += SystemCall.runCMD("ldd " + cogroot + "/bin64/cogbootstrapservice");
            ret += "\n";
            ret += "\n";
            ret += "### Including custom LD_LIBRARY_PATH ###" + System.getProperty("line.separator");
            ret += SystemCall.runCMD("env | grep LD_LIBRARY_PATH", sv);
            ret += "\n";
            //ret += SystemCall.runCMD("ldd "+cogroot+"/cgi-bin/mod2*.so", sv);
            ret += "ldd " + cogroot + "/bin64/BIBusTKServerMain" + System.getProperty("line.separator");
            ret += SystemCall.runCMD("ldd " + cogroot + "/bin64/BIBusTKServerMain", sv);
            ret += "\n";
            ret += "ldd " + cogroot + "/bin64/cogbootstrapservice" + System.getProperty("line.separator");
            ret += SystemCall.runCMD("ldd " + cogroot + "/bin64/cogbootstrapservice", sv);
            ret += System.getProperty("line.separator");
        } catch (NullPointerException e) {
        }
        ret += System.getProperty("line.separator");
        return ret;
    }
    
    public static String getDockerInformation() {
        String ret = "### Docker INFO ###" + System.getProperty("line.separator");
        ret += SystemCall.runCMD("docker --version");
        ret += System.getProperty("line.separator");
        ret += System.getProperty("line.separator");
        ret += SystemCall.runCMD("docker images");
        ret += System.getProperty("line.separator");
        ret += System.getProperty("line.separator");
        ret += SystemCall.runCMD("docker ps -a");
        ret += System.getProperty("line.separator");
        return ret;
    }
}
