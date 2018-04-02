import com.myproject.util.ClientHelper;
import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;

/**
 * Created by wei.wang on 2017/10/27.
 */
public class DelateAPI {

  @Test
  public  void ww(){
    QueryBuilder query = QueryBuilders.termQuery("searchtype","裁判结果");
    Client client = ClientHelper.getInstance().getClient();
    for(int i = 0; i<1000;i++) {
      SearchRequestBuilder searchRequestBuilder1 = new SearchRequestBuilder(client, SearchAction.INSTANCE);
      searchRequestBuilder1.setQuery(query).setSearchType(SearchType.QUERY_THEN_FETCH)
          .setIndices("searchtype_20171018").setTypes("linksearch")
          //                //                .setRequestCache(true)
          //                //                .setFetchSource(includeFields,excludeFields)
          //                /*从第几条结果开始返回，默认为0*/
          .setFrom(0)
          //                /*返回结果的总数量，默认为10*/
          .setSize(1000);
      searchRequestBuilder1.execute().actionGet();
      SearchResponse response = searchRequestBuilder1.get();
      SearchHit[] searchHitsByPrepareSearch = response.getHits().hits();
      for (SearchHit ss : searchHitsByPrepareSearch) {
        String id = ss.getId();
        String type = ss.getType();
        String index = ss.getIndex();
        client.prepareDelete(index, type, id).execute().actionGet();
      }
    }
  }

}
