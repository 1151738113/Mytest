import org.junit.Test;

import java.util.Random;

/**
 * Created by wei.wang on 2017/12/4.
 */
public class Term_ww {
  @Test
  public void ww(){
    Random random = new Random(100);
    for(int i = 0;i<20;i++) {
      System.out.println(random.nextDouble());
    }
  }

}
