package com.myproject.ww;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wei.wang on 2018/3/2.
 */
public class Test4 {

  public static void main(String[] args) throws Exception{
    FileWriter writer=new FileWriter("C:/wwproject/法院名称_new.txt");
//    String path = "[\\u4e00-\\u9fae,，:：（(）)Xx×]*?所$";
//    Pattern pattern = Pattern.compile(path);
    String path1 = "[\\u4e00-\\u9fae]*?人民法院";
    Pattern pattern1 = Pattern.compile(path1);
    Matcher matcher;
    InputStream in = Test4.class.getClassLoader().getResourceAsStream("data/法院名称.txt");
    BufferedReader br;
    InputStreamReader reader = null;
    try {
      reader = new InputStreamReader(in,"UTF-8");
    } catch (UnsupportedEncodingException e) {

    }
    br = new BufferedReader(reader);
    try {
      for(String line = br.readLine();
          line != null; line = br.readLine()){
        if(line.startsWith("#")){
          continue;
        }
//        if(line.length()<35){
//          writer.write(line + "\n");
//        }
//        matcher = pattern.matcher(line);
//        if(matcher.find()){
//          if(line.length()==matcher.group().length()) {
//            writer.write(line + "\n");
//          }
//        }

        matcher = pattern1.matcher(line);
                if(matcher.find()){
                  if(line.length()==matcher.group().length()) {
                   if(line.contains("某") || line.contains("不服") || line.contains("签发") || line.contains("凝固剂") ||
                       line.contains("拟") || line.contains("|") || line.contains("书")
                       || line.contains("巨野") || line.contains("日在") || line.contains("意见") ||
                       line.contains("行为")){
                     continue;
                   }
                   if(line.startsWith("宋体") || line.startsWith("黑体")  || line.startsWith("的")
                       || line.startsWith("梁") || line.startsWith("对") || line.startsWith("都")
                       || line.startsWith("了") || line.startsWith("年月日") || line.startsWith("第")
                       || line.startsWith("抄") || line.startsWith("数") || line.startsWith("市")){
                     continue;
                   }
                   if (line.length()>35 || line.length()<7){
                     continue;
                   }
                    writer.write(line + "\n");
                  }
                }
      }
    } catch (IOException e) {
    }
    writer.close();

  }

}
