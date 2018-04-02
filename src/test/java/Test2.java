import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${ww} on ${DATA}.
 */
public class Test2 {
  @Test
  public void ww(){
//    List<String> list1 = new ArrayList<String>();
//    list1.add("aa");
//    list1.add("bb");
//    System.out.println(list1.toString());
//    String cc = list1.toString().trim();
//    String result = cc.substring(1,cc.length()-1);
//    System.out.println(result);
//    String[] cp = result.split(",");
//for(String ss : cp) {
//  System.out.println(ss.trim());
//}

String str = "[0~3万)元";
    String unit = str.replaceAll("[0-9]", "");
System.out.println(unit);

    if ("万".equals(unit)) {
      String amount = str.replaceAll("[^0-9]", "");
      double ss =  NumberUtils.toDouble(amount) * 10000;
      System.out.println(ss);
    }
System.out.println(str);

  }

}
