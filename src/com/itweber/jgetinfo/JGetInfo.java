package com.itweber.jgetinfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

public class JGetInfo {

    public final static String VERSION = "4.0.0";
    private String tempFolder;
    private String zipFile;
    private String cogPath;
    private String scriptExt;
    private boolean deleteTempFiles = true;
    private boolean createZipFile = true;
    private boolean bigLogs = false;
    private boolean collectCognosLogs = true;

    public JGetInfo(String[] args) {
        if (args.length > 0) {
            for (String s : args) {
                if (s.toLowerCase().contains("-version")) {
                    System.out.println("JGetInfo " + JGetInfo.VERSION);
                    System.exit(0);
                }
                if (s.toLowerCase().contains("-help")) {
                    printHelp();
                    System.exit(0);
                }
                if (s.toLowerCase().contains("-nologs")) {
                    collectCognosLogs = false;
                }
                if (s.toLowerCase().contains("-biglogs")) {
                    System.out.println("------------------------------------------------------------------------");
                    System.out.println("Warning: Collecting big log files (-biglogs).");
                    System.out.println("If you get a OutOfMemory Exception the logs are too big.");
                    System.out.println("You could increase the Java Heap like:");
                    System.out.println("    \"java -Xmx8192m -jar JGetInfo.jar -biglogs\"");
                    System.out.println("------------------------------------------------------------------------");
                    bigLogs = true;
                }
                if (s.toLowerCase().contains("-log")) {
                    try {
                        FileHandler fh = new FileHandler("JGetInfo.log");
                        Logger.getLogger(JGetInfo.class.getName()).addHandler(fh);
                        Logger.getLogger(JGetInfo.class.getName()).log(Level.INFO, "File Logger enabled.");
                    } catch (IOException ex) {
                        Logger.getLogger(JGetInfo.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SecurityException ex) {
                        Logger.getLogger(JGetInfo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (s.toLowerCase().contains("-iisonly")) {
                    FileHelper.deleteIfExist("iisinfo.zip");
                    try {
                        FileOutputStream fos = new FileOutputStream("iisinfo.zip");
                        ZipOutputStream zos = new ZipOutputStream(fos);
                        tempFolder = "." + File.separator + "iisinfo";
                        if (FileHelper.existFile("cmplst.txt")) {
                            cogPath = new File(".").getAbsolutePath();
                            System.out.println("Found Cognos Installation on " + cogPath);
                            String[] cogFiles = new String[]{cogPath + File.separator + "webcontent" + File.separator + "web.config",
                                cogPath + File.separator + "cgi-bin" + File.separator + "web.config",
                                cogPath + File.separator + "webcontent" + File.separator + "bi" + File.separator + "web.config"};
                            for (String cf : cogFiles) {
                                if (new File(cf).exists()) {
                                    System.out.println("Collecting " + new File(cf).getAbsolutePath());
                                    ZipCreator.addToZipFile(cf, zos);
                                }
                            }
                        }
                        System.out.println("Collecting " + new File("iis.xml").getAbsolutePath());
                        StringToFile.write("iis.xml", JGIWindows.getIISConfig());
                        ZipCreator.addToZipFile("iis.xml", zos);
                        zos.close();
                        fos.close();
                        FileHelper.deleteIfExist("iis.xml");
                    } catch (IOException e) {
                        Logger.getLogger(JGetInfo.class.getName()).log(Level.SEVERE, null, e);
                    }
                    File fullZipPath = new File("iisinfo.zip");
                    System.out.println();
                    System.out.println("Output file " + fullZipPath.getAbsolutePath());
                }
            }
        }
        collectInformation();
    }

    private void collectInformation() {
        String username = System.getProperty("user.name");
        System.out.println("Running jgetinfo V" + VERSION + " on " + OSSpecific.getOSType() + " as " + username);
        zipFile = "jgetinfo.zip";
        tempFolder = "." + File.separator + "sysinfo";
        FileHelper.createFolder(tempFolder);
        FileHelper.deleteIfExist(zipFile);
        scriptExt = "sh";

        if (OSSpecific.getOSType().equals("Linux")) {
            try {
                StringToFile.write(tempFolder + File.separator + "selinux.txt", JGILinux.getSeLinux());
                StringToFile.write(tempFolder + File.separator + "ulimits.txt", JGILinux.getLimits());
                StringToFile.write(tempFolder + File.separator + "packages.txt", JGILinux.getPatches());
                StringToFile.write(tempFolder + File.separator + "cpu.txt", JGILinux.getCPUInfo());
                StringToFile.write(tempFolder + File.separator + "mem.txt", JGILinux.getMemInfo());
                StringToFile.write(tempFolder + File.separator + "hdd.txt", JGILinux.getFreeDiscSpace());
                StringToFile.write(tempFolder + File.separator + "os.txt", JGILinux.getLinuxRelease());
                StringToFile.write(tempFolder + File.separator + "net.txt", JGILinux.getNetConnections());
                StringToFile.write(tempFolder + File.separator + "dmesg.txt", JGILinux.getMessages());
                StringToFile.write(tempFolder + File.separator + "java.txt", JGILinux.getJavaInfo());
                StringToFile.write(tempFolder + File.separator + "env.txt", JGILinux.getEnv());
                StringToFile.write(tempFolder + File.separator + "locale.txt", JGILinux.getLocaleInformation());
                StringToFile.write(tempFolder + File.separator + "docker.txt", JGILinux.getDockerInformation());
            } catch (IOException e) {
                Logger.getLogger(JGetInfo.class.getName()).log(Level.SEVERE, null, e);
            }

        }
        if (OSSpecific.getOSType().contains("Windows")) {
            scriptExt = "bat";
            try {
                StringToFile.write(tempFolder + File.separator + "systeminfo.txt", JGIWindows.getSystemInfo());
                StringToFile.write(tempFolder + File.separator + "env.txt", JGIWindows.getEnv());
                StringToFile.write(tempFolder + File.separator + "net.txt", JGIWindows.getNetworkInfo());
                StringToFile.write(tempFolder + File.separator + "iis.xml", JGIWindows.getIISConfig());
            } catch (IOException e) {
                Logger.getLogger(JGetInfo.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        if (OSSpecific.getOSType().equals("AIX")) {
            try {
                StringToFile.write(tempFolder + File.separator + "env.txt", JGIAIX.getEnv());
                StringToFile.write(tempFolder + File.separator + "cpu.txt", JGIAIX.getCPUInfo());
                StringToFile.write(tempFolder + File.separator + "ErrorLogs.txt", JGIAIX.getErrorLogs());
                StringToFile.write(tempFolder + File.separator + "hdd.txt", JGIAIX.getFreeDiscSpace());
                StringToFile.write(tempFolder + File.separator + "java.txt", JGIAIX.getJavaInfo());
                StringToFile.write(tempFolder + File.separator + "limits.txt", JGIAIX.getLimits());
                StringToFile.write(tempFolder + File.separator + "os.txt", JGIAIX.getOSLevel());
                StringToFile.write(tempFolder + File.separator + "page.txt", JGIAIX.getPageSpace());
                StringToFile.write(tempFolder + File.separator + "patches.txt", JGIAIX.getPatches());
                StringToFile.write(tempFolder + File.separator + "net.txt", JGIAIX.getNetConnections());
            } catch (IOException e) {
                Logger.getLogger(JGetInfo.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        if (OSSpecific.getOSType().equals("HPUX")) {
            try {
                StringToFile.write(tempFolder + File.separator + "env.txt", JGIHPUX.getEnv());
                StringToFile.write(tempFolder + File.separator + "bundles.txt", JGIHPUX.getBundles());
                StringToFile.write(tempFolder + File.separator + "cpu.txt", JGIHPUX.getCPUInfo());
                StringToFile.write(tempFolder + File.separator + "ErrorLogs.txt", JGIHPUX.getErrorLogs());
                StringToFile.write(tempFolder + File.separator + "hd.txt", JGIHPUX.getFreeDiscSpace());
                StringToFile.write(tempFolder + File.separator + "java.txt", JGIHPUX.getJavaInfo());
                StringToFile.write(tempFolder + File.separator + "limits.txt", JGIHPUX.getLimits());
                StringToFile.write(tempFolder + File.separator + "mem.txt", JGIHPUX.getMEMInfo());
                StringToFile.write(tempFolder + File.separator + "patches.txt", JGIHPUX.getPatches());
                StringToFile.write(tempFolder + File.separator + "swap.txt", JGIHPUX.getSWAPInfo());
                StringToFile.write(tempFolder + File.separator + "net.txt", JGIHPUX.getNetConnections());
            } catch (IOException e) {
                Logger.getLogger(JGetInfo.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        if (OSSpecific.getOSType().equals("Solaris") || OSSpecific.getOSType().equals("SunOS")) {
            try {
                StringToFile.write(tempFolder + File.separator + "env.txt", JGISolaris.getEnv());
                StringToFile.write(tempFolder + File.separator + "psrinfo.txt", JGISolaris.getPsrinfo());
                StringToFile.write(tempFolder + File.separator + "cpu.txt", JGISolaris.getCPUInfo());
                StringToFile.write(tempFolder + File.separator + "hd.txt", JGISolaris.getFreeDiscSpace());
                StringToFile.write(tempFolder + File.separator + "java.txt", JGISolaris.getJavaInfo());
                StringToFile.write(tempFolder + File.separator + "os.txt", JGISolaris.getLinuxRelease());
                StringToFile.write(tempFolder + File.separator + "mem.txt", JGISolaris.getMemInfo());
                StringToFile.write(tempFolder + File.separator + "dmesg.txt", JGISolaris.getMessages());
                StringToFile.write(tempFolder + File.separator + "net.txt", JGISolaris.getNetConnections());
                StringToFile.write(tempFolder + File.separator + "patches.txt", JGISolaris.getPatches());
                StringToFile.write(tempFolder + File.separator + "limits.txt", JGISolaris.getLimits());
            } catch (IOException e) {
                Logger.getLogger(JGetInfo.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        if (createZipFile) {
            try {
                FileOutputStream fos = new FileOutputStream(zipFile);
                ZipOutputStream zos = new ZipOutputStream(fos);
                if (FileHelper.existFile("cmplst.txt")) {
                    cogPath = new File(".").getAbsolutePath();
                    System.out.println("Found Cognos Installation on " + cogPath);
                    if (collectCognosLogs) {
                        File[] allDumps = new File(".").listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                if (name.contains(".dmp")) {
                                    return true;
                                }
                                if (name.contains("javacore") && name.contains(".txt")) {
                                    return true;
                                }
                                if (name.contains(".phd")) {
                                    return true;
                                }
                                return false;
                            }
                        });
                        String dumps = "";
                        System.out.println("Size of dumps: " + allDumps.length);
                        if (allDumps.length > 0) {
                            //TODO: Testing with Dump files
                            for (File f : allDumps) {
                                dumps += f.getAbsolutePath();
                            }
                        }
                        StringToFile.write(tempFolder + File.separator + "Dumps.txt", dumps);
                        if (OSSpecific.getOSType().contains("Windows")) {
                            StringToFile.write(tempFolder + File.separator + "FreeDiskSpace.txt",
                                    JGIWindows.getFreeDiskSpace(cogPath));
                        }
                        if (OSSpecific.getOSType().equals("Linux")) {
                            StringToFile.write(tempFolder + File.separator + "RunningProcs.txt", JGILinux.getOpenFiles());
                            StringToFile.write(tempFolder + File.separator + "ldd.txt", JGILinux.getLddStatus(cogPath));
                        }
                        //TODO: Checking Logs folder size and warn if to big
                        Long logsSize = folderSize(cogPath + File.separator + "logs");
                        System.out.println("DEBUG: Logs Folder size as long: " + logsSize);
                        System.out.println("Logs Folder size: " + readableFileSize(logsSize));
                        if (bigLogs) {
                            if (logsSize > 2048000000) {
                                System.out.println("Logs bigger then 2 GB. Override with -biglogs");
                                cleanup();
                                System.exit(1);
                            }
                        }

                        String[] cogLogs = FileHelper.getFilesFromFolder(cogPath + File.separator + "logs");
                        for (String s : cogLogs) {
                            if (!new File(s).toString().contains(".lck")) {
                                System.out.println("Collecting " + new File(s).getAbsolutePath());
                                ZipCreator.addToZipFile(s, zos);
                            }
                        }
                        String[] xqe = FileHelper
                                .getFilesFromFolder(cogPath + File.separator + "configuration" + File.separator + "xqe");
                        for (String s : xqe) {
                            System.out.println("Collecting " + new File(s).getAbsolutePath());
                            ZipCreator.addToZipFile(s, zos);
                        }

                        File[] bootstrapList = new File(cogPath + File.separator + "bin64").listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                if (name.contains("bootstrap") && name.contains(".xml")) {
                                    return true;
                                }
                                return false;
                            }
                        });
                        for (File f : bootstrapList) {
                            if (f.exists()) {
                                System.out.println("Collecting " + f.getAbsolutePath());
                                ZipCreator.addToZipFile(f.getAbsolutePath(), zos);
                            }
                        }

                        String[] tm1Srv = TM1Info.getTM1Server(
                                cogPath + File.separator + "configuration" + File.separator + "cogstartup.xml", cogPath);
                        String[] cogFiles = new String[]{cogPath + File.separator + "cmplst.txt",
                            cogPath + File.separator + "configuration" + File.separator + "cogstartup.xml",
                            cogPath + File.separator + "configuration" + File.separator + "ipfclientconfig.xml",
                            cogPath + File.separator + "configuration" + File.separator + "coglocale.xml",
                            cogPath + File.separator + "configuration" + File.separator + "powerplay" + File.separator
                            + "ppstyles.ini",
                            cogPath + File.separator + "configuration" + File.separator + "powerplay" + File.separator
                            + "c10dot2dot232.ini",
                            cogPath + File.separator + "configuration" + File.separator + "powerplay" + File.separator
                            + "c8dot4.ini",
                            cogPath + File.separator + "configuration" + File.separator + "ppds_cfg.xml",
                            cogPath + File.separator + "configuration" + File.separator + "cogconfig.prefs",
                            cogPath + File.separator + "configuration" + File.separator + "cogconfig_reg.txt",
                            cogPath + File.separator + "configuration" + File.separator + "smtpRules-default.xml",
                            cogPath + File.separator + "templates" + File.separator + "ps" + File.separator
                            + "system.xml",
                            cogPath + File.separator + "templates" + File.separator + "ps" + File.separator + "portal"
                            + File.separator + "system.xml",
                            cogPath + File.separator + "webapps" + File.separator + "p2pd" + File.separator + "WEB-INF"
                            + File.separator + "classes" + File.separator + "cogroot.link",
                            cogPath + File.separator + "war" + File.separator + "gateway" + File.separator + "classes"
                            + File.separator + "cogroot.link",
                            cogPath + File.separator + "bin64" + File.separator + "cogconfig." + scriptExt,
                            cogPath + File.separator + "bin64" + File.separator + "startup." + scriptExt,
                            cogPath + File.separator + "bin64" + File.separator + "startwlp." + scriptExt,
                            cogPath + File.separator + "bin64" + File.separator + "startup_tomcat." + scriptExt,
                            cogPath + File.separator + "ids" + File.separator + "cm_ids_svr_custom.log",
                            cogPath + File.separator + "ids" + File.separator + "concm_ids_svr_custom.log",
                            cogPath + File.separator + "informix" + File.separator + "ol_cognoscm.log",
                            cogPath + File.separator + "tomcat" + File.separator + "conf" + File.separator
                            + "server.xml",
                            cogPath + File.separator + "wlp" + File.separator + "usr" + File.separator + "servers"
                            + File.separator + "cognosserver" + File.separator + "server.xml",
                            cogPath + File.separator + "wlp" + File.separator + "usr" + File.separator + "servers"
                            + File.separator + "cognosserver" + File.separator + "jvm.options",
                            cogPath + File.separator + "wlp" + File.separator + "usr" + File.separator + "servers"
                            + File.separator + "servletgateway" + File.separator + "server.xml",
                            cogPath + File.separator + "wlp" + File.separator + "usr" + File.separator + "servers"
                            + File.separator + "servletgateway" + File.separator + "jvm.options",
                            cogPath + File.separator + "webapps" + File.separator + "p2pd" + File.separator + "WEB-INF"
                            + File.separator + "services" + File.separator + "powerplayservice.xml",
                            cogPath + File.separator + "webapps" + File.separator + "p2pd" + File.separator + "WEB-INF"
                            + File.separator + "services" + File.separator + "powerplayStudio.xml",
                            cogPath + File.separator + "configuration" + File.separator + "xqe.config.xml",
                            cogPath + File.separator + "configuration" + File.separator + "xqe.custom.config.xml",
                            cogPath + File.separator + "configuration" + File.separator + "xqeerp.config.xml",
                            cogPath + File.separator + "configuration" + File.separator + "xqeerp.custom.config.xml",
                            cogPath + File.separator + "configuration" + File.separator + "xqe.diagnosticlogging.xml",
                            cogPath + File.separator + "webcontent" + File.separator + "web.config",
                            cogPath + File.separator + "cgi-bin" + File.separator + "web.config",
                            cogPath + File.separator + "webcontent" + File.separator + "bi" + File.separator + "web.config",
                            cogPath + File.separator + "temp" + File.separator + "cbs.prefs", cogPath + File.separator
                            + "v5dataserver" + File.separator + "databaseDriverLocations.properties"

                        };

                        for (String s : cogFiles) {
                            if (new File(s).exists()) {
                                System.out.println("Collecting " + new File(s).getAbsolutePath());
                                ZipCreator.addToZipFile(s, zos);
                            }
                        }
                        for (String s : tm1Srv) {
                            if (new File(s).exists()) {
                                System.out.println("Collecting " + new File(s).getAbsolutePath());
                                ZipCreator.addToZipFile(s, zos);
                            }
                        }
                    }
                    String jdbc = getJDBCVersion(cogPath + File.separator + "webapps" + File.separator + "p2pd"
                            + File.separator + "WEB-INF" + File.separator + "lib");
                    jdbc += getJDBCVersion(cogPath + File.separator + "v5dataserver" + File.separator + "lib");
                    jdbc += getJDBCVersion(cogPath + File.separator + "drivers");
                    StringToFile.write(tempFolder + File.separator + "jdbc.txt", jdbc);
                } else {
                    System.out.println("Cognos installation not found!");
                    System.out.println("Output file will just contain system information.");
                }

                for (String s : FileHelper.getFilesFromFolder(tempFolder)) {
                    System.out.println("Collecting " + new File(s).getAbsolutePath());
                    ZipCreator.addToZipFile(s, zos);
                }
                if (new File("JGetInfo.log").exists()) {
                    System.out.println("Collecting " + new File("JGetInfo.log").getAbsolutePath());
                    ZipCreator.addToZipFile(new File("JGetInfo.log").getAbsolutePath(), zos);
                }
                zos.close();
                fos.close();
                File fullZipPath = new File(zipFile);
                System.out.println();
                System.out.println("Output file: " + fullZipPath.getAbsolutePath());
            } catch (FileNotFoundException e) {
                Logger.getLogger(JGetInfo.class.getName()).log(Level.SEVERE, null, e);
            } catch (IOException e) {
                Logger.getLogger(JGetInfo.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        cleanup();
    }

    private void cleanup() {
        if (deleteTempFiles) {
            for (String s : FileHelper.getFilesFromFolder(tempFolder)) {
                FileHelper.delete(s);
            }
            FileHelper.delete(tempFolder);
            if (new File("JGetInfo.log").exists()) {
                File f = new File("JGetInfo.log");
                f.deleteOnExit();
            }
            if (new File("JGetInfo.log.lck").exists()) {
                File f = new File("JGetInfo.log.lck");
                f.deleteOnExit();
            }
            if (new File("JGetInfo.log.1").exists()) {
                File f = new File("JGetInfo.log.1");
                f.deleteOnExit();
            }
            if (new File("JGetInfo.log.1.lck").exists()) {
                File f = new File("JGetInfo.log.1.lck");
                f.deleteOnExit();
            }
        }
    }

    public static String getJDBCVersion(String path) {
        String[] jdbcFiles = new String[]{"ojdbc14.jar", "ojdbc5.jar", "ojdbc6.jar", "ojdbc7.jar", "db2jcc4.jar",
            "db2jcc.jar"};
        String jdbc = "";
        String[] jarF = FileHelper.getFilesFromFolder(path);
        for (String s : jarF) {
            jdbc += s + System.getProperty("line.separator");
        }
        for (String s : jdbcFiles) {
            String jdbcDriver = path + File.separator + s;
            System.out.println("Checking JDBC: " + jdbcDriver);
            if (new File(jdbcDriver).exists()) {
                System.out.println("JDBC Driver found: " + new File(jdbcDriver).getAbsolutePath());
                jdbc += new File(jdbcDriver).getAbsolutePath() + System.getProperty("line.separator");
                if (new File(jdbcDriver).getName().equals("db2jcc.jar")) {
                    jdbc += SystemCall.runJavaCMD(
                            "-cp \"" + new File(jdbcDriver).getAbsolutePath() + "\" com.ibm.db2.jcc.DB2Jcc -version");
                }
                if (new File(jdbcDriver).getName().equals("db2jcc4.jar")) {
                    jdbc += SystemCall.runJavaCMD(
                            "-cp \"" + new File(jdbcDriver).getAbsolutePath() + "\" com.ibm.db2.jcc.DB2Jcc -version");
                }
                if (new File(jdbcDriver).getName().equals("ojdbc4.jar")) {
                    jdbc += SystemCall
                            .runJavaCMD("-jar \"" + new File(jdbcDriver).getAbsolutePath() + "\" -getversion");
                }
                if (new File(jdbcDriver).getName().equals("ojdbc5.jar")) {
                    jdbc += SystemCall
                            .runJavaCMD("-jar \"" + new File(jdbcDriver).getAbsolutePath() + "\" -getversion");
                }
                if (new File(jdbcDriver).getName().equals("ojdbc6.jar")) {
                    jdbc += SystemCall
                            .runJavaCMD("-jar \"" + new File(jdbcDriver).getAbsolutePath() + "\" -getversion");
                }
                if (new File(jdbcDriver).getName().equals("ojdbc7.jar")) {
                    jdbc += SystemCall
                            .runJavaCMD("-jar \"" + new File(jdbcDriver).getAbsolutePath() + "\" -getversion");
                }
            }
        }
        return jdbc + System.getProperty("line.separator") + System.getProperty("line.separator");
    }

    public static long folderSize(String directory) {
        long length = 0;
        File dir = new File(directory);
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                length += file.length();
            } else {
                length += folderSize(file.getAbsolutePath());
            }
        }
        return length;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    
    private void printHelp() {
        System.out.println("------------------------------------------------------------------------");
        System.out.println("JGetInfo " + JGetInfo.VERSION + " by Andreas Weber <webera64720@googlemail.com");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("java [Java arguments] -jar JGetInfo.jar [options]");
        System.out.println("");
        System.out.println("Java arguments:");
        System.out.println("");
        System.out.println("     -Xmx8192m       Using 8GB Java Heap (You can use your own value)");
        System.out.println("");
        System.out.println("Options:");
        System.out.println("     -help           This will display the Help");
        System.out.println("     -version        Displays the JGetInfo version");
        System.out.println("     -iisonly        Will only collect the IIS configuration (Windows only)");
        System.out.println("     -nologs         Will not include the IBM Cognos Logs");
        System.out.println("     -biglogs        Collect IBM Cognos Logs, even if the content of <cognos>/logs surpasses 2GB");
        System.out.println("     -log            Create JGetInfo Log");
        System.out.println("------------------------------------------------------------------------");
        System.out.println("");
    }

    public static void main(String[] args) {
        new JGetInfo(args);
    }
}
