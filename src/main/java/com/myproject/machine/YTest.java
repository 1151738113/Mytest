package com.myproject.machine;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;

import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

/**
 * Created by wei.wang on 2018/1/5.
 */
public class YTest {
  @Test
  public void ww(){
    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    QueryBuilder queryBuilder = QueryBuilders.termQuery("meta_案由","盗窃罪");
    QueryBuilder queryBuilder1 = QueryBuilders.termQuery("meta_test","aaa");
    QueryBuilder queryBuilder2 = QueryBuilders.termQuery("meta_案号","acc");
    boolQueryBuilder.must(queryBuilder);
    boolQueryBuilder.must(nestedQuery("das",queryBuilder1));
    boolQueryBuilder.must(nestedQuery("das",queryBuilder2));
    boolQueryBuilder.must(nestedQuery("das",queryBuilder2));
    System.out.println(boolQueryBuilder.toString());
  }
}
