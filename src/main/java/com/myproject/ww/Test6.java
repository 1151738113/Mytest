package com.myproject.ww;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wei.wang on 2018/3/2.
 */
public class Test6 {

  public static void main(String[] args) throws Exception{
    FileWriter writer=new FileWriter("C:/wwproject/法律法规_new.txt");
    InputStream in = Test6.class.getClassLoader().getResourceAsStream("data/法律条款.txt");
    String phyt = "《[\\u4e00-\\u9fae]*?(则|规定|条例|(?<!办)法)》";
    Pattern pattern = Pattern.compile(phyt);
    Matcher matcher;
    BufferedReader br;
    InputStreamReader reader = null;
    try {
      reader = new InputStreamReader(in,"UTF-8");
    } catch (UnsupportedEncodingException e) {

    }
    br = new BufferedReader(reader);
    try{
      for(String line = br.readLine();
          line != null; line = br.readLine()) {
        if (line.startsWith("#")) {
          continue;
        }
//        if(line.endsWith("司") || line.endsWith("行") || line.endsWith("局") || line.endsWith("会") || line.endsWith("院")
//            || line.endsWith("部") || line.endsWith("厅") || line.endsWith("店") || line.endsWith("处") || line.endsWith("中心")
//            ){
//          writer.write(line+"\n");
//        }else{
//          List<Term> list = HanLP.segment(line);
//          if(list.size() == 1){
//            if(list.get(0).nature.name().equals("nr")){
//              writer.write(line+"\n");
//            }
//          }
//        }
        matcher = pattern.matcher(line);
        if(matcher.find()){
          writer.write(line+"\n");
        }
      }
    }catch(Exception ex){

    }
    writer.close();
  }

}
