package com.myproject.tools;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${ww} on ${DATA}.
 * 个案预警数据导入工具
 */
public class EsImport {
  private static List<Map<String,String>> list = new ArrayList<Map<String, String>>();

  public static void main(String[] args) throws Exception {
    insertData();
  }


  private static void insertData() throws Exception {
    InputStream in = EsImport.class.getClassLoader().getResourceAsStream("data/data1");
    BufferedReader br;
    InputStreamReader reader = null;
    try {
      reader = new InputStreamReader(in,"UTF-8");
    } catch (UnsupportedEncodingException e) {
      System.out.println(e.getMessage());
    }
    br = new BufferedReader(reader);
    try {
      for(String line = br.readLine();
          line != null; line = br.readLine()){
        Map<String, String> propertiesMap = new HashMap<String, String>();
        String[] str1 = line.trim().split("\t");
        propertiesMap.put("caseNo",str1[0]);
        propertiesMap.put("facts",str1[1]);
        propertiesMap.put("judge",str1[2]);
        propertiesMap.put("anomaly",str1[3]);
        propertiesMap.put("abn_pos",str1[4]);
        list.add(propertiesMap);
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

    TransportClient client = getClient();
    for(Map<String, String> map : list){
     SearchResponse response = client.prepareSearch("es_fdlawcase")
          .setSearchType(SearchType.QUERY_THEN_FETCH)
          .setQuery(QueryBuilders.termQuery("meta_案号", map.get("caseNo")))
          .execute()
          .actionGet();
      if (response.getHits().getTotalHits() > 0) {
        SearchHit hit= response.getHits().getAt(0);
        UpdateRequest updateRequest = new UpdateRequest("es_fdlawcase",hit.getType(),hit.getId())
            .doc("abnormal","true","abn_情节",map.get("facts"),"abn_判决",map.get("judge"),"abn_原因",map.get("anomaly"),"abn_pos",map.get("abn_pos"));
//            .doc("abn_情节",map.get("facts"))
//            .doc("abn_判决",map.get("judge"))
//            .doc("abn_原因",map.get("anomaly"));
            client.update(updateRequest).get();
      }
    }
    client.close();
  }

  private static TransportClient getClient() throws Exception{
    Settings settings = Settings.settingsBuilder()
        .put("cluster.name", "fd-data")
        .put("client.transport.sniff", true)
        .build();
    TransportClient client = TransportClient.builder().settings(settings).build()
        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.220"), 9300))
        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.221"), 9300))
        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.222"), 9300))
//        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("139.2.14.100"), 9300))
//        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("139.2.14.101"), 9300))
//        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("139.2.14.102"), 9300))
        ;
     return client;
  }

}
