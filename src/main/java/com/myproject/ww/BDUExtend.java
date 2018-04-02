package com.myproject.ww;

import com.myproject.tools.CustomQueryBuilder;
import com.myproject.util.FilterBDU;
import com.myproject.util.FilterType;
import com.myproject.util.GroupsBDU;
import com.myproject.util.QueryType;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.*;

/**
 * Created by wei.wang on 2018/1/10.
 */
public class BDUExtend {

  public static void main(String[] args){
    Map<String, FilterBDU> map = new HashMap<String, FilterBDU>();
    List<GroupsBDU> list = new LinkedList<GroupsBDU>();
    list.add(new GroupsBDU(FilterType.Must,"a1"));
    list.add(new GroupsBDU(FilterType.Probably,"a4"));
    list.add(new GroupsBDU(FilterType.MustNot,"a3"));
    list.add(new GroupsBDU(FilterType.MustNot,"a5"));

    List<GroupsBDU> list1 = new LinkedList<GroupsBDU>();
    list1.add(new GroupsBDU(FilterType.Must,"a1"));
    list1.add(new GroupsBDU(FilterType.Probably,"a4"));
    list1.add(new GroupsBDU(FilterType.MustNot,"a3"));
    list1.add(new GroupsBDU(FilterType.MustNot,"a5"));


    List<GroupsBDU> list2 = new LinkedList<GroupsBDU>();
    list2.add(new GroupsBDU(FilterType.Must,"a11"));
    list2.add(new GroupsBDU(FilterType.Probably,"a2"));
    list2.add(new GroupsBDU(FilterType.MustNot,"a3"));
    list2.add(new GroupsBDU(FilterType.MustNot,"a5"));

    List<GroupsBDU> list3 = new LinkedList<GroupsBDU>();
    list3.add(new GroupsBDU(FilterType.Must,"a11"));
    list3.add(new GroupsBDU(FilterType.Probably,"a2"));
    list3.add(new GroupsBDU(FilterType.MustNot,"a3"));
    list3.add(new GroupsBDU(FilterType.MustNot,"a5"));


    FilterBDU filterBDU1 = new FilterBDU(FilterType.Must, QueryType.Term,"meta_案由","aa",list);
    FilterBDU filterBDU2 = new FilterBDU(FilterType.Must, QueryType.Term,"meta_案由1","bb",list1);
    FilterBDU filterBDU3 = new FilterBDU(FilterType.Must, QueryType.Term,"meta_案由2","cc",list2);
    FilterBDU filterBDU4 = new FilterBDU(FilterType.Must, QueryType.Term,"meta_案由3","dd",list3);
    map.put("a1",filterBDU1);
    map.put("a2",filterBDU2);
    map.put("a3",filterBDU3);
    map.put("a4",filterBDU4);


    FilterBDU filterBDU = new FilterBDU(FilterType.Must,QueryType.Term,"meta_法院名称","安徽省高级人民法院");
    map.put("a5",filterBDU);
    QueryBuilder builder = CustomQueryBuilder.boolQuery(map);
    System.out.println(builder.toString());
  }

}
