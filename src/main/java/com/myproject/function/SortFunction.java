package com.myproject.function;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wei.wang on 2018/3/23.
 */
public class SortFunction {

  /**
   * 插入排序
   */
  private static  <T extends Comparable<? super T>> void insertSort(T[] a) {
    for (int p = 1; p < a.length; p++) {
      T tmp = a[p];//保存当前位置p的元素，其中[0,p-1]已经有序
      int j;
      for (j = p; j > 0 && tmp.compareTo(a[j - 1]) < 0; j--) {
        a[j] = a[j - 1];//后移一位
      }
      a[j] = tmp;//插入到合适的位置
    }
  }
//  public static int[] insert_sort(int[] arry) {
//    //获取数组长度
//    int len = arry.length;
//    int preIndex, current;
//    for (int i = 1; i < len; i++) {
//      preIndex = i - 1;
//      current = arry[i];
//      while (preIndex >= 0 && arry[preIndex] > current) {
//        arry[i + 1] = arry[preIndex];
//        preIndex--;
//      }
//      arry[preIndex + 1] = current;
//    }
//    return arry;
//  }



  /**
   *希尔排序
   */
  private static  void shellSort(int[] arr){
    int d = arr.length;
    while (true){
      d= d /2; //每次将gap减半
      for(int x = 0; x<d;x++){
        for(int i = x+d; i< arr.length;i=i+d){
          int temp = arr[i];
          int j;
          for(j=i-d;j>=0&&arr[j]>temp;j=j-d){
            arr[j+d] = arr[j];
          }
          arr[j+d] = temp;
        }
      }
      if(d==1){
        break;
      }
    }
  }



  public static void main(String[] args) {
    Integer[] a = {4, 1, 6, 2, 8};
    int[] a1 = {4, 1, 6, 2, 8};
//    int[] b = insert_sort(a);
//    insertSort(a);
    shellSort(a1);
    for (Integer anA : a1) {
      System.out.println(anA);
    }
  }
}
