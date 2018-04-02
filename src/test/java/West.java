import java.util.Scanner;

/**
 * Created by wei.wang on 2017/12/19.
 */
public class West {

  public static void main(String[] args){
//    West west = new West();
//    west.ww();
    System.out.println("输入：");
    Scanner scan = new Scanner(System.in);
    if(scan.next().equals("1")){
      System.out.println("输入错误请重新输入");
    }else if(scan.next().equals("2")){
      System.out.println("输入正确");
    }
  }

  public void ww(){
    System.out.println("输入：");
    Scanner scan = new Scanner(System.in);
    if(scan.equals(1)){
      System.out.println("输入错误请重新输入");
      ww();
    }else if(scan.equals(2)){
      System.out.println("输入正确");
    }
  }

}
