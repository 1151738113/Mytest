package com.myproject.elastic;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wei.wang on 2017/12/1.
 * how to create elastic client
 */
public class CreatElasticClient {

  //first,create setting
   Settings setting;
   Client client;
   String[] ips = {"192.168.0.201","192.168.0.158"};
   int port = 9300;

  public void ClientInit(){
    setting = Settings
        .settingsBuilder()
        //设置client.transport.sniff为true来使客户端去嗅探整个集群的状态，把集群中其它机器的ip地址加到客户端中，这样做的好处是一般你不用手动设置集群里所有集群的ip到连接客户端，它会自动帮你添加，并且自动发现新加入集群的机器;
        .put("client.transport.sniff",true)
        .put("cluster.name","fd-data")
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

  }




//  //获得ip信息
//  private List<InetSocketTransportAddress> getAllAddress(Map<String, Integer> ips){
//    List<InetSocketTransportAddress> addressList = new ArrayList<InetSocketTransportAddress>();
//    //这种写法可以等价于for(int i=0(定义一个对象); i<length(给定一个约束条件); i++(可有可无))
//    for (Iterator<Map.Entry<String,Integer>> iterator = ips.entrySet().iterator(); iterator.hasNext(); ) {
//      Map.Entry<String,Integer> entry =iterator.next( );
//      String ip = entry.getKey( );
//      Integer port = entry.getValue();
//      try {
//        addressList.add(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
//      } catch (UnknownHostException e) {
//      }
//    }
//    return addressList;
//  }

}
