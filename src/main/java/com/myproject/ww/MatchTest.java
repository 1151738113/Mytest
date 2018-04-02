package com.myproject.ww;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wei.wang on 2017/11/6.
 */
public class MatchTest {
  @Test
  public void ww(){

    String regix = "第?[零一二三四五六七八九十百]+[条款项]+";
    String input = "全国人大常委会关于刑法第二百二十八条、第三百四十二条、第四百一十条的解释（2009年修正）";
    Pattern pat = Pattern.compile(regix);
    Matcher mat = pat.matcher(input);
    if(mat.find()){
      String group = mat.group();
      if(group.contains("、")){
        System.out.println("不符合结果");
      }
      int head1 = input.indexOf(group);
      String gg = input.substring(0,head1);
      System.out.println("符合："+gg
      );
    }

  }

}
