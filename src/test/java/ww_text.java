import com.myproject.tools.CustomQueryBuilder;
import com.myproject.util.*;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on ${DATA}.
 */
public class ww_text {

  public static void main(String[] args) throws Exception {

    Map<String, FilterBDU> map = new HashMap<String, FilterBDU>();
    Map<String, FilterBDU> map1 = new HashMap<String, FilterBDU>();
    map.put("aaa",new FilterBDU(FilterType.Must,QueryType.Term,"meta_案由","盗窃罪"));
//    FilterBDU filterBDU
//            =new FilterBDU(FilterType.Must, QueryType.Term)

//    FilterBDU bb2 =
//                new FilterBDU(FilterType.Must, QueryType.Term, "meta_人物信息.peopleAttrMap.info_情节轻微",
//                        true);
//        bb2.setIsnestedfield(true);
//    map.put("情节轻微" , bb2);

    FilterBDU bb3 =
            new FilterBDU(FilterType.Must, QueryType.Term, "meta_人物信息.peopleAttrMap.info_入户盗窃",
                    true);
    bb3.setIsnestedfield(true);
    map.put("入户盗窃" , bb3);


    //    FilterBDU bb1 =
//        new FilterBDU(FilterType.Must, QueryType.Term, "meta_人物信息.peopleAttrMap.judgmentType", "无罪");
//    bb1.setIsnestedfield(true);
//    map.put("无罪",bb1);
//    map.put("",new FilterBDU(FilterType.Must,QueryType.Term,"meta_判决类型","判决书"));
//    FilterBDU bb = new FilterBDU(FilterType.Must,QueryType.Term,"meta_人物信息.peopleAttrMap.info_入户盗窃",true);
//    bb.setIsnestedfield(true);
//    map.put("bbb",bb);
//    FilterBDU bb1 = new FilterBDU(FilterType.Must,QueryType.Term,"meta_人物信息.peopleAttrMap.info_携带凶器",true);
//    bb1.setIsnestedfield(true);
//    map1.put("bbb1",bb1);
    QueryBuilder builder = CustomQueryBuilder.boolQuery(map);
//    QueryBuilder builder1 = CustomQueryBuilder.boolQuery(map1);
//    BoolQueryBuilder build = new BoolQueryBuilder();
//    build.must(builder1);
//    StatsAlg statsAlg = new StatsAlg();
//    Set<StatsBDU> statsData = new HashSet<StatsBDU>();
    Client client = ClientHelper.getInstance().getClient();
//    Map<String, Set<Object>> jsonString = statsAlg.FieldStats(client, builder, statsData);
//    System.out.print(JSON.toJSONString(jsonString, true));
    AggregationBuilder aggregation = AggregationBuilders
        .filters("agg")
        .filter("filters", builder);
//    AggregationBuilder aggnest2 =
//        AggregationBuilders.nested("nested").path("meta_人物信息");
//    aggregation.subAggregation(aggnest2);
//    AggregationBuilder peopleName = AggregationBuilders
//        .terms("name")
//        .size(0)
//        .order(Terms.Order.count(false))
//        .field("meta_人物信息.pname");
//    AggregationBuilder aggfilter111 =
//        AggregationBuilders.filters("agg2").filter("filteratr111", (new CustomQueryBuilder()).myfilters).subAggregation(AggregationBuilders.reverseNested("lawcasecnt"));
//    aggnest2.subAggregation(aggfilter111);
//    AggregationBuilder aggest11 = AggregationBuilders
//        .terms("sad")
//        .size(0)
//        .order(Terms.Order.count(false))
//        .field("meta_案由");
//    aggregation.subAggregation(aggest11);
//    AggregationBuilder aggreData = AggregationBuilders
//        .dateHistogram("时间日期")
//        .field("meta_裁判日期")
//        .interval(DateHistogramInterval.YEAR);
//    aggregation.subAggregation(aggreData);
    AggregationBuilder aggnest =
        AggregationBuilders.nested("nested").path("meta_人物信息.peopleAttrMap");
    aggregation.subAggregation(aggnest);
    AggregationBuilder aggfilter =
        AggregationBuilders.filters("agg2").filter("filteratr111", (new CustomQueryBuilder()).myfilters).subAggregation(AggregationBuilders.reverseNested("lawcasecnt"));
    aggnest.subAggregation(aggfilter);
//    AggregationBuilder subagg2 = AggregationBuilders
//        .terms("携带凶器1")
//        .size(0)
//        .order(Terms.Order.count(false))
//        .field("meta_人物信息.peopleAttrMap.info_携带凶器").subAggregation(AggregationBuilders.reverseNested("ruhu1"));
//    aggnest.subAggregation(subagg2);
//
//    AggregationBuilder aggnest1 =
//        AggregationBuilders.nested("nested1").path("meta_人物信息.peopleAttrMap");
//    aggregation.subAggregation(aggnest1);
//    AggregationBuilder subagg3 = AggregationBuilders
//        .terms("携带凶器2")
//        .size(0)
//        .order(Terms.Order.count(false))
//        .field("meta_人物信息.peopleAttrMap.info_携带凶器").subAggregation(AggregationBuilders.reverseNested("ruhu2"));
//    aggnest.subAggregation(subagg3);
////
//    AggregationBuilder subagg4 = AggregationBuilders
//        .terms("入户盗窃")
//        .size(0)
//        .order(Terms.Order.count(false))
//        .field("meta_人物信息.peopleAttrMap.info_入户盗窃").subAggregation(AggregationBuilders.reverseNested("ruhu3"));
//    aggnest.subAggregation(subagg4);

//    AggregationBuilder FineType = AggregationBuilders
//        .terms("判罚类型")
//        .size(0)
//        .order(Terms.Order.count(false))
//        .field("meta_人物信息.peopleAttrMap.judgmentType").subAggregation(AggregationBuilders.reverseNested("judgetypecnt"));
//    aggnest.subAggregation(FineType);

    SearchRequestBuilder searchRequestBuilder = client.prepareSearch(SimCaseConfig.LXIndexName)
        .addAggregation(aggregation)
        .setSize(0);
 System.out.print(searchRequestBuilder.toString());
    SearchResponse response = searchRequestBuilder
        .execute().actionGet();
    System.out.print(response.toString());
//    Filters agg = response.getAggregations().get("agg");
//    for (Filters.Bucket entry : agg.getBuckets()) {
//      Nested filters1 = entry.getAggregations().get("filters");
//      Histogram tt = entry.getAggregations().get("时间日期");
//      for(Histogram.Bucket entry1 : tt.getBuckets()){
//        System.out.println("key: "+entry1.getKeyAsString()+"  value:"+entry1.getDocCount());
//      }
////      System.out.print("1------------"+filters1.getDocCount());
//      Nested nestedagg = entry.getAggregations().get("nested");
//      long ss  =  nestedagg.getDocCount();
//      //获取doc数量
//      System.out.println("2------------"+ss);
////      Terms FineTypes = nestedagg.getAggregations().get("判罚类型");
////      for(Terms.Bucket entry1 : FineTypes.getBuckets()){
////        String key = entry1.getKeyAsString();
////        System.out.println("key: "+key+"  value:"+entry1.getDocCount());
////        InternalReverseNested SSS = entry1.getAggregations().get("judgetypecnt");
////        System.out.println("key: "+key+"  value:"+entry1.getDocCount()+"  much:"+SSS.getDocCount());
////      }
//
//
//      Filters agg2 = nestedagg.getAggregations().get("agg2");
//      for(Filters.Bucket entry1 : agg2.getBuckets()){
//        System.out.println("3-----------------"+entry1.getDocCount());
//        InternalReverseNested SSS = entry1.getAggregations().get("lawcasecnt");
//        System.out.println("4-----------------"+SSS.getDocCount());
//      }
//      Terms xdxq = nestedagg.getAggregations().get("携带凶器2");
//      for(Terms.Bucket entry1 : xdxq.getBuckets()){
//        System.out.println(entry1.getKeyAsString()+"----------"+entry1.getDocCount());
//        InternalReverseNested SSS = entry1.getAggregations().get("ruhu2");
//        System.out.println("----------"+SSS.getDocCount());
//      }
//
//    }


  }


}
