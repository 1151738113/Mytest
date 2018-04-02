package com.myproject.util;



import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gengw on 11/1/16.
 * 在Java端使用是ES服务需要创建Java Client，
 * 但是每一次连接都实例化一个client，对系统的消耗很大，
 * 即使在使用完毕之后将client close掉，由于服务器不能及时回收socket资源，
 * 极端情况下会导致服务器达到最大连接数。
 * 为了解决上述问题并提高client利用率，使用池化技术复用client。
 */

public class ClientHelper {


    private Settings setting;

    private Map<String, Client> clientMap = new ConcurrentHashMap<String, Client>();

    private ClientHelper(){
        init();
        //TO-DO 添加你需要的client到helper
    }

    public static final ClientHelper getInstance() {
        return ClientHolder.INSTANCE;
    }



    private static class ClientHolder {
        private static final ClientHelper INSTANCE = new ClientHelper();
    }


    /**
     * 初始化默认的client
     */
    public void init() {
        try {
            Map<String,Integer> ips = SimCaseConfig.IP;
//            ips.put(localip, config.PORT);
            setting = Settings
                    .settingsBuilder()
                    .put("client.transport.sniff", true)
                    .put("cluster.name", SimCaseConfig.CLUSTER_NAME)
                    .build();
            addClient(setting, getAllAddress(ips));
        }catch (Exception ex)
        {
        }
    }


    /**
     * 获得所有的地址端口
     *
     * @return
     */
    public List<InetSocketTransportAddress> getAllAddress(Map<String, Integer> ips) {
        List<InetSocketTransportAddress> addressList = new ArrayList<InetSocketTransportAddress>();
      for (Iterator<Map.Entry<String,Integer>> iterator = ips.entrySet().iterator(); iterator.hasNext(); ) {
        Map.Entry<String,Integer> entry =iterator.next( );
        String ip = entry.getKey( );
        Integer port = entry.getValue();
        try {
          addressList.add(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
        } catch (UnknownHostException e) {
        }
      }
        return addressList;
    }

    public Client getClient() {
        return getClient(setting.get("cluster.name"));
    }

    public Client getClient(String clusterName) {
        return clientMap.get(clusterName);
    }

    public void addClient(Settings settings, List<InetSocketTransportAddress> transportAddress) {
        TransportClient client = TransportClient.builder().settings(settings).build()
                .addTransportAddresses(transportAddress
                        .toArray(new InetSocketTransportAddress[transportAddress.size()]));
        clientMap.put(setting.get("cluster.name"), client);
    }
}
