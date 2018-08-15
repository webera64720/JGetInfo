package com.itweber.jgetinfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TM1Info {

    public static String[] getTM1Server(String cogstartup, String cogPath) {
        String cfg = FileHelper.readFile(cogstartup);
        String[] cfgArr = cfg.split("\n");
        List<String> tmp = new ArrayList<String>();
        for (String s : cfgArr) {
            if (s.contains("cfg:folderPath")) {
                try {
                    String path = s.replace("<crn:value xsi:type=\"cfg:folderPath\">", "").replace("</crn:value>", "").trim();
                    if (path.startsWith("..")) {
                        path = path.replace("..", cogPath);
                    }
                    if (new File(path + File.separator + "tm1s.cfg").exists()) {
                        tmp.add(path + File.separator + "tm1s.cfg");
                    }
                    if (new File(path + File.separator + "tm1s-log.properties").exists()) {
                        tmp.add(path + File.separator + "tm1s-log.properties");
                    }
                    if (new File(path + File.separator + "tm1server.log").exists()) {
                        tmp.add(path + File.separator + "tm1server.log");
                    }
                } catch (Exception e) {
                }
            }
        }
        String[] ret = new String[tmp.size()];
        for (int i = 0; i < tmp.size(); i++) {
            ret[i] = tmp.get(i);
        }
        return ret;
    }
}
