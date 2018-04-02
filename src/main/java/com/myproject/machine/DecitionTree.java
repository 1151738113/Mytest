package com.myproject.machine;

/**
 * Created by wei.wang on 2017/11/24.
 */
public class DecitionTree {

  public static double ww(String[] rows){
    float total = rows.length;
    Integer[] uniqueRows =null;
    double ent = 0.0;
    for(int i=0;i<uniqueRows.length;i++){
      float p = uniqueRows[i]/total;
      ent = ent - p* (Math.log(p)/Math.log(2));
    }
return ent;
  }
}
