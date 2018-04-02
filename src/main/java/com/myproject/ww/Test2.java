package com.myproject.ww;

import com.myproject.tools.CustomQueryBuilder;
import com.myproject.util.*;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.RangeBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on ${DATA}.
 */
public class Test2 {
  public static void main(String[] args) throws IOException {
    Map<String, FilterBDU> map = new HashMap<String, FilterBDU>();
    Set<StatsBDU> statsData= Utils.readToMap("data/casestats","诈骗罪");
    QueryBuilder builder = CustomQueryBuilder.boolQuery(map);
    AggregationBuilder aggregation =
        AggregationBuilders
            .filters("agg")
            .filter("filters", builder);

    AggregationBuilder as2 = AggregationBuilders
        .terms("meta_省")
        .field("meta_省");
    aggregation.subAggregation(as2);
    AggregationBuilder aggnested =
        AggregationBuilders.nested("peopleAttr").path("meta_人物信息.peopleAttrMap");
    aggregation.subAggregation(aggnested);
    for(StatsBDU statsBDU : statsData){
      if(statsBDU.getStatstype().equals(StatsType.Term)){
        AggregationBuilder aggregationBuilder1  =
            AggregationBuilders.terms(statsBDU.getAliasName())
            .size(0).order(Terms.Order.term(false)).field(statsBDU.getInfo_point());
        aggnested.subAggregation(aggregationBuilder1);
      }else if(StatsType.Range.equals(statsBDU.getStatstype())){
        RangeBuilder rangeBuilder = CustomRangeBuilder(statsBDU);
        rangeBuilder.field(statsBDU.getInfo_point());
        aggnested.subAggregation(rangeBuilder);

      }
    }
        Client client = ClientHelper.getInstance().getClient();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("main_lawcase20170821")
            .addAggregation(aggregation)
            .setSize(0);
        SearchResponse response = searchRequestBuilder
            .execute().actionGet();
System.out.println(response.toString());














  }
  private static RangeBuilder CustomRangeBuilder(StatsBDU bdu)
  {
    Object obj = bdu.getStatsval();
    RangeBuilder rangeBuilder = new RangeBuilder(bdu.getAliasName());
    //        AggregationBuilders.range("").field("").subAggregation(rangeBuilder);
    if (obj instanceof int[]) {
      //            obj = (int[]) bdu.getStatsval();
      int rangeSize = ((int[]) bdu.getStatsval()).length;
      for (int rangeNo = 0; rangeNo <= rangeSize; rangeNo++) {
        if (rangeNo == 0)
          rangeBuilder.addUnboundedTo(((int[]) bdu.getStatsval())[rangeNo]);
        else if (rangeNo == rangeSize)
          rangeBuilder.addUnboundedFrom(((int[]) bdu.getStatsval())[rangeNo-1]);
        else
          rangeBuilder.addRange(((int[]) bdu.getStatsval())[rangeNo - 1], ((int[]) bdu.getStatsval())[rangeNo]);
      }
    } else if (obj instanceof double[]) {
      //            obj = (double[]) bdu.getStatsval();
      int rangeSize = ((double[]) bdu.getStatsval()).length;
      for (int rangeNo = 0; rangeNo <= rangeSize; rangeNo++) {
        if (rangeNo == 0)
          rangeBuilder.addUnboundedTo(((double[]) bdu.getStatsval())[rangeNo]);
        else if (rangeNo == rangeSize)
          rangeBuilder.addUnboundedFrom(((double[]) bdu.getStatsval())[rangeNo-1]);
        else
          rangeBuilder.addRange(((double[]) bdu.getStatsval())[rangeNo - 1], ((double[]) bdu.getStatsval())[rangeNo]);
      }
    }else if (obj instanceof float[]) {
      //            obj = (double[]) bdu.getStatsval();
      int rangeSize = ((float[]) bdu.getStatsval()).length;
      for (int rangeNo = 0; rangeNo <=rangeSize; rangeNo++) {
        if (rangeNo == 0)
          rangeBuilder.addUnboundedTo(((float[]) bdu.getStatsval())[rangeNo]);
        else if (rangeNo == rangeSize)
          rangeBuilder.addUnboundedFrom(((float[]) bdu.getStatsval())[rangeNo-1]);
        else
          rangeBuilder.addRange(((float[]) bdu.getStatsval())[rangeNo - 1], ((float[]) bdu.getStatsval())[rangeNo]);
      }
    }

    return rangeBuilder;
  }
}
