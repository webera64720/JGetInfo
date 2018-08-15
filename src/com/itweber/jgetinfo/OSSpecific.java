package com.itweber.jgetinfo;

public class OSSpecific {

    public static String getOSType() {
        return System.getProperty("os.name");
    }

    public static String getClassPath() {
        return System.getProperty("java.class.path");
    }

    public static String getJavaLibraryPath() {
        return System.getProperty("java.library.path");
    }

    public static String getTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getUserName() {
        return System.getProperty("user.name");
    }

    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    public static String getUserDir() {
        return System.getProperty("user.dir");
    }

}
