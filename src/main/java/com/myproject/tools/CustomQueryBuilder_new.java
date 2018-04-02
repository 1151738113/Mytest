//package com.futuredata.tools;
//
//import com.futuredata.util.*;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.index.query.RangeQueryBuilder;
//
//import java.util.*;
//
///**
// * Created by wei.wang on 2018/1/5.
// * 解决多层嵌套的模式
// */
//public class CustomQueryBuilder_new {
//
//
//  public static List<String> highlightfield ;
//  public static List<String> keywordList ;
//  public static BoolQueryBuilder myfilters;
//
//  public static QueryBuilder boolquery(Map<String,ExtendBDU> filterbdu){
//
//    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//    myfilters = new BoolQueryBuilder();
//    Map<String,Set<ExtendBDU>> nestedfilter=new HashMap<String,Set<ExtendBDU>>();
//    highlightfield = new Vector<String>();
//    keywordList = new Vector<String>();
//    int nestedNum = 0,mustNum = 0;
//    for(Map.Entry<String,ExtendBDU> entry:filterbdu.entrySet()) {
//      ExtendBDU extendBDU = entry.getValue();
//      //确定是否需要嵌套
//      //如果是嵌套
//      if (extendBDU.isnestedfield()) {
//        String info_point = extendBDU.getInfo_point();
//        String nestedPath = info_point.substring(0, info_point.lastIndexOf("."));
//        if (extendBDU.getFiltertype().equals(FilterType.Probably))
//          nestedNum++;
//        if (extendBDU.getFiltertype().equals(FilterType.Must)) {
//          mustNum++;
//            if (extendBDU.getQuerytype().equals(QueryType.Term)) {
//              QueryBuilder queryBuilder =
//                  QueryBuilders.termQuery(extendBDU.getInfo_point(), extendBDU.getInfo_point_val()).boost(extendBDU.getInfo_point_weight());
//              myfilters.must(queryBuilder);
//
//            } else if (extendBDU.getQuerytype().equals(QueryType.Range)) {
//              RangeQueryBuilder rangeQueryBuilder = GetRangeQueryBuilder(extendBDU);
//              myfilters.must(rangeQueryBuilder).boost(extendBDU.getInfo_point_weight());
//            }
//        } else if (extendBDU.getFiltertype().equals(FilterType.MustNot)) {
//            if (extendBDU.getQuerytype().equals(QueryType.Term)) {
//              QueryBuilder queryBuilder =
//                  QueryBuilders.termQuery(extendBDU.getInfo_point(), extendBDU.getInfo_point_val()).boost(extendBDU.getInfo_point_weight());
//              myfilters.mustNot(queryBuilder);
//            }
//        }
//        if (!nestedfilter.containsKey(nestedPath))
//          nestedfilter.put(nestedPath, new HashSet<ExtendBDU>());
//        nestedfilter.get(nestedPath).add(extendBDU);
//      }else{
//        //查看层数
//       List<String> group_list = extendBDU.getInfo_group_bool();
//
//        String groups = extendBDU.getInfo_group();
//        if (groups.equals("")) {
//          AddGroupFilter(boolQueryBuilder, extendBDU);
//        } else {
//          try {
//            if (!groupsMap.containsKey(groups)) {
//              BoolQueryBuilder boolgroupBuilder = new BoolQueryBuilder();
//              boolgroupBuilder.minimumShouldMatch("1");
//              groupsMap.put(groups, boolgroupBuilder);
//              AddGroupFilter(boolgroupBuilder, bdu);
//              boolQueryBuilder.must(boolgroupBuilder);
//              //                            logger.info("group:"+boolgroupBuilder);
//            } else {
//              BoolQueryBuilder boolgroupBuilder = groupsMap.get(groups);
//              AddGroupFilter(boolgroupBuilder, bdu);
//              //                            logger.info("group:"+boolgroupBuilder);
//            }
//          } catch (Exception ex) {
//            //                        logger.error("boolQuery:" + ex.getMessage());
//          }
//        }
//      }
//
//
//
//    }
//
//
//    return boolQueryBuilder;
//  }
//
//
//  private static RangeQueryBuilder GetRangeQueryBuilder(FilterBDU bdu)
//  {
//    RangeBDU rangeBDU = (RangeBDU)bdu.getInfo_point_val();
//    RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(bdu.getInfo_point());
//    rangeQueryBuilder.queryName(bdu.getInfo_point());
//    if(rangeBDU.getFrom()!=null) {
//      rangeQueryBuilder.gte(rangeBDU.getFrom());
//    }
//    if(rangeBDU.getTo()!=null) {
//      rangeQueryBuilder.lt(rangeBDU.getTo());
//    }
//
//    return rangeQueryBuilder;
//  }
//}
