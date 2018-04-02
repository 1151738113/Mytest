package com.myproject.function;

import java.util.Arrays;

/**
 * Created by wei.wang on 2018/3/23.
 * 堆排序
 */
public class HeapSort {

  public static void sort(int[] arr){
    //构建大顶堆
   for(int i = arr.length/2-1; i>=0;i--){
     addjustHead(arr,i,arr.length);
   }
    //2.调整堆结构+交换堆顶元素与末尾元素
    for(int j=arr.length-1;j>0;j--){
      swap(arr,0,j);//将堆顶元素与末尾元素进行交换
      addjustHead(arr,0,j);//重新对堆进行调整
    }
  }


  /**
   * 调整大顶堆
   */
  public static void addjustHead(int[] arr, int i,int length){
    int temp = arr[i]; //去除元素
    for(int k=i*2+1;k<length;k = k*2+1){   //从i节点的左子节点开始，也就是2i+1处开始
        if(k+1<length && arr[k]<arr[k+1]){   //如果左子节点小于右子节点，k指向右子节点
            k++;
        }
        if(arr[k]>temp){   //如果子节点大于父节点，将子节点值赋给父节点（不用进行交换）
          arr[i] = arr[k];
          i=k;
        }else{
          break;
        }
    }
    arr[i] = temp;    //将temp值放到最终的位置
  }

  /**
   * 数据交换
   * @param arr
   * @param a
   * @param b
   */
  private static void swap(int[] arr, int a, int b){
    int temp = arr[a];
    arr[a] = arr[b];
    arr[b] = temp;
  }


  public static void main(String[] args){
    int []arr = {9,8,7,6,5,4,3,2,1};
    sort(arr);
    System.out.println(Arrays.toString(arr));
  }

}
