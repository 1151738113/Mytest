package com.myproject.elastic;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.junit.Test;

/**
 * Created by wei.wang on 2017/12/1.
 * how to create elastic index from java client
 */
public class CreatIndex {
  CreatElasticClient creatElasticClient = new CreatElasticClient();
  @Test
  public void creat(){
    //创建客户端
    creatElasticClient.ClientInit();

//    CreateIndexRequestBuilder index = creatElasticClient.client.admin().indices().prepareCreate("my_index_20171202");
    DeleteIndexRequestBuilder delte = creatElasticClient.client.admin().indices().prepareDelete("my_index_20171201");
    delte.execute().actionGet();

  }


  //创建一个query语句
  public void creaetBuilder(){
    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//    boolQueryBuilder.must();
//    boolQueryBuilder.mustNot();
//    boolQueryBuilder.should();
//    boolQueryBuilder.minimumShouldMatch();
//    boolQueryBuilder.filter();
//    boolQueryBuilder.minimumNumberShouldMatch()
    //完全匹配
    QueryBuilder queryBuilder = QueryBuilders.termQuery("","").boost(1.0f);
    //存在某个字段
    ExistsQueryBuilder builder1 = QueryBuilders.existsQuery("");
    //查出所有docment
    MatchAllQueryBuilder builder2 =  QueryBuilders.matchAllQuery();
    //匹配查询(短语,前缀)
    MatchQueryBuilder builder3 = QueryBuilders.matchPhrasePrefixQuery("","").boost(1.0f);
    //短语匹配查询
    MatchQueryBuilder builder4 = QueryBuilders.matchPhraseQuery("","").boost(1.0f);
    //匹配查询
    MatchQueryBuilder builder5 = QueryBuilders.matchQuery("","");
    //对多个字段进行匹配,text（匹配的字段）,fieldNames（需要匹配的字段，为数组）；
    MultiMatchQueryBuilder builder6 = QueryBuilders.multiMatchQuery("","");
    //模糊匹配（通过计算边界距离，选择最接近的doc）
    FuzzyQueryBuilder builder7  = QueryBuilders.fuzzyQuery("","");
    //
    MoreLikeThisQueryBuilder builder8 = QueryBuilders.moreLikeThisQuery("");
    //范围查询
    RangeQueryBuilder rangeBuild =  QueryBuilders.rangeQuery("").from(3).to(1);
    //通配符及正则查询
    RegexpQueryBuilder regix = QueryBuilders.regexpQuery("","");
    //前缀查询
    PrefixQueryBuilder prefix = QueryBuilders.prefixQuery("", "");
    //通过script脚本编写自定义的打分逻辑（尽量不要使用）
    ScriptQueryBuilder ss = QueryBuilders.scriptQuery(new Script(""));
    //查询的某个字段从段开头的偏移量
    SpanFirstQueryBuilder first = QueryBuilders.spanFirstQuery(QueryBuilders.spanOrQuery(),3);
    //这个查询如果单独使用，效果跟term查询差不多，但是一般还是用于其他的span查询的子查询。
    SpanTermQueryBuilder spanTerm= QueryBuilders.spanTermQuery("", "");
    //模糊查询，?匹配单个字符，*匹配多个字符
    WildcardQueryBuilder wilder = QueryBuilders.wildcardQuery("meta_案由","*盗窃？");



  }

}
