package com.myproject.ww;

import com.myproject.util.ClientHelper;
import org.elasticsearch.client.Client;
import org.junit.Test;

/**
 * Created by wei.wang on 2017/11/9.
 * 删除单个索引
 */
public class OneIndexDelete {

  @Test
  public void ww(){

    Client client = ClientHelper.getInstance().getClient();
    client.prepareDelete("searchtype_20180312","linksearch","ce10827c3bc7403171c89ba54ac87b33")
    .execute().actionGet();


  }
}
