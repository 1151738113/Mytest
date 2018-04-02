package com.myproject.ww;

import com.myproject.tools.CustomQueryBuilder;
import com.myproject.util.FilterBDU;
import com.myproject.util.FilterType;
import com.myproject.util.GroupsBDU;
import com.myproject.util.QueryType;
import org.elasticsearch.index.query.QueryBuilder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by wei.wang on 2018/1/12.
 */
public class ArrestTest {

  public static void main(String[] args){
    Map<String,FilterBDU> input = new HashMap<String, FilterBDU>();
    FilterBDU bb1 =
        new FilterBDU(FilterType.Must, QueryType.Term, "meta_人物信息.peopleAttrMap.info_判罚类型", "无罪");
    bb1.setIsnestedfield(true);
    List<GroupsBDU> list = new LinkedList<GroupsBDU>();
    list.add(new GroupsBDU(FilterType.Must,"捕后无罪"));
    list.add(new GroupsBDU(FilterType.Probably,"a"));
    bb1.setInfo_group_bool(list);
    input.put("无罪", bb1);
    FilterBDU bb =
        new FilterBDU(FilterType.Must, QueryType.Term, "meta_人物信息.peopleAttrMap.info_建议批准逮捕", true);
    bb.setIsnestedfield(true);
    List<GroupsBDU> list1 = new LinkedList<GroupsBDU>();
    list1.add(new GroupsBDU(FilterType.Must,"捕后无罪"));
    list1.add(new GroupsBDU(FilterType.Probably,"a"));
    bb.setInfo_group_bool(list1);
    input.put("建议逮捕", bb);

    FilterBDU bb3 = new FilterBDU(FilterType.Must, QueryType.Term, "meta_判决类型", "撤案函");
    List<GroupsBDU> list2 = new LinkedList<GroupsBDU>();
    list2.add(new GroupsBDU(FilterType.Must,"捕后撤案"));
    list2.add(new GroupsBDU(FilterType.Probably,"a"));
    bb3.setInfo_group_bool(list2);
    input.put("文书类型", bb3);
    FilterBDU bb2 =
        new FilterBDU(FilterType.Must, QueryType.Term, "meta_人物信息.peopleAttrMap.info_建议批准逮捕", true);
    bb2.setIsnestedfield(true);
    List<GroupsBDU> list3 = new LinkedList<GroupsBDU>();
    list3.add(new GroupsBDU(FilterType.Must,"捕后撤案"));
    list3.add(new GroupsBDU(FilterType.Probably,"a"));
    bb2.setInfo_group_bool(list3);
    input.put("建议逮捕1", bb2);

    QueryBuilder builder = CustomQueryBuilder.boolQuery(input);
    System.out.println(builder.toString());




  }
}
