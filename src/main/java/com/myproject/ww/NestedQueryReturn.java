package com.myproject.ww;

import com.myproject.tools.CustomQueryBuilder;
import com.myproject.util.*;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wei.wang on 2017/11/9.
 */
public class NestedQueryReturn {
@Test
public void ww(){

  Map<String, FilterBDU> filterMap = new HashMap<String, FilterBDU>();
//  filterMap.put("案由",new FilterBDU(FilterType.Must, QueryType.Term, "meta_案由","盗窃罪"));
////  FilterBDU filterBDU = new FilterBDU(FilterType.Must, QueryType.Term, "meta_人物信息.peopleAttrMap.info_入户盗窃 ",true,"1");
////  filterBDU.setIsnestedfield(true);
////  filterMap.put("入户",filterBDU);
//  FilterBDU filterBDU = new FilterBDU(FilterType.Must, QueryType.Term, "meta_人物信息.peopleAttrMap.info_多次盗窃",true,"1");
//  filterBDU.setIsnestedfield(true);
//  filterMap.put("多次盗窃",filterBDU);
//  FilterBDU filterBDU1 = new FilterBDU(FilterType.Must, QueryType.Term, "meta_人物信息.peopleAttrMap.info_主动归还",true,"2");
//  filterBDU1.setIsnestedfield(true);
//  filterMap.put("主动归还",filterBDU1);
//  FilterBDU filterBDU2 = new FilterBDU(FilterType.Must, QueryType.Range, "meta_人物信息.peopleAttrMap.info_总金额", new RangeBDU(5000,10000),"3");
//  filterBDU2.setIsnestedfield(true);
//  filterMap.put("金额",filterBDU2);

  FilterBDU f1 = new FilterBDU(FilterType.Probably, QueryType.Term, "aa1","aa1","w1");
  FilterBDU f2 = new FilterBDU(FilterType.Probably, QueryType.Term, "aa2","aa2","w1");
  FilterBDU f3 = new FilterBDU(FilterType.Probably, QueryType.Term, "aa3","aa3","w2");
  FilterBDU f4 = new FilterBDU(FilterType.Probably, QueryType.Term, "aa4","aa4","w2");
  filterMap.put("we",f1);
  filterMap.put("ws",f2);
  filterMap.put("sa",f3);
  filterMap.put("dd",f4);
  QueryBuilder builder = CustomQueryBuilder.boolQuery(filterMap);
  Client client = ClientHelper.getInstance().getClient();
  SearchRequestBuilder searchRequestBuilderq = new SearchRequestBuilder(client, SearchAction.INSTANCE)
      .setQuery((builder))
      .setSearchType(SearchType.QUERY_AND_FETCH)
      .setIndices("es_fdlawcase")
      //                //                .setRequestCache(true)
      //                //                .setFetchSource(includeFields,excludeFields)
      //                /*从第几条结果开始返回，默认为0*/
      .setFrom(0)
      //                /*返回结果的总数量，默认为10*/
      .setSize(5);
  searchRequestBuilderq.execute().actionGet();
    SearchResponse response1 = searchRequestBuilderq.get();
    SearchHits hits = response1.getHits();
    System.out.println(hits.getTotalHits());

}

}
