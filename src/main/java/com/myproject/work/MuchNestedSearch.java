package com.myproject.work;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Created by wei.wang on 2018/1/4.
 * 多重嵌套的实现方式
 */
public class MuchNestedSearch {

  public static void main(String[] args){
    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
    BoolQueryBuilder boolQueryBuilder2 = new BoolQueryBuilder();
    QueryBuilder builder = QueryBuilders.termsQuery("meta_案由","盗窃罪");
    QueryBuilder builder1 = QueryBuilders.matchQuery("meta_条件2","冬雪");
    QueryBuilder builder2 = QueryBuilders.matchQuery("meta_条件2","夏日");
    QueryBuilder builder3 = QueryBuilders.matchQuery("meta_条件2","春风");
    boolQueryBuilder1.must(builder);
    boolQueryBuilder1.mustNot(builder1);
    boolQueryBuilder2.should(builder2);
    boolQueryBuilder2.should(builder3);
    boolQueryBuilder.must(boolQueryBuilder1);
    boolQueryBuilder.must(boolQueryBuilder2);
    System.out.println(boolQueryBuilder.toString());

  }

}
