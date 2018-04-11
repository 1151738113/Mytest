package com.myproject.ww;

import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.Test;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 *该篇文章主要介绍如何学习ES，如何入门，ES简单的检索方式等(ES版本2.4.1)
   **/
public class Elasticsearch {

  //elasticsearch为一个开源的非关系型数据库，主要用于大数据的检索，目前对于聚合分析也在慢慢优化。
  //如何使用elasticsearch呢，首先我们要定义一个基本的存储结构
  /**
   * 如我们构建一个学生的索引，学生的属性有姓名、性别、年龄
   *
   *
   *{
   "settings": {
   "number_of_shards" : 1,
   "number_of_replicas" : 0
   },
   　　"mappings":{
   　　　　"students":{
   　　　　　　"properties":{
   　　　　　　　　"name":{
   　　　　　　　　　　"type":"string"
   　　　　　　　　},
   　　　　　　　　"age":{
   　　　　　　　　　　"type":"long"
   　　　　　　　　},
   　　　　　　　　"sex":{
   　　　　　　　　　　"type":"string"
   　　　　　　　　}
   　　　　　　}
   　　　　}
   　　}
   }
   */
  /**
   * 通过put指令构建新的索引
   * PUT students
   * {
   *   数据结构
   * }
   *
   * 然后通过put指令插入数据
   *
   * PUT students/student(type)/1(id)
   * {
   *   "name":"李明",
   *   "age":"16",
   *   "sex":"boy"
   * }
   */

  /**
   *这是索引已经构建成功了
   *ES提供了两种方式去查询，一种通过restfulApi，一种是java API方式查询
   */

  /**
   * restfulAPI是ES提供的一套DSL查询方式
   * 如：我们要查询姓名为李明的人
   * POST  students/_search
   {
   　　"query":{
   　　　　"term":{
   　　　　　　"name":{
   　　　　　　　　"value":"李明"
   　　　　　　}
   　　　　}
   　　}
   }
   ES提供了多种查询逻辑如: term(完全匹配)、match（包含）、exist（存在）、range（范围查询）、fuzzy（模糊匹配）、prefix（前缀匹配）等、
   通过不同的检索逻查找所需内容
   */

  /**
   * 如果我们同时通过多个条件查询
   * ES提供了条件与条件之间的逻辑关系Must（与）、should（或）、mustNot（非）
   * ES通过bool的形式将多个条件的逻辑关系表示出来
   * 如：我们要查找姓名为李明，年龄为16岁的人。
   * POST students/_search
   * {
   　　"query":{
   　　　　"bool":{
   　　　　　　"must":[
   　　　　　　　　{
   　　　　　　　　　　"term":{
   　　　　　　　　　　　　"name":{
   　　　　　　　　　　　　　　"value":"李明"
   　　　　　　　　　　　　}
   　　　　　　　　　　}
   　　　　　　　　},
   　　　　　　　　{
   　　　　　　　　　　"term":{
   　　　　　　　　　　　　"age":{
   　　　　　　　　　　　　　　"value":16
   　　　　　　　　　　　　}
   　　　　　　　　　　}
   　　　　　　　　}
   　　　　　　]
   　　　　}
   　　}
   }
   */

  /**
   * javaAPI方式查询
   */
  @Test
  public  void test(){
    QueryBuilder builder = QueryBuilders.termQuery("name","李明").boost(1L);
    System.out.println(builder.toString());
  }

  /**
   * 如果多条件查询
   */
  @Test
  public void test2(){
    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    QueryBuilder b1 = QueryBuilders.termQuery("name","李明");
    QueryBuilder b2 = QueryBuilders.termQuery("age",16);
    boolQueryBuilder.must(b1).must(b2);
    QueryBuilder builder = boolQueryBuilder;
    System.out.println(builder.toString());
  }

  /**
   * 如何通过java构建ES客户端
   */

  public void test3(){
    Client client;   //ES客户端Client
    String[] ips = {"192.168.0.201","192.168.0.158"};  //集群节点信息
    int port = 9300;  //端口号

    Settings setting = Settings
        .settingsBuilder()
        //设置client.transport.sniff为true来使客户端去嗅探整个集群的状态，把集群中其它机器的ip地址加到客户端中，这样做的好处是一般你不用手动设置集群里所有集群的ip到连接客户端，它会自动帮你添加，并且自动发现新加入集群的机器;
        .put("client.transport.sniff",true)
        .put("cluster.name","fd-data")  //集群名称
        .build();

    try {
      List<InetSocketTransportAddress> addressList = new ArrayList<InetSocketTransportAddress>();
      for(String ip:ips){
        addressList.add(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
      }
      client = TransportClient.builder().settings(setting).build()
          .addTransportAddresses(addressList
              .toArray(new InetSocketTransportAddress[addressList.size()]));
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    //最后创建一个client，构建客户端成功。
  }

  /**
   * 如何通过client进行查询
   */
  public void test5(Client client,QueryBuilder builder){
    SearchRequestBuilder searchRequestBuilderq = new SearchRequestBuilder(client, SearchAction.INSTANCE)
        .setQuery((builder))
        .setSearchType(SearchType.QUERY_AND_FETCH)  //检索模式，通过每个分片单独查询，将每个分片上返回的结果的必定比例推送出来，速度最快
        .setIndices("es_fdlawcase")
        //                //                .setRequestCache(true)
        //                //                .setFetchSource(includeFields,excludeFields)
        //                /*从第几条结果开始返回，默认为0*/
        .setFrom(0)
        //                /*返回结果的总数量，默认为10*/
        .setSize(5);
    searchRequestBuilderq.execute().actionGet();
    SearchResponse response1 = searchRequestBuilderq.get();
    SearchHits hits = response1.getHits();
    System.out.println(hits.getTotalHits());

  }

  /**
   * ES还提供了一系列的聚合方法
   * 例如：我想知道所有年龄学生构成有哪些
   * ES的聚合根据官方说明就是每个需要聚合的条件为一个桶，先创建一个大桶，然后将每个聚合小桶放入大桶中。其结构大概就是这样模式的。
   */

  public void test6(Client client){
    AggregationBuilder agg1 = AggregationBuilders.filters("agg").filter("filter",QueryBuilders.termQuery("xx","xx"));  //此块为根据用户的过滤条件
    AggregationBuilder agg2 = AggregationBuilders.terms("年龄").field("age").order(Terms.Order.term(false)); //对年龄进行
    agg1.subAggregation(agg2);

    SearchRequestBuilder searchRequestBuilder = client.prepareSearch("es_fdlawcase")
        .addAggregation(agg1)
        .setSize(0);
    SearchResponse response = searchRequestBuilder
        .execute().actionGet();
    Filters aggs = response.getAggregations().get("agg");
    for(Filters.Bucket bucket : aggs.getBuckets()){
      Terms terms = bucket.getAggregations().get("年龄");
      for(Terms.Bucket bucket1 : terms.getBuckets()){
        String name = bucket1.getKeyAsString();
        System.out.println(name);
      }
    }
  }


  /**
   * 此为ES的基础使用方式，未在检索原理，集群搭建，以及检索优化上有介绍，后期会继续补充。
   */




}
