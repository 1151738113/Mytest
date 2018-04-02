package com.myproject.function;

/**
 * Created by wei.wang on 2018/3/12.
 */
public class Test2 {
  private Test1 test1;
//  public Test2(Test1 t1){
//    this.test1 = t1;
//  }
  public void setMain(Test1 t1){
    this.test1 = t1;
  }

  public void say(){
    System.out.println("dasdadasd"+test1.say());
  }

  public static void main(String[] args){
    Test1 t1 = new Test1();
    Test2 t2 = new Test2();
    t2.setMain(t1);
    t2.say();
  }
}
