package com.myproject.ww;

import com.myproject.util.ClientHelper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

/**
 * Created by wei.wang on 2018/2/6.
 */
public class CreatJson {

  public static void main(String[] args){
    QueryBuilder builder = QueryBuilders.existsQuery("meta_案号");
    AggregationBuilder agg1 = AggregationBuilders
        .filters("agg").filter(builder);
    AggregationBuilder a1 = AggregationBuilders.terms("a").field("meta_案由");
    agg1.subAggregation(a1);
    Client client = ClientHelper.getInstance().getClient();
    SearchRequestBuilder searchRequestBuilder = client.prepareSearch("es_fdlawcase")
        .addAggregation(agg1)
        .setSize(0);
    searchRequestBuilder.execute().actionGet();
  }

}
