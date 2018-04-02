package com.myproject.function;

import java.util.Scanner;

/**
 * Created by wei.wang on 2018/3/8.
 */
public class Dongtaiguihua {

  int min(int a,int b,int c){
    int t = a<b? a:b;
    return t<c?t:c;
  }

  private void editDistance(char[] a, char[] b){
    int la = a.length;
    int lb = b.length;
    int d[][] = new int[la+1][lb+1];
    int i,j;
    //横轴
    for(i=0;i<=la;i++){
      d[i][0] = i;
    }
    //纵轴
    for(j=0;j<=lb;j++){
      d[0][j]=j;
    }
    for(i=1;i<=la;i++){
      for(j=1;j<=lb;j++){
        int cost = a[i-1]==b[j-1]?0:1;
        int deletion = d[i-1][j]+1;
        int insertion = d[i][j-1]+1;
        int substitudion = d[i-1][j-1]+cost;
        d[i][j] = min(deletion,insertion,substitudion);
      }
    }
    System.out.println(d[la][lb]);
  }

  public static void main(String[] args){
    Dongtaiguihua ed=new Dongtaiguihua();
    Scanner in=new Scanner(System.in);
    String a=in.nextLine();
    String b=in.nextLine();
    in.close();
    ed.editDistance(a.toCharArray(), b.toCharArray());
  }

}
