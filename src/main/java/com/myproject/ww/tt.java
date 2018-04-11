package com.myproject.ww;

/**
 * Created by wei.wang on 2018/4/9.
 */
public class tt {

  public static void main(String[] args){
    String content = "犯罪嫌疑人费1发以非法占有为目的,采用秘密手段,盗窃方式他人手机,价格鉴定约为人民币4900元,数额较大,其行为触犯了《中华人民共和国刑法》第二百六十四条,涉嫌盗窃罪。鉴于费1发有盗窃罪前科,故而有逮捕必要。\n"
        + "综上所述,根据《中华人民共和国刑事诉讼法》第八十八条之规定,建议批准逮捕犯罪嫌疑人费1发。";
    System.out.println(content.length());
  StringBuilder ss = new StringBuilder(content);
  ss.insert(154,"/em");
  String aa = "《中华人民共和国刑事诉讼法》第八十八条之规定,建议批准逮捕犯罪嫌疑人费1发。";
  System.out.println(ss);
  }

}
