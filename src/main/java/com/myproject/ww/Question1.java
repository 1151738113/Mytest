package com.myproject.ww;

import com.myproject.tools.CustomQueryBuilder;
import com.myproject.util.*;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${ww} on ${DATA}.
 */
public class Question1 {
public static void main(String[] args){
  Question1 question1 = new Question1();
  try{
    FileWriter writer=new FileWriter("C:/wwproject/公报案例.txt");
    question1.test(writer);
    writer.flush();
    writer.close();
  } catch (IOException e) {
    e.printStackTrace();
  }
}


  public void test(FileWriter write) throws IOException {
  int i = 0;
    Map<String, FilterBDU> map = new HashMap<String, FilterBDU>();
//  map.put("shen",new FilterBDU(FilterType.Must, QueryType.Term, "meta_法院_省", "安徽省"));
//  map.put("shen3",new FilterBDU(FilterType.Must, QueryType.Term, "meta_案由", "盗窃罪"));
    map.put("shen3",new FilterBDU(FilterType.Must, QueryType.Term, "meta_级别", "公报案例"));
    QueryBuilder builder = CustomQueryBuilder.boolQuery1(map);
  Client client = ClientHelper.getInstance().getClient();
  SearchRequestBuilder searchRequestBuilder1 = new SearchRequestBuilder(client, SearchAction.INSTANCE);
  searchRequestBuilder1.setQuery((builder))
      .setSearchType(SearchType.QUERY_THEN_FETCH)
      .setIndices("es_lawcase_1103")
//      .setTypes("盗窃罪")
      .addField("meta_案件名称")

      //                //                .setRequestCache(true)
      //                //                .setFetchSource(includeFields,excludeFields)
      //                /*从第几条结果开始返回，默认为0*/
      .setFrom(0)
//      //                /*返回结果的总数量，默认为10*/
      .setSize(2000)
  ;
  searchRequestBuilder1.execute().actionGet();
  SearchResponse response1 = searchRequestBuilder1.get();
  SearchHit[] searchHitsByPrepareSearch = response1.getHits().hits();
  for(SearchHit hitFields : searchHitsByPrepareSearch){
    Map<String, Object> obj = hitFields.getSource();
    Map<String, SearchHitField> obj1 = hitFields.getFields();
    SearchHitField HITS =  obj1.get("meta_案件名称");
    List<Object> list = HITS.getValues();
    for(Object oo : list){
      write.write(oo.toString()+"\t");
      i++;
      System.out.println(oo.toString()+"\n");
      System.out.println(i);
    }
//    System.out.println(obj.get("caseId"));
  }

  }

}
