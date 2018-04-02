package com.myproject.util;


import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gengw on 10/17/16.
 */

public class Utils {

    /*
     *信息点统计条件map 集合
     * string表示案由
     * Set<String[]>表示该案由下的所有
     */
    private  static Map<String,Set<String[]>> statsMap=new HashMap<String, Set<String[]>>();

    public static void main(String[] args)  throws IOException {
        Map<String,String> maplist = GetSysnomns("data/sysnomns");
        System.out.println(maplist);
//        Set<StatsBDU> list = readToMap("data/casestats","交通肇事罪", "盗窃罪");
//        for (StatsBDU s : list) {
//            System.out.println(list.size()+"\t"+s);
//        }
//        System.out.println("\t\n");
//        List<StatsBDU> list2 = readToMap("data/casestats","交通肇事罪");
//        for (StatsBDU s : list2) {
//            System.out.println(s);
//        }
//        System.out.println("\t\n");
////        list.removeAll(list2);
//        list.addAll(list2);
//        Set<StatsBDU> ts = new HashSet<StatsBDU>();
//        ts.addAll(list);
//        for (StatsBDU s : ts) {
//            System.out.println(s);
//        }

    }

    public static Map<String,String> GetSysnomns(String fileName)  throws  IOException {

        Map<String,String> result = new HashMap<String,String>();
        InputStream in = Utils.class.getClassLoader().getResourceAsStream(fileName);
        InputStreamReader read = new InputStreamReader(in,"UTF-8");
        BufferedReader reader = new BufferedReader(read);
        try {
            String data = "";
            while ((data = reader.readLine()) != null) {
                if (!data.startsWith("#")) {

                    String[] list = data.split(",");
                    String key = list[0];
                    for(int i=1;i<list.length;i++) {
                        if (!result.containsKey(key))
                            result.put(list[i],key);
                    }
                }
            }
            reader.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }
    /*
    *对文本内容执行正则表达式
     */
    public static List<String> extractRegexMatchs(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        ArrayList result = new ArrayList();

        while(m.find()) {
            if(!m.group().equals(""))
                result.add(m.group());
        }

        return result;
    }
    public static Set<StatsBDU> readToMap(String fileName) throws  IOException {

        Set<StatsBDU> ts = new HashSet<StatsBDU>();
        InputStream in = Utils.class.getClassLoader().getResourceAsStream(fileName);
        InputStreamReader read = new InputStreamReader(in,"UTF-8");
        BufferedReader reader = new BufferedReader(read);
        try {
            String data = "";
            while ((data = reader.readLine()) != null) {
                if (!data.startsWith("#")) {

                    String[] list = data.split("#");
                    String key = list[2];
                    if (!statsMap.containsKey(key))
                        statsMap.put(list[2], new HashSet<String[]>());
                    statsMap.get(key).add(list);
                    ts.add(GetStatsInfo(list));
                }
            }
            reader.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ts;
    }

//    public static Set<StatsBDU> readToMap(String fileName,String... ay) throws  IOException {
//
//        Set<StatsBDU> ts = new HashSet<StatsBDU>();
//        for(String s : ay) {
//            ts.addAll(readToMap("data/casestats",s));
//        }
//        return ts;
//    }



    //readToMap(new File("data/info_points_weights"));
    //读取信息点权重信息到map集合
    public synchronized static Set<StatsBDU> readToMap(String fileName,String... ay) throws  IOException {
//        InputStream in;
        Set<StatsBDU> ts = new HashSet<StatsBDU>();
        if (statsMap.size() > 0) {
            List<StatsBDU> statsData = new ArrayList<StatsBDU>();
            if (statsMap.containsKey("通用案由")) {
                Set<String[]> statsSet = statsMap.get("通用案由");
                Iterator<String[]> it = statsSet.iterator();
                while (it.hasNext()) {
                    String[] strlist = it.next();
//                    System.out.println(strlist);
                    statsData.add(GetStatsInfo(strlist));
                }
            }
            ts.addAll(statsData);
        }
        for(String s : ay) {
            List<StatsBDU> statsData = new ArrayList<StatsBDU>();
            if (statsMap.size() > 0) {

                if (statsMap.containsKey(s)) {
                    Set<String[]> statsSet = statsMap.get(s);
                    Iterator<String[]> it = statsSet.iterator();
                    while (it.hasNext()) {
                        String[] strlist = it.next();
//                    System.out.println(strlist);
                        statsData.add(GetStatsInfo(strlist));
                    }

                }

            } else {
                InputStream in = Utils.class.getClassLoader().getResourceAsStream(fileName);
                InputStreamReader read = new InputStreamReader(in, "UTF-8");
                BufferedReader reader = new BufferedReader(read);
                try {
                    String data = "";
                    while ((data = reader.readLine()) != null) {
                        if (!data.startsWith("#")) {

                            String[] list = data.split("#");
                            String key = list[2];
                            if (!statsMap.containsKey(key))
                                statsMap.put(list[2], new HashSet<String[]>());
                            statsMap.get(key).add(list);
                            if (list[2].equals("通用案由") || list[2].equals(s)) {
                                statsData.add(GetStatsInfo(list));
                            }
                        }
                    }
                    reader.close();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            ts.addAll(statsData);
        }
        return ts;
    }

    private static void ReadDataFromFile()
    {

    }

    private static StatsBDU GetStatsInfo(String[] list) {
        StatsBDU bdu = null;

        if (list.length > 4) {
            if (list[3].equals("Date"))
                bdu = new StatsBDU(list[0], list[1],StatsType.HDate, list[4]);
            else if (list[3].equals("Range")) {
                if(list[4].indexOf(".")>0)
                {
                    double[] arr = Str2Dbl(list[4]);
                    bdu = new StatsBDU(list[0], list[1], StatsType.Range, arr,TransLabel(list[5]));
                }else {
                    int[] arr = Str2Int(list[4]);
                    bdu = new StatsBDU(list[0], list[1], StatsType.Range, arr,TransLabel(list[5]));
                }
            } else if (list[3].equals("Histogram")) {
//                                int[] arr = Str2Int(list[3]);
                bdu = new StatsBDU(list[0],list[1], StatsType.Histogram, list[4]);
            } else if (list[3].equals("List")) {
//                                int[] arr = Str2Int(list[3]);
                bdu = new StatsBDU(list[0],list[1], StatsType.List, TransLabel(list[4]));
            }else if(list[3].equals("Term")){
              bdu = new StatsBDU(list[0],list[1], StatsType.Term, TransLabel(list[4]));
            }
        } else {
            if (list[3].equals("Percentile")) {
//                                int[] arr = Str2Int(list[3]);
                bdu = new StatsBDU(list[0],list[1], StatsType.Percentile);
            } else if (list[3].equals("StatsDes")) {
//                                int[] arr = Str2Int(list[3]);
                bdu = new StatsBDU(list[0],list[1], StatsType.StatsDes);
            }else if (list[3].equals("Term")){
              bdu = new StatsBDU(list[0],list[1], StatsType.Term);
          } else {
//            System.out.println(list.length);
                bdu = new StatsBDU(list[0], list[1], StatsType.List);
            }
        }

        return bdu;
    }

    private static Map<String,String> TransLabel(String label)
    {
        Map<String,String> tran=new HashMap<String,String>();
        String[] arr = label.split(",");
        for(String ids:arr)
        {
            String[] keyval=ids.split("\\|");
            tran.put(keyval[0],keyval[1]);
        }
        return  tran;
    }


    /*
    *字符串转换为int数组
     */
    private static int[] Str2Int(String str)
    {
        int ret[] = new int[str.split(",").length];
        StringTokenizer toKenizer = new StringTokenizer(str, ",");

        for(int i=0;i<str.split(",").length;i++){

            ret[i] = Integer.valueOf(toKenizer.nextToken());
        }
        return ret;
    }

    /*
    *
    * 字符串转换为double数组
    *
     */
    private static double[] Str2Dbl(String str)
    {
        double ret[] = new double[str.split(",").length];
        StringTokenizer toKenizer = new StringTokenizer(str, ",");

        for(int i=0;i<str.split(",").length;i++){

            ret[i] = Double.valueOf(toKenizer.nextToken());
        }
        return ret;
    }

    public static String readToString(String file) {
        return readToString(new File(file));
    }

    public static String readToString(File file) {


        Long filelength = file.length(); // 获取文件长度
        FileInputStream in = null;
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (Exception e) {
//            logger.error("读取文件失败，文件：" + file.getName(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new String(filecontent);// 返回文件内容,默认编码

    }

    /*

     */
    private static QueryType GetQueryType(String querytype)
    {
        QueryType queryType = QueryType.Term;
        if(querytype.equals("List"))
        {
            queryType =QueryType.List;
        }else if(querytype.equals("Range"))
        {
            queryType =QueryType.Range;
        }else if(querytype.equals("Match"))
        {
            queryType =QueryType.Match;
        }
        else if(querytype.equals("Fuzzy"))
        {
            queryType =QueryType.Fuzzy;
        }
        return queryType;
    }


    private static FilterType GetFilterType(String querytype)
    {
        FilterType queryType = FilterType.Must;
        if(querytype.equals("Probably"))
        {
            queryType =FilterType.Probably;
        }else if(querytype.equals("MustNot"))
        {
            queryType =FilterType.MustNot;
        }
        return queryType;
    }
}
