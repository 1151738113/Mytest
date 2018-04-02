package com.myproject.ww;

import com.myproject.tools.CustomQueryBuilder;
import com.myproject.util.FilterBDU;
import com.myproject.util.FilterType;
import com.myproject.util.QueryType;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wei.wang on 2017/10/31.
 */
public class Demo01 {
  @Test
   public  void ww(){
    Map<String, FilterBDU> map = new HashMap<String, FilterBDU>();
    FilterBDU filterBDU = new FilterBDU(FilterType.Probably, QueryType.Term, "meta_人物信息.peopleAttrMap.meta_案由", "aa","1");
    filterBDU.setIsnestedfield(true);
    map.put("1",filterBDU);
    filterBDU =  new FilterBDU(FilterType.Probably, QueryType.Term, "meta_人物信息.peopleAttrMap.meta_案由", "bb","1");
    filterBDU.setIsnestedfield(true);
    map.put("2",filterBDU);
    filterBDU =  new FilterBDU(FilterType.Must, QueryType.Term, "meta_人物信息.peopleAttrMap.meta_案号", "xx","");
    filterBDU.setIsnestedfield(true);
    map.put("7",filterBDU);
    filterBDU = new FilterBDU(FilterType.Probably, QueryType.Term, "meta_人物信息.peopleAttrMap.meta_案由1", "cc","2");
    filterBDU.setIsnestedfield(true);
    map.put("3",filterBDU);
    filterBDU = new FilterBDU(FilterType.Probably, QueryType.Term, "meta_人物信息.peopleAttrMap.meta_案由1", "dd","2");
    filterBDU.setIsnestedfield(true);
    map.put("4",filterBDU);
    map.put("5",new FilterBDU(FilterType.Must,QueryType.Term,"meta_案由","盗窃罪"));
//    map.put("1",new FilterBDU(FilterType.Probably, QueryType.Term, "meta_案由", "aa","1"));
//    map.put("2",new FilterBDU(FilterType.Probably, QueryType.Term, "meta_案由", "bb","1"));
//    map.put("3",new FilterBDU(FilterType.Probably, QueryType.Term, "meta_案由", "cc","2"));
//    map.put("4",new FilterBDU(FilterType.Probably, QueryType.Term, "meta_案由", "dd","2"));
    QueryBuilder builder = CustomQueryBuilder.boolQuery(map);
System.out.println(builder.toString());

  }

}
