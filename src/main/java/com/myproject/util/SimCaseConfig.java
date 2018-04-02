package com.myproject.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by gengw on 11/1/16.
 * 相似案例配置文件
 */

public class SimCaseConfig {
    public static String CLUSTER_NAME = "future-data";
    public static String CLUSTER_NAME1 = "future-data";
    public static Map<String, Integer> IP = new HashMap<String, Integer>();
    public static String IndexName = "es_fdlawcase";
    public static String LXIndexName = "es_lxfdlawcase";
    public static String GeanIndexName = "es_geanfdlawcase";
    public static String PPOIndexName = "es_ppolawcase";
    public static int PORT = 9300;

    public static float Min_Score = 0.2f;
    public static String configfile = "data/config";

    static {



        Properties prop = new Properties();
        try {
            //        FileInputStream in = new FileInputStream("data/config");
            InputStream in = SimCaseConfig.class.getClassLoader().getResourceAsStream(configfile);
            prop.load(in);
            CLUSTER_NAME = prop.getProperty("CLUSTER_NAME");
            String[] IPList = prop.getProperty("IP").split(",");
            PORT = Integer.parseInt(prop.getProperty("PORT"));
            for (String ip : IPList)
                IP.put(ip, PORT);
            IndexName = prop.getProperty("LawIndex");
            LXIndexName = prop.getProperty("LXLawIndex");
            GeanIndexName = prop.getProperty("GeanIndexName");
            Min_Score = Float.parseFloat(prop.getProperty("Min_Score"));
            in.close();
        } catch (Exception e) {

        }
    }
}
