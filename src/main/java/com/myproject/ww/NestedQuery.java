package com.myproject.ww;

import com.myproject.tools.CustomQueryBuilder;
import com.myproject.util.ClientHelper;
import com.myproject.util.FilterBDU;
import com.myproject.util.FilterType;
import com.myproject.util.QueryType;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wei.wang on 2017/11/9.
 */
public class NestedQuery {
@Test
  public void ww(){

    Map<String, FilterBDU> filterMap = new HashMap<String, FilterBDU>();
    filterMap.put("",new FilterBDU(FilterType.Must,QueryType.Term,"searchtype","案由"));
//    filterMap.put("案由",new FilterBDU(FilterType.Must, QueryType.Term, "meta_案由","盗窃罪"));
//    FilterBDU filterBDU = new FilterBDU(FilterType.Must, QueryType.Term, "meta_人物信息.peopleAttrMap.info_多次盗窃",true,"1");
//    filterBDU.setIsnestedfield(true);
//    filterMap.put("多次盗窃",filterBDU);
//  FilterBDU filterBDU1 = new FilterBDU(FilterType.Must, QueryType.Term, "meta_人物信息.peopleAttrMap.info_主动归还",true,"2");
//  filterBDU1.setIsnestedfield(true);
//  filterMap.put("主动归还",filterBDU1);

    QueryBuilder builder = CustomQueryBuilder.boolQuery1(filterMap);
    AggregationBuilder agg1 = AggregationBuilders
        .filters("agg")
        .filter("filters",builder);
    AggregationBuilder aggregationBuilder = AggregationBuilders.terms("anyou").field("searchcontent");
  agg1.subAggregation(aggregationBuilder);
    //    AggregationBuilder nested1 = AggregationBuilders.nested("agg2").path("meta_人物信息.peopleAttrMap");
//        ;agg1.subAggregation(nested1);
//      AggregationBuilder aggfilter =
//          AggregationBuilders.filters("agg2").filter("filteratr111", (new CustomQueryBuilder()).myfilters).subAggregation(AggregationBuilders.reverseNested("lawcasecnt"));
//    nested1.subAggregation(aggfilter);
//        RangeBuilder rangeBuilder = new RangeBuilder("盗窃金额");
//          rangeBuilder.field("meta_人物信息.peopleAttrMap.info_总金额");
//          rangeBuilder.addUnboundedTo(0);
//          rangeBuilder.addRange(0,5000.0);
//          rangeBuilder.addRange(5000.0,10000.0);
//          rangeBuilder.addUnboundedFrom(10000.0);
//          rangeBuilder.subAggregation(AggregationBuilders.reverseNested("ss"));
//    nested1.subAggregation(rangeBuilder);
    Client client = ClientHelper.getInstance().getClient();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("es_searchtype1")
            .addAggregation(agg1)
            .setSize(0);
        SearchResponse response = searchRequestBuilder
            .execute().actionGet();
  Filters agg = response.getAggregations().get("agg");
  for (Filters.Bucket entry : agg.getBuckets()) {
    Terms terms = entry.getAggregations().get("anyou");
    for(Terms.Bucket bucket : terms.getBuckets()){
      System.out.println(bucket.getKey());
    }

  }

  }





}
