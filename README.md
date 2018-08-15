
This Utility is designed to collect system information from Windows, Linux, AIX, HPUX and Solaris and pack it into a file called jgetinfo.zip. The Tool will also collect information from a IBM Cognos Analytics Installation. 

# Usage:

java [Java arguments] -jar JGetInfo.jar [options]

Java arguments:

     -Xmx8192m       Using 8GB Java Heap (You can use your own value)

Options:

     -help           This will display the Help
     -version        Displays the JGetInfo version
     -iisonly        Will only collect the IIS configuration (Windows only)
     -nologs         Will not include the IBM Cognos Logs
     -biglogs        Collect IBM Cognos Logs, even if the content of <cognos>/logs surpasses 2GB
     -log            Create JGetInfo Log

# Download:

https://github.com/webera64720/JGetInfo/blob/master/binary/JGetInfo4.jar
