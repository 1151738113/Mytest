package com.myproject.ww;

import com.myproject.tools.CustomQueryBuilder;
import com.myproject.util.ClientHelper;
import com.myproject.util.FilterBDU;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.range.RangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wei.wang on 2017/11/18.
 */
public class Aggsression {
@Test
  public void ww(){

    Map<String, FilterBDU> map = new HashMap<String, FilterBDU>();
    QueryBuilder builder = CustomQueryBuilder.boolQuery1(map);

    AggregationBuilder agg1 = AggregationBuilders
        .filters("agg")
        .filter("filters",builder);
    AggregationBuilder agg = AggregationBuilders
        .terms("案由")
        .order(Terms.Order.term(true))
        .size(0)
        .field("meta_案由");
    agg1.subAggregation(agg);

//    AggregationBuilder aggnest =
//        AggregationBuilders.nested("nested").path("meta_人物信息.peopleAttrMap");
//    agg1.subAggregation(aggnest);
//    AggregationBuilder aggfilter =
//        AggregationBuilders.filters("agg2").filter("filteratr", (new CustomQueryBuilder()).myfilters).subAggregation(AggregationBuilders.reverseNested("lawcasecnt"));
//    aggnest.subAggregation(aggfilter);

//    AggregationBuilder aggs = AggregationBuilders
//        .terms("aaa")
//        .field("meta_人物信息.peopleAttrMap.info_入户")
//        .subAggregation(AggregationBuilders.reverseNested("name"));
//    aggnest.subAggregation(aggs);
//    AggregationBuilder subprobationtime = AggregationBuilders
//        .terms("缓刑1")
//        .order(Terms.Order.term(true))
//        .size(0)
//        .field("meta_人物信息.peopleAttrMap.info_缓刑月数");
//    aggfilter.subAggregation(subprobationtime);
//
//    AggregationBuilder subprobationtimecase = GetRangeBuilder("meta_人物信息.peopleAttrMap.info_缓刑月数", "缓刑统计").subAggregation(AggregationBuilders.reverseNested("缓刑2"));
//    aggfilter.subAggregation(subprobationtimecase);
//
//    MetricsAggregationBuilder statsprobationTime = AggregationBuilders
//        .extendedStats("statsprobationTime")
//        .field("meta_人物信息.peopleAttrMap.info_缓刑月数");
//    aggfilter.subAggregation(statsprobationTime);
//    MetricsAggregationBuilder percentilesprobationTime = AggregationBuilders
//        .percentiles("percentilesprobationTime")
//        .field("meta_人物信息.peopleAttrMap.info_缓刑月数")
//        .percentiles(25.0, 40.0, 50.0, 60.0, 75.0, 99.0);
//    aggfilter.subAggregation(percentilesprobationTime);

//  AggregationBuilder subagg1 = AggregationBuilders
//      .terms("剥夺政治权利时间统计2")
//      .size(0)
//      .order(Terms.Order.count(false))
//      .field("meta_人物信息.peopleAttrMap.info_剥夺政治权利期限");
//  aggfilter.subAggregation(subagg1);
//  AggregationBuilder deprivationpolitical = GetRangeBuilder("meta_人物信息.peopleAttrMap.info_剥夺政治权利期限", "剥夺政治权利").subAggregation(AggregationBuilders.reverseNested("deprivation"));
//  aggfilter.subAggregation(deprivationpolitical);
//  RangeBuilder rangeBuilder = GetRangeBuilder1();
//  aggfilter.subAggregation(rangeBuilder);
//  MetricsAggregationBuilder statsprobationTime1 = AggregationBuilders
//      .extendedStats("zhengzhiquanli")
//      .field("meta_人物信息.peopleAttrMap.info_剥夺政治权利期限");
//  aggfilter.subAggregation(statsprobationTime1);
//  MetricsAggregationBuilder percentilesprobationTime1 = AggregationBuilders
//      .percentiles("zhengzhiquanliTime")
//      .field("meta_人物信息.peopleAttrMap.info_剥夺政治权利期限")
//      .percentiles(25.0, 40.0, 50.0, 60.0, 75.0, 99.0);
//  aggfilter.subAggregation(percentilesprobationTime1);


    Client client = ClientHelper.getInstance().getClient();
    SearchRequestBuilder searchRequestBuilder = client.prepareSearch("es_fdlawcase")
        .addAggregation(agg1)
        .setSize(0);
    SearchResponse response = searchRequestBuilder
        .execute().actionGet();
  Filters aggs = response.getAggregations().get("agg");
  for(Filters.Bucket bucket : aggs.getBuckets()){
    Terms terms = bucket.getAggregations().get("案由");
    for(Terms.Bucket bucket1 : terms.getBuckets()){
      String name = bucket1.getKeyAsString();
      System.out.println(name);
    }
  }

  }


  private RangeBuilder GetRangeBuilder(String info_point,String aliasName) {
    RangeBuilder rangefineBuilder = new RangeBuilder(aliasName);
    rangefineBuilder.field(info_point);
    rangefineBuilder.addUnboundedTo("等于0", 0.1);
    rangefineBuilder.addUnboundedFrom("大于0", 0.1);
    return rangefineBuilder;
  }

  private RangeBuilder GetRangeBuilder1() {
    RangeBuilder rangefineBuilder = new RangeBuilder("剥夺政治权利时间统计1");
    rangefineBuilder.field("meta_人物信息.peopleAttrMap.info_剥夺政治权利期限");
    rangefineBuilder.addUnboundedTo("0（含）-6个月", 0.5);
    rangefineBuilder.addRange("6个月（含）-3年", 0.5, 3);
    rangefineBuilder.addRange("3（含）-5年", 3, 5);
    rangefineBuilder.addUnboundedFrom("5年及以上", 5);
    return rangefineBuilder;
  }

}
