package com.myproject.tools;

import com.myproject.util.ClientHelper;
import com.myproject.util.FilterBDU;
import com.myproject.util.FilterType;
import com.myproject.util.QueryType;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wei.wang on 2017/11/14.
 */
public class ToolTest {


  public void abnormal(){

    Map<String, FilterBDU> map = new HashMap<String, FilterBDU>();
    map.put("异常案件",new FilterBDU(FilterType.Must, QueryType.Term, "abnormal",true));
    QueryBuilder bilder = CustomQueryBuilder.boolQuery(map);
    Client client = ClientHelper.getInstance().getClient();
    SearchRequestBuilder searchRequestBuilder1 = new SearchRequestBuilder(client, SearchAction.INSTANCE);
    searchRequestBuilder1.setQuery(bilder)
        .setSearchType(SearchType.QUERY_THEN_FETCH)
        .setIndices("es_lxfdlawcase")
        //                //                .setRequestCache(true)
        //                //                .setFetchSource(includeFields,excludeFields)
        //                /*从第几条结果开始返回，默认为0*/
        .setFrom(0)
        //                /*返回结果的总数量，默认为10*/
        .setSize(170);




  }


}
