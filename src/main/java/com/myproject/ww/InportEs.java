package com.myproject.ww;

import com.myproject.util.ClientHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wei.wang on 2018/3/6.
 */
public class InportEs {

  public static void main(String[] args) throws Exception{
    Client client = ClientHelper.getInstance().getClient();
    BulkProcessor bulkProcessor  = BulkProcessor.builder(client, new BulkProcessor.Listener() {
      public void afterBulk(long arg0, BulkRequest request, BulkResponse arg2) {
//        ImportES.t2 = System.currentTimeMillis();
//        PrintStream var10000 = System.out;
//        StringBuilder var10001 = (new StringBuilder("已读取:")).append(ImportES.read).append("，读文件耗时").append(ImportES.r2 - ImportES.r1).append("ms，已导入:");
//        int var10002 = ImportES.write + request.numberOfActions();
//        ImportES.write = var10002;
//        var10000.println(var10001.append(var10002).append("，写入ES耗时").append(ImportES.t2 - ImportES.t1).append("ms").toString());
//        ImportES.r1 = System.currentTimeMillis();
      }

      public void afterBulk(long arg0, BulkRequest request, Throwable arg2) {
        System.out.println("导入失败:" + arg2.getMessage());
      }

      public void beforeBulk(long arg0, BulkRequest request) {
//        ImportES.t1 = System.currentTimeMillis();
//        ImportES.r2 = System.currentTimeMillis();
      }
    }).setBulkActions(100).setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.GB)).setFlushInterval(
        TimeValue.timeValueSeconds(3L)).build();


    InputStream in = InportEs.class.getClassLoader().getResourceAsStream("data/result.txt");
    InputStreamReader reader = new InputStreamReader(in,"UTF-8");
    BufferedReader br = new BufferedReader(reader);
    String line;
    while ((line = br.readLine()) != null) {
      String str = "案由" + line;
      String ss= DigestUtils.md5Hex(str);
      System.out.println(line+"----------------");
      Map<String, Object> json = new HashMap<String, Object>();
       json.put("searchtype","案由");
       json.put("searchcontent",line);
       json.put("searchweight",0);
       json.put("caseId",ss);
      bulkProcessor.add((new IndexRequest("searchtype_20180312", "linksearch", ss)).source(json));
//     client.prepareDelete("searchtype_20180312","linksearch",ss).execute().actionGet();
    }
    br.close();
    reader.close();
    in.close();
    bulkProcessor.close();
  }

}
