package com.myproject.ww;

import com.myproject.tools.CustomQueryBuilder;
import com.myproject.util.ClientHelper;
import com.myproject.util.FilterBDU;
import com.myproject.util.FilterType;
import com.myproject.util.QueryType;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wei.wang on 2018/3/2.
 */
public class OutputES {

  public static void main(String[] args) throws Exception{
    FileWriter writer=new FileWriter("C:/wwproject/律所.txt");
    Client client = ClientHelper.getInstance().getClient();
    Map<String,FilterBDU> map = new HashMap<String, FilterBDU>();
    map.put("",new FilterBDU( FilterType.Must,QueryType.Term,"searchtype","律所"));
    QueryBuilder builder = CustomQueryBuilder.boolQuery1(map);
    SearchRequestBuilder searchRequestBuilder1 = new SearchRequestBuilder(client, SearchAction.INSTANCE);
    searchRequestBuilder1.setQuery((builder))
        .setSearchType(SearchType.QUERY_THEN_FETCH)
        .setIndices("es_searchtype_1103")
        .setFrom(0)
        .setSize(20000);
    searchRequestBuilder1.execute().actionGet();
    SearchResponse response1 = searchRequestBuilder1.get();
    SearchHit[] searchHitsByPrepareSearch = response1.getHits().hits();
    for(SearchHit hitFields : searchHitsByPrepareSearch){
      Map<String, Object> obj = hitFields.getSource();
      Object name  = obj.get("searchcontent");
      Object caseId = obj.get("caseId");
      System.out.println(name.toString()+"  "+caseId.toString());
      writer.write(name.toString()+"\t");
    }
    writer.close();
  }

}
