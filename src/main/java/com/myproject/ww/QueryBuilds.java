package com.myproject.ww;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Created by admin on ${DATA}.
 * 创建query
 */
public class QueryBuilds {

  public static BoolQueryBuilder myfilters;

  //public static QueryBuilder getQueryBuild(Map<String, FilterBDU> condition){
  //     BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
  //     myfilters = new BoolQueryBuilder();
  //
  //
  //
  //
  //
  //}
  public static void main(String[] args) {

    QueryBuilder queryBuilders = QueryBuilders.boolQuery();
    myfilters =new BoolQueryBuilder();

    myfilters.filter(queryBuilders);
    myfilters.must(queryBuilders);
    myfilters.mustNot(queryBuilders);
    myfilters.minimumShouldMatch("1");
  }




}
