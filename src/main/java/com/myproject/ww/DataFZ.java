package com.myproject.ww;

import com.myproject.util.ClientHelper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHits;

import java.io.*;

/**
 * Created by wei.wang on 2018/4/11.
 */
public class DataFZ {

  static Client client = null;
      static{
        client =  ClientHelper.getInstance().getClient();

      }
  public static void main(String[] args){
        String query = "";

    SearchResponse response = client.prepareSearch("es_fdlawcase")
        .setQuery(query).setSize(1000).setScroll(new TimeValue(600000))
        .setSearchType(SearchType.SCAN).execute().actionGet();
    String scrollid = response.getScrollId();
    try{
      File file = new File("C:\\tool\\es_fdlawcase2.json");
      FileOutputStream fos = new FileOutputStream(file,true);
      OutputStreamWriter osWritter = new OutputStreamWriter(fos, "UTF-8");//设置字符编码
      //把导出的结果以JSON的格式写到文件里
      BufferedWriter out = new BufferedWriter(osWritter, 1024);
      //每次返回数据10000条。一直循环查询直到所有的数据都查询出来
      while (true) {
        SearchResponse response2 = client.prepareSearchScroll(scrollid).setScroll(new TimeValue(1000000))
            .execute().actionGet();
        SearchHits searchHit = response2.getHits();
        //再次查询不到数据时跳出循环
        if (searchHit.getHits().length == 0) {
          break;
        }
        System.out.println("查询数量 ：" + searchHit.getHits().length);
        for (int i = 0; i < searchHit.getHits().length; i++) {
          String type = searchHit.getHits()[i].getType();
          String id = searchHit.getHits()[i].getId();
          String json = searchHit.getHits()[i].getSourceAsString();
          json = "{"+"\"type\":\""+type+"\",\"_source\":"+json+",\"id\":\""+id+"\"}";
          out.write(json);
          out.write("\n");
          System.out.println("第 "+i+" 次打印");
        }
      }
      System.out.println("查询结束");
      out.close();
    }catch(Exception e){
      System.out.println(e.getMessage());
    }
  }



}
