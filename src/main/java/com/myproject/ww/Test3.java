package com.myproject.ww;

import com.myproject.tools.CustomQueryBuilder;
import com.myproject.util.*;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wei.wang on 2017 10.
 */
public class Test3 {

  @Test
  public void ww1() {
   Map<String, FilterBDU> map = new HashMap<String, FilterBDU>();
//    map.put("案由", new FilterBDU(FilterType.Must, QueryType.Term, "meta_案由","金融借款合同纠纷"));
//    FilterBDU filterBDU = new FilterBDU(FilterType.Must, QueryType.Match, "lawName", "刑法");
//    filterBDU.setIsphrase(true);
//    map.put("关键字",filterBDU);
//    map.put("111",new FilterBDU(FilterType.Probably, QueryType.Term, "forceGrade","行政法规","效力等级",8));
//    map.put("222",new FilterBDU(FilterType.Probably, QueryType.Term, "forceGrade","司法解释","效力等级",6));
//    map.put("333",new FilterBDU(FilterType.Probably, QueryType.Term, "forceGrade","法律","效力等级",10));

    QueryBuilder builder = CustomQueryBuilder.boolQuery(map);
//    QueryBuilder builder1 = QueryBuilders.boolQuery().must(QueryBuilders.wildcardQuery("lawName","刑法")).must(QueryBuilders.termQuery("forceGrade","法律").boost(10)).must(QueryBuilders.termQuery("forceGrade","行政法规").boost(8)).must(QueryBuilders.termQuery("forceGrade","司法解释").boost(6));
    Client client = ClientHelper.getInstance().getClient();
//    SearchRequestBuilder searchRequestBuilder = new SearchRequestBuilder(client, SearchAction.INSTANCE);
//    searchRequestBuilder.setQuery((builder))
//        .setSearchType(SearchType.QUERY_THEN_FETCH)
//        .setIndices("es_law")
//        .setFrom(0)
//        .setSize(50);
//    searchRequestBuilder.execute().actionGet();
//    SearchResponse response = searchRequestBuilder.get();
//    SearchHit[] searchHitsByPrepareSearch = response.getHits().hits();
//    Set<Object> set = new LinkedHashSet<Object>();
//    for(SearchHit hitFields : searchHitsByPrepareSearch){
//      Map<String, Object> items = hitFields.getSource();
//      set.add(items.get("lawName"));
////      System.out.println(items.get("lawName"));
//    }
//    System.out.println(set);





        AggregationBuilder aggregationBuilder = AggregationBuilders
        .filters("agg")
        .filter(builder);
    AggregationBuilder aggregationBuilder2 = AggregationBuilders
        .terms("法规名称")
        .size(0)
        .order(Terms.Order.count(false))
        .field("searchtype");
    aggregationBuilder.subAggregation(aggregationBuilder2);
//    AggregationBuilder aggregationBuilder1 = AggregationBuilders
//        .terms("测试")
//        .size(0)
//        .order(Terms.Order.count(false))
//        .field("forceGrade");
//    aggregationBuilder.subAggregation(aggregationBuilder1);
    SearchRequestBuilder searchRequestBuilder = client.prepareSearch("es_searchtype1")
        .addAggregation(aggregationBuilder)
        .setSize(0);
    SearchResponse response = searchRequestBuilder
        .execute().actionGet();
System.out.println(response.toString());

}
}
