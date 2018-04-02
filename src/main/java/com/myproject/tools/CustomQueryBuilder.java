package com.myproject.tools;


import com.myproject.util.*;
import org.elasticsearch.index.query.*;

import java.lang.reflect.Array;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

/**
 * Created by gengw on 10/27/16.
 */

public class CustomQueryBuilder {


  /**
   * 日志记录组件
   *
   * @return logger
   */
  //    private static Logger logger = LoggerFactory.getLogger(CustomQueryBuilder.class);

  public static List<String> highlightfield;
  public static List<String> keywordList;
  public static BoolQueryBuilder myfilters;


  /**
   * boolean query and 条件组合查询
   *
   * @return QueryBuilder
   */


  public static QueryBuilder boolQuery(Map<String,FilterBDU> filters){
    filters.put("meta_案号",new FilterBDU(FilterType.Must,QueryType.Exist,"meta_案号"));
    filters.put("meta_判决类型1",new FilterBDU(FilterType.MustNot,QueryType.Term,"meta_判决类型","调解书"));
    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    boolQuery2(boolQueryBuilder,filters);
    return boolQueryBuilder;
  }
  public static QueryBuilder boolQuery1(Map<String,FilterBDU> filters){
    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    boolQuery2(boolQueryBuilder,filters);
    return boolQueryBuilder;
  }


  public static QueryBuilder boolQuery2(BoolQueryBuilder boolQueryBuilder,Map<String, FilterBDU> filters) {

//       boolQueryBuilder = new BoolQueryBuilder();


    //        boolQueryBuilder.filter(QueryBuilders.matchQuery("meta_全文",fulltext));
    //        logger.error(JSON.toJSONString(filters,true));
    //        boolQueryBuilder.must(QueryBuilders.existsQuery("meta_案号").queryName("meta_案号"));//.boost(0.0f);
    ////        boolQueryBuilder.must(QueryBuilders.missingQuery("meta_级别").queryName("meta_级别"));
    //        boolQueryBuilder.mustNot(QueryBuilders.termQuery("meta_判决类型", "调解书"));//.boost(0.0f));//.boost(0.0f);
    Map<String, BoolQueryBuilder> groupsMap = new HashMap<String, BoolQueryBuilder>();
    myfilters = new BoolQueryBuilder();
    //        boolQueryBuilder.must(QueryBuilders.e)
    //        BoolQueryBuilder boolgroupBuilder=null;
    //        Set<String> set = new HashSet<String>();
    Map<String, Set<FilterBDU>> nestedfilter = new HashMap<String, Set<FilterBDU>>();
    List<FilterBDU> groupCollect = new ArrayList<FilterBDU>();
    highlightfield = new Vector<String>();
    keywordList = new Vector<String>();
    int shouldNum = 0, nestedNum = 0, mustNum = 0, groupSize = 0;
    for (Map.Entry<String, FilterBDU> entry : filters.entrySet()) {
      FilterBDU bdu = entry.getValue();
      if (bdu.isnestedfield() && bdu.getInfo_group_bool() == null) {
        String info_point = bdu.getInfo_point();
        if (bdu.getFiltertype().equals(FilterType.Probably))
          nestedNum++;
        String nestedPath = info_point.substring(0, info_point.lastIndexOf("."));
        if (bdu.getFiltertype().equals(FilterType.Must)) {
          mustNum++;
          if (nestedPath.equals("meta_人物信息.peopleAttrMap")) {
            if (bdu.getQuerytype().equals(QueryType.Term)) {
              QueryBuilder queryBuilder =
                  QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val())
                      .boost(bdu.getInfo_point_weight());
              myfilters.must(queryBuilder);

            } else if (bdu.getQuerytype().equals(QueryType.Range)) {
              RangeQueryBuilder rangeQueryBuilder = GetRangeQueryBuilder(bdu);
              myfilters.must(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
            }
          }

        } else if (bdu.getFiltertype().equals(FilterType.MustNot)) {
          if (nestedPath.equals("meta_人物信息.peopleAttrMap")) {
            if (bdu.getQuerytype().equals(QueryType.Term)) {
              QueryBuilder queryBuilder =
                  QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val())
                      .boost(bdu.getInfo_point_weight());
              myfilters.mustNot(queryBuilder);

            }
          }
        }
        if (!nestedfilter.containsKey(nestedPath))
          nestedfilter.put(nestedPath, new HashSet<FilterBDU>());
        nestedfilter.get(nestedPath).add(bdu);

      } else if (!bdu.isnestedfield() && bdu.getInfo_group_bool() == null) {
        String groups = bdu.getInfo_group();
        if (bdu.getFiltertype().equals(FilterType.Probably))
          shouldNum++;
        if (groups.equals("")) {
          AddGroupFilter(boolQueryBuilder, bdu);
        } else {
          try {
            if (!groupsMap.containsKey(groups)) {
              BoolQueryBuilder boolgroupBuilder = new BoolQueryBuilder();
              boolgroupBuilder.minimumShouldMatch("1");
              groupsMap.put(groups, boolgroupBuilder);
              AddGroupFilter(boolgroupBuilder, bdu);
              boolQueryBuilder.must(boolgroupBuilder);


              //                            logger.info("group:"+boolgroupBuilder);
            } else {
              BoolQueryBuilder boolgroupBuilder = groupsMap.get(groups);
              AddGroupFilter(boolgroupBuilder, bdu);
              //                            logger.info("group:"+boolgroupBuilder);
            }
          } catch (Exception ex) {
            //                        logger.error("boolQuery:" + ex.getMessage());
          }
        }
      } else if (bdu.getInfo_group_bool().size() > 0) {
        if (bdu.getInfo_group_bool().size() > groupSize) {
          groupSize = bdu.getInfo_group_bool().size();
        }
        groupCollect.add(bdu);
      }
    }
    if (nestedfilter.size() > 0) {
      AddNestedFilter(boolQueryBuilder, nestedfilter, mustNum);
      int shouldCnt = Math.round((nestedNum) / 2);
      boolQueryBuilder.minimumShouldMatch(String.valueOf(shouldCnt));
      //            if(mustNum<2)
      //                boolQueryBuilder.must(nestedQuery("meta_人物信息.peopleAttrMap", QueryBuilders.boolQuery().must(QueryBuilders.existsQuery("meta_人物信息.peopleAttrMap.info_主刑罪名").queryName("meta_人物信息.peopleAttrMap.info_主刑罪名"))));
    }
    {
      //            boolQueryBuilder.minimumShouldMatch(String.valueOf(Math.round(shouldNum/2)));
    }
    //        logger.info("boolQueryBuilder:"+boolQueryBuilder);

    //扩展方案
    //保存完整的map
    Map<String, List<Object>> map;
    //初始化map，将group转换成一个树状的结构，构建成一个map树，key为组名，value为一个相同组的map，依次递归下去
    Map<String, Object> map1 = new HashMap<String, Object>();
    for (int i = groupSize - 1; i >= 0; i--) {
      if (i == groupSize - 1) {
        for (FilterBDU filterBDU : groupCollect) {
          List<GroupsBDU> list = filterBDU.getInfo_group_bool();
          GroupsBDU groupsBDU = list.get(i);
          if (!map1.containsKey(groupsBDU.getGroups())) {
            map1.put(groupsBDU.getGroups(), new ArrayList<Object>());
            List ll = (List) map1.get(groupsBDU.getGroups());
            ll.add(filterBDU);
          } else {
            List ll = (List) map1.get(groupsBDU.getGroups());
            ll.add(filterBDU);
          }
        }
        continue;
      }
      ww(map1, i);
    }

    //解析map，将业务逻辑转换成ES的查询逻辑
    //创建一个组合过滤类型的映射表
    Map<String, FilterType> map2 = new HashMap<String, FilterType>();
    for (FilterBDU filterBDU : groupCollect) {
      List<GroupsBDU> list = filterBDU.getInfo_group_bool();
      for (GroupsBDU groupsBDU : list) {
        if (map2.containsKey(groupsBDU.getGroups())) {
          if (!map2.get(groupsBDU.getGroups()).equals(groupsBDU.getFilterType())) {
            System.out.println("分组错误！" + filterBDU.toString());
          }
          continue;
        }
        map2.put(groupsBDU.getGroups(), groupsBDU.getFilterType());
      }
    }
    esMapAnalysis(boolQueryBuilder,map1, map2,FilterType.Must);
    return boolQueryBuilder;
  }


  //递归解析map
  public static void ww(Map<String, Object> map, int i) {
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (entry.getValue() instanceof List) {
        List<FilterBDU> list = (List<FilterBDU>) entry.getValue();
        Map<String, List<FilterBDU>> map1 = new HashMap<String, List<FilterBDU>>();
        for (FilterBDU filterBDU : list) {
          List<GroupsBDU> groupsBDUS = filterBDU.getInfo_group_bool();
          GroupsBDU groupsBDU = groupsBDUS.get(i);
          if (!map1.containsKey(groupsBDU.getGroups())) {
            map1.put(groupsBDU.getGroups(), new ArrayList<FilterBDU>());
            map1.get(groupsBDU.getGroups()).add(filterBDU);
          } else {
            map1.get(groupsBDU.getGroups()).add(filterBDU);
          }
        }
        map.put(entry.getKey(), map1);
      } else {
        ww((Map<String, Object>) entry.getValue(), i);
      }

    }
  }

  //解析map
  private static void esMapAnalysis(BoolQueryBuilder boolQueryBuilders,Map<String, Object> map,
      Map<String, FilterType> map2,FilterType filterType) {
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (entry.getValue() instanceof List) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        List<FilterBDU> list = (List<FilterBDU>) entry.getValue();
        //用于存储嵌套条件
        Map<FilterBDU,QueryBuilder> nested = new HashMap<FilterBDU, QueryBuilder>();
        for (FilterBDU filterBDU : list) {
          if(filterBDU.isnestedfield()){
            QueryBuilder builder = getQuery(filterBDU);
            nested.put(filterBDU,builder);
          }else {
            creatBuild(filterBDU, boolQueryBuilder);
          }
        }
        //单独处理嵌套类型
        if(nested.size()>0){
          BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
          for(Map.Entry<FilterBDU,QueryBuilder> entry1 : nested.entrySet()){
            FilterBDU key = entry1.getKey();
            QueryBuilder builder = entry1.getValue();
            if(FilterType.Must.equals(key.getFiltertype())){
              boolQueryBuilder1.must(builder);
            }else if(FilterType.MustNot.equals(key.getFiltertype())){
              boolQueryBuilder1.mustNot(builder);
            }else if(FilterType.Probably.equals(key.getFiltertype())){
              boolQueryBuilder1.should(builder);
            }
          }
          NestedQueryBuilder nestedQueryBuilder = nestedQuery("meta_人物信息.peopleAttrMap",boolQueryBuilder1);
          boolQueryBuilder.must(nestedQueryBuilder);
        }
      filterCompare(boolQueryBuilders, boolQueryBuilder, filterType);
      } else if (entry.getValue() instanceof Map) {
        String key = entry.getKey();
        Map<String, Object> map1 = (Map) entry.getValue();
        //获得该组的过滤类型
        if (map2.containsKey(key)) {
          FilterType filterType1 = map2.get(key);
          BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
          filterCompare(boolQueryBuilders, boolQueryBuilder, filterType);
          esMapAnalysis(boolQueryBuilder,(Map)entry.getValue(),map2,filterType1);
        }
      }
    }
  }


  //解析filterbdu集合
  private static void creatBuild(FilterBDU filterBDU, BoolQueryBuilder boolQueryBuilder) {
    QueryBuilder builder = getQuery(filterBDU);
    if (FilterType.Must.equals(filterBDU.getFiltertype())) {
      boolQueryBuilder.must(builder);
    } else if (FilterType.MustNot.equals(filterBDU.getFiltertype())) {
      boolQueryBuilder.mustNot(builder);
    } else if (FilterType.Probably.equals(filterBDU.getFiltertype())) {
      boolQueryBuilder.should(builder);
    }
  }

  private static QueryBuilder getQuery(FilterBDU filterBDU) {
    if (QueryType.Term.equals(filterBDU.getQuerytype())) {
//      if (!filterBDU.isnestedfield()) {
        QueryBuilder builder =
            QueryBuilders.termQuery(filterBDU.getInfo_point(), filterBDU.getInfo_point_val())
                .boost(filterBDU.getInfo_point_weight());
        return builder;
//      }
//      else {
//        NestedQueryBuilder nested = nestedQuery("meta_人物信息.peopleAttrMap",
//            QueryBuilders.termQuery(filterBDU.getInfo_point(), filterBDU.getInfo_point_val())
//                .boost(filterBDU.getInfo_point_weight()));
//        return nested;
//      }
    } else if (QueryType.Match.equals(filterBDU.getQuerytype())) {
      if (filterBDU.isphrase()) {
//        if (filterBDU.isnestedfield()) {
//          NestedQueryBuilder builder = nestedQuery("meta_人物信息.peopleAttrMap", QueryBuilders
//              .matchPhraseQuery(filterBDU.getInfo_point(), filterBDU.getInfo_point_val())
//              .boost(filterBDU.getInfo_point_weight()).minimumShouldMatch("100%"));
//          return builder;
//        } else {
          QueryBuilder builder = QueryBuilders
              .matchPhraseQuery(filterBDU.getInfo_point(), filterBDU.getInfo_point_val())
              .boost(filterBDU.getInfo_point_weight()).minimumShouldMatch("100%");
          return builder;
//        }
      } else {
//        if (filterBDU.isnestedfield()) {
//          NestedQueryBuilder builder = nestedQuery("meta_人物信息.peopleAttrMap", QueryBuilders
//              .multiMatchQuery(filterBDU.getInfo_point_val(), filterBDU.getInfo_point(),
//                  filterBDU.getInfo_point() + ".bigrm^10")
//              .type(MultiMatchQueryBuilder.Type.BEST_FIELDS).boost(filterBDU.getInfo_point_weight())
//              .minimumShouldMatch("100%"));
//          return builder;
//        } else {
          QueryBuilder queryBuilder = QueryBuilders
              .multiMatchQuery(filterBDU.getInfo_point_val(), filterBDU.getInfo_point(),
                  filterBDU.getInfo_point() + ".bigrm^10")
              .type(MultiMatchQueryBuilder.Type.BEST_FIELDS).boost(filterBDU.getInfo_point_weight())
              .minimumShouldMatch("100%");
          return queryBuilder;
//        }
      }
    }
    return new BoolQueryBuilder();
  }

  //嵌套类型聚合
  private void nestedBuilder(FilterBDU filterBDU,BoolQueryBuilder bool){



  }




  private static void filterCompare(BoolQueryBuilder boolQueryBuilder,
      BoolQueryBuilder boolQueryBuilder1, FilterType filterType) {
    if (FilterType.Must.equals(filterType)) {
      boolQueryBuilder.must(boolQueryBuilder1);
    } else if (FilterType.MustNot.equals(filterType)) {
      boolQueryBuilder.mustNot(boolQueryBuilder1);
    } else if (FilterType.Probably.equals(filterType)) {
      boolQueryBuilder.should(boolQueryBuilder1);
    }
  }


  //如果存在这种嵌套集合则做处理
  //        if(groupCollect.size()>0) {
  //          //用于保存组名和分组信息
  //          Map<String, Set<FilterBDU>> map = new HashMap<String, Set<FilterBDU>>();
  //          BoolQueryBuilder boolQueryBuilder1 = new BoolQueryBuilder();
  //          //对第一层进行分组，将相同分组的放入一个bool内
  //          //第一层用于处理基本的数据表现形式
  //          for (FilterBDU filterBDU : groupCollect) {
  //            List<GroupsBDU> list = filterBDU.getInfo_group_bool();
  //            if(list.size()>=1){
  //              if(map.containsKey(list.get(0).getGroups())){
  //                map.get(list.get(0).getGroups()).add(filterBDU);
  //              }else{
  //                map.put(list.get(0).getGroups(),new HashSet<FilterBDU>());
  //                map.get(list.get(0).getGroups()).add(filterBDU);
  //              }
  //            }
  //          }
  //          //mapbool中存储的是第一层，key:第一层组名  value: 对应的boolbuilder
  //       Map<String,BoolQueryBuilder> mapBool = new HashMap<String,BoolQueryBuilder>();
  //       for(Map.Entry<String, Set<FilterBDU>> entry : map.entrySet()){
  //         BoolQueryBuilder boolQuery1 = new BoolQueryBuilder();
  //         BoolQueryBuilder bool_nested = new BoolQueryBuilder();
  //         String groupsBDU = entry.getKey();
  //           //获取该组的bool类型
  //           Set<FilterBDU> set = entry.getValue();
  //           for(FilterBDU filter:set){
  //             if(filter.isnestedfield()) {
  //               AddNestedGroupFilter_new(bool_nested, filter, "meta_人物信息.peopleAttrMap");
  //             }else{
  //               AddGroupFilter(boolQuery1,filter);
  //             }
  //           }
  //         boolQuery1.must(nestedQuery("meta_人物信息.peopleAttrMap",bool_nested));
  //         mapBool.put(groupsBDU,boolQuery1);
  //          }
  //          Map<String,BoolQueryBuilder> result = createBool(mapBool,groupSize,groupCollect);
  //          for(Map.Entry<String,BoolQueryBuilder> entry : result.entrySet()){
  //            BoolQueryBuilder boolQueryBuilder2 = entry.getValue();
  //            boolQueryBuilder.must(boolQueryBuilder2);
  //          }
  //        }



  //  /**
  //   * 组装bool
  //   */
  //  /**
  //   * 通过递归的方式调用
  //   */
  //
  //
  //
  //
  //  public static Map<String,BoolQueryBuilder> createBool( Map<String,BoolQueryBuilder> map2, int groupSize,List<FilterBDU> groupCollect){
  //    for(int i=1;i<groupSize;i++){
  //      //用于存储每层组 和 bool
  //      Map<String,List<BoolQueryBuilder>> map = new HashMap<String, List<BoolQueryBuilder>>();
  //      //上层的group和bool映射
  //      //从第二层开始
  //      for(Map.Entry<String,BoolQueryBuilder> entry : map2.entrySet()){
  //        String groupsBDU = entry.getKey();
  //        BoolQueryBuilder boolQueryBuilder1 = entry.getValue();
  //        for(FilterBDU filterBDU : groupCollect){
  //          List<GroupsBDU> list = filterBDU.getInfo_group_bool();
  //          if(groupsBDU.equals(list.get(i-1).getGroups())){
  //            if(list.size()>i-1){
  //              GroupsBDU groupsBDU1 = list.get(i);
  //              if(!map.containsKey(groupsBDU1.getGroups())){
  //                map.put(groupsBDU1.getGroups(),new ArrayList<BoolQueryBuilder>());
  //                map.get(groupsBDU1.getGroups()).add(boolQueryBuilder1);
  //              }else{
  //                map.get(groupsBDU1.getGroups()).add(boolQueryBuilder1);
  //              }
  //            }
  //          }
  //        }
  //      }
  //      //获得组和bool的映射表
  //      map2 = new HashMap<String, BoolQueryBuilder>();
  //      for(Map.Entry<String,List<BoolQueryBuilder>> entry : map.entrySet()){
  //        BoolQueryBuilder boolQueryBuilder2 = new BoolQueryBuilder();
  //        String groupsBDU = entry.getKey();
  //        //根据组名获取该过滤类型
  //        FilterType filterType = null;
  //        for(FilterBDU groupsBDU1 : groupCollect){
  //          List<GroupsBDU> list = groupsBDU1.getInfo_group_bool();
  //          String string = list.get(i).getGroups();
  //          if(groupsBDU.equals(string)){
  //            filterType = list.get(i).getFilterType();
  //            break;
  //          }
  //        }
  //        if(filterType == null){
  //          continue;
  //        }
  //        List<BoolQueryBuilder> list = entry.getValue();
  //        if(FilterType.Must.equals(filterType)){
  //          for(BoolQueryBuilder boolQueryBuilder3 : list){
  //            boolQueryBuilder2.must(boolQueryBuilder3);
  //          }
  //        }else if(FilterType.MustNot.equals(filterType)){
  //          for(BoolQueryBuilder boolQueryBuilder3 : list){
  //            boolQueryBuilder2.mustNot(boolQueryBuilder3);
  //          }
  //        }else if(FilterType.Probably.equals(filterType)){
  //          for(BoolQueryBuilder boolQueryBuilder3 : list){
  //            boolQueryBuilder2.should(boolQueryBuilder3);
  //          }
  //        }
  //        map2.put(groupsBDU,boolQueryBuilder2);
  //      }
  //    }
  //    return map2;
  //  }



  /**
   * 用于将相同组的相同的过滤逻辑的条件放入一个bool里
   */
  public BoolQueryBuilder nestedAdd(FilterBDU filterBDU) {
    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    if (FilterType.Must.equals(filterBDU.getFiltertype())) {

    } else if (FilterType.MustNot.equals(filterBDU.getFiltertype())) {

    } else if (FilterType.Probably.equals(filterBDU.getFiltertype())) {

    }
    return boolQueryBuilder;
  }

  /**
   * boolean AddNestedFilter 添加nested组查询条件
   *
   * @return
   */
  private static void AddNestedFilter(BoolQueryBuilder boolQueryBuilder,
      Map<String, Set<FilterBDU>> nestedfilter, int mustNum) {
    Map<String, BoolQueryBuilder> groupsMap = new HashMap<String, BoolQueryBuilder>();
    Map<String, BoolQueryBuilder> nestedgroupsMap = new HashMap<String, BoolQueryBuilder>();
    boolean isadd = false;
    for (Map.Entry<String, Set<FilterBDU>> entry : nestedfilter.entrySet()) {
      BoolQueryBuilder nestboolBuilder = new BoolQueryBuilder();
      Set<FilterBDU> listnested = entry.getValue();
      for (FilterBDU obj : listnested) {
        //                logger.info("AddNestedFilter FilterBDU:" + obj);

        //
        if (obj.getFiltertype().equals(FilterType.Probably)) {
          if (obj.getQuerytype().equals(QueryType.Term)) {
            boolQueryBuilder.should(nestedQuery(entry.getKey(), QueryBuilders.boolQuery().should(
                QueryBuilders.termQuery(obj.getInfo_point(), obj.getInfo_point_val())
                    .boost(obj.getInfo_point_weight()))));
          } else if (obj.getQuerytype().equals(QueryType.NotExist)) {
            boolQueryBuilder.should(QueryBuilders.notQuery(nestedQuery(entry.getKey(),
                QueryBuilders.boolQuery().must(QueryBuilders.existsQuery(obj.getInfo_point())
                    .queryName(obj.getInfo_point())))));
          }
        } else if (obj.getFiltertype().equals(FilterType.Must)) {
          String groups = obj.getInfo_group();
          if (!groupsMap.containsKey(groups)) {
            BoolQueryBuilder boolgroupBuilder = new BoolQueryBuilder();
            //                        boolgroupBuilder.minimumShouldMatch("50%");
            groupsMap.put(groups, boolgroupBuilder);
            AddNestedGroupFilter(boolgroupBuilder, obj, entry.getKey());
            boolQueryBuilder.must(nestedQuery(entry.getKey(), boolgroupBuilder));

          } else {
            BoolQueryBuilder boolgroupBuilder = groupsMap.get(groups);
            AddNestedGroupFilter(boolgroupBuilder, obj, entry.getKey());
          }
        } else if (obj.getFiltertype().equals(FilterType.MustNot)) {

          String groups = obj.getInfo_group();
          if (!groupsMap.containsKey(groups)) {
            BoolQueryBuilder boolgroupBuilder = new BoolQueryBuilder();
            //                        boolgroupBuilder.minimumShouldMatch("50%");
            groupsMap.put(groups, boolgroupBuilder);
            AddNestedGroupFilter(boolgroupBuilder, obj, entry.getKey());
            boolQueryBuilder.must(nestedQuery(entry.getKey(), boolgroupBuilder));
          } else {
            BoolQueryBuilder boolgroupBuilder = groupsMap.get(groups);
            AddNestedGroupFilter(boolgroupBuilder, obj, entry.getKey());
          }
          if (mustNum < 2 && !isadd) {
            isadd = true;
            BoolQueryBuilder boolgroupBuilder = groupsMap.get(groups);
            boolgroupBuilder.must(QueryBuilders.existsQuery("meta_人物信息.peopleAttrMap.info_主刑罪名")
                .queryName("meta_人物信息.peopleAttrMap.info_主刑罪名"));
          }
        }
      }

      //            boolQueryBuilder.must(nestedQuery(entry.getKey(),nestboolBuilder));
    }
  }

  /**
   * boolean AddNestedGroupFilter  增加嵌套主从条件AddNestedGroupFilter
   *
   * @return void
   */
  private static void AddNestedGroupFilter(BoolQueryBuilder boolgroupBuilder, FilterBDU bdu,
      String nestedPath) {

    try {

      if (bdu.getFiltertype().equals(FilterType.Probably)) {
        AddNestedProbablyFilter(boolgroupBuilder, bdu, nestedPath);
      } else if (bdu.getFiltertype().equals(FilterType.Must)) {
        AddNestedMustFilter(boolgroupBuilder, bdu, nestedPath);
      } else if (bdu.getFiltertype().equals(FilterType.MustNot)) {
        AddNestedMustNotFilter(boolgroupBuilder, bdu, nestedPath);
      }
    } catch (Exception ex) {
      //            logger.error("AddNestedGroupFilter:"+ex.getMessage());
    }
  }

  private static void AddNestedGroupFilter_new(BoolQueryBuilder boolgroupBuilder, FilterBDU bdu,
      String nestedPath) {

    try {

      if (bdu.getInfo_group_bool().get(0).getFilterType().equals(FilterType.Probably)) {
        AddNestedProbablyFilter(boolgroupBuilder, bdu, nestedPath);
      } else if (bdu.getInfo_group_bool().get(0).getFilterType().equals(FilterType.Must)) {
        AddNestedMustFilter(boolgroupBuilder, bdu, nestedPath);
      } else if (bdu.getInfo_group_bool().get(0).getFilterType().equals(FilterType.MustNot)) {
        AddNestedMustNotFilter(boolgroupBuilder, bdu, nestedPath);
      }
    } catch (Exception ex) {
      //            logger.error("AddNestedGroupFilter:"+ex.getMessage());
    }
  }

  /**
   * boolean AddNestedMustFilter  Must嵌套主从条件AddNestedMustFilter
   *
   * @return void
   */
  private static void AddNestedMustFilter(BoolQueryBuilder boolgroupBuilder, FilterBDU bdu,
      String nestedPath) {
    //        BoolQueryBuilder nestboolBuilder = new BoolQueryBuilder();
    if (bdu.getQuerytype().equals(QueryType.Term)) {
      boolgroupBuilder.must(QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val())
          .boost(bdu.getInfo_point_weight()));
    } else if (bdu.getQuerytype().equals(QueryType.Range)) {
      RangeQueryBuilder rangeQueryBuilder = GetRangeQueryBuilder(bdu);
      boolgroupBuilder.must(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
    } else if (bdu.getQuerytype().equals(QueryType.NotExist)) {
      boolgroupBuilder.must(
          QueryBuilders.missingQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point())
              .existence(true).nullValue(true));
    }

  }

  /**
   * boolean AddNestedProbablyFilter  Probably嵌套主从条件AddNestedProbablyFilter
   *
   * @return void
   */
  private static void AddNestedProbablyFilter(BoolQueryBuilder boolgroupBuilder, FilterBDU bdu,
      String nestedPath) {
    //        BoolQueryBuilder nestboolBuilder = new BoolQueryBuilder();
    if (bdu.getQuerytype().equals(QueryType.Term)) {
      boolgroupBuilder.should(QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val())
          .boost(bdu.getInfo_point_weight()));
    } else if (bdu.getQuerytype().equals(QueryType.Range)) {
      RangeQueryBuilder rangeQueryBuilder = GetRangeQueryBuilder(bdu);
      boolgroupBuilder.should(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
    } else if (bdu.getQuerytype().equals(QueryType.NotExist)) {
      boolgroupBuilder
          .should(QueryBuilders.existsQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));
    }
  }

  /**
   * boolean AddNestedMustNotFilter  MustNot嵌套主从条件AddNestedMustNotFilter
   *
   * @return void
   */
  private static void AddNestedMustNotFilter(BoolQueryBuilder boolgroupBuilder, FilterBDU bdu,
      String nestedPath) {
    //        BoolQueryBuilder nestboolBuilder = new BoolQueryBuilder();
    if (bdu.getQuerytype().equals(QueryType.Term)) {
      boolgroupBuilder
          .mustNot(QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val()))
          .boost(bdu.getInfo_point_weight());
    } else if (bdu.getQuerytype().equals(QueryType.Range)) {
      RangeQueryBuilder rangeQueryBuilder = GetRangeQueryBuilder(bdu);
      boolgroupBuilder.mustNot(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
    }
    //        return nestboolBuilder;
    //        boolQueryBuilder.must(nestedQuery(nestedPath,nestboolBuilder));
  }

  /**
   * boolean AddGroupFilter 添加组查询条件
   *
   * @return
   */
  private static void AddGroupFilter(BoolQueryBuilder boolQueryBuilder, FilterBDU bdu) {
    try {
      if (bdu.getFiltertype().equals(FilterType.Must)) {
        AddMustFilter(boolQueryBuilder, bdu);
      }
      if (bdu.getFiltertype().equals(FilterType.Probably)) {
        AddProbablyFilter(boolQueryBuilder, bdu);
      }
      if (bdu.getFiltertype().equals(FilterType.MustNot)) {
        AddMustNotFilter(boolQueryBuilder, bdu);
      }
    } catch (Exception ex) {
      //            logger.error("AddGroupFilter:"+ex.getMessage());
    }
  }

  private static void AddGroupFilters(BoolQueryBuilder boolQueryBuiild_father,
      BoolQueryBuilder boolQueryBuilder, GroupsBDU bdu) {
    try {
      if (bdu.getFilterType().equals(FilterType.Must)) {
        boolQueryBuiild_father.must(boolQueryBuilder);
      }
      if (bdu.getFilterType().equals(FilterType.Probably)) {
        boolQueryBuiild_father.should(boolQueryBuilder);
      }
      if (bdu.getFilterType().equals(FilterType.MustNot)) {
        boolQueryBuiild_father.mustNot(boolQueryBuilder);
      }
    } catch (Exception ex) {
      //            logger.error("AddGroupFilter:"+ex.getMessage());
    }
  }

  /**
   * boolean RangeQueryBuilder  条件组合构造
   *
   * @return RangeQueryBuilder
   */
  private static RangeQueryBuilder GetRangeQueryBuilder(FilterBDU bdu) {
    RangeBDU rangeBDU = (RangeBDU) bdu.getInfo_point_val();
    RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(bdu.getInfo_point());
    rangeQueryBuilder.queryName(bdu.getInfo_point());
    if (rangeBDU.getFrom() != null) {
      rangeQueryBuilder.gte(rangeBDU.getFrom());
    }
    if (rangeBDU.getTo() != null) {
      rangeQueryBuilder.lt(rangeBDU.getTo());
    }

    return rangeQueryBuilder;
  }

  /**
   * void MultiList  对数组类型做处理
   *
   * @return void
   */
  @SuppressWarnings("rawtypes") private static void MultiList(BoolQueryBuilder boolQueryBuilder,
      FilterBDU bdu) {
    Object obj = bdu.getInfo_point_val();
    // 传入数值非数组时, 不作为检索条件
    if (!obj.getClass().isArray() && !(obj instanceof ArrayList)) {
      return;
    }

    if (obj instanceof ArrayList) {
      obj = ((ArrayList) obj).toArray();
    }

    int length = Array.getLength(obj);
    // 传入数组为空时, 不作为检索条件
    if (length == 0) {
      return;
    }

    // 传入数值转数组
    Object[] pointVals = new Object[length];
    for (int i = 0; i < length; i++) {
      pointVals[i] = (Array.get(obj, i));
    }

    // 构造ES的检索条件
    TermsQueryBuilder tqBuilder =
        QueryBuilders.termsQuery(bdu.getInfo_point(), pointVals).boost(bdu.getInfo_point_weight());

    if (bdu.getFiltertype().equals(FilterType.Must)) {
      boolQueryBuilder.must(tqBuilder);
    } else if (bdu.getFiltertype().equals(FilterType.Probably)) {
      boolQueryBuilder.should(tqBuilder);
    } else {
      boolQueryBuilder.mustNot(tqBuilder);
    }
  }

  /**
   * boolean AddMustFilter  AddMustFilter条件组合构造
   *
   * @return void
   */
  private static void AddMustFilter(BoolQueryBuilder boolQueryBuilder, FilterBDU bdu) {
    if (bdu.getQuerytype().equals(QueryType.Term)) {
      QueryBuilder queryBuilder =
          QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val())
              .boost(bdu.getInfo_point_weight());
      if (!bdu.isnestedfield()) {
        boolQueryBuilder.must(queryBuilder);
      }
    } else if (bdu.getQuerytype().equals(QueryType.List)) {
      MultiList(boolQueryBuilder, bdu);
      //            highlightfield.add(bdu.getInfo_point());

    } else if (bdu.getQuerytype().equals(QueryType.Range)) {
      RangeQueryBuilder rangeQueryBuilder = GetRangeQueryBuilder(bdu);
      boolQueryBuilder.must(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
    } else if (bdu.getQuerytype().equals(QueryType.Match)) {
      if (bdu.isphrase()) {
        keywordList.add(bdu.getInfo_point_val().toString());
        boolQueryBuilder.must(QueryBuilders
            .matchQuery(bdu.getInfo_point(), bdu.getInfo_point_val().toString().toLowerCase())
            .type(MatchQueryBuilder.Type.PHRASE).boost(bdu.getInfo_point_weight())
            .minimumShouldMatch("100%"));
      } else {
        boolQueryBuilder.must(QueryBuilders
            .multiMatchQuery(bdu.getInfo_point_val().toString().toLowerCase(), bdu.getInfo_point(),
                bdu.getInfo_point() + ".bigram^10").boost(bdu.getInfo_point_weight())
            .minimumShouldMatch("100%"));
      }
      highlightfield.add(bdu.getInfo_point());
    } else if (bdu.getQuerytype().equals(QueryType.MultiMatch)) {
      boolQueryBuilder.must(QueryBuilders
          .multiMatchQuery(bdu.getInfo_point_val().toString().toLowerCase(), bdu.getInfo_point(),
              bdu.getInfo_point() + ".bigram^10").boost(bdu.getInfo_point_weight())
          .minimumShouldMatch("100%"));
      highlightfield.add(bdu.getInfo_point());
    } else if (bdu.getQuerytype().equals(QueryType.NotExist)) {
      //                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
      boolQueryBuilder
          .must(QueryBuilders.missingQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));

    } else if (bdu.getQuerytype().equals(QueryType.Exist)) {
      //                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
      boolQueryBuilder
          .must(QueryBuilders.existsQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));

    } else if (bdu.getQuerytype().equals(QueryType.Prefix)) {
      //                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
      boolQueryBuilder
          .must(QueryBuilders.prefixQuery(bdu.getInfo_point(), bdu.getInfo_point_val().toString()));

    } else {
      boolQueryBuilder.must(QueryBuilders
          .wildcardQuery(bdu.getInfo_point(), "*" + bdu.getInfo_point_val().toString() + "*")
          .boost(bdu.getInfo_point_weight()));
      highlightfield.add(bdu.getInfo_point());

    }
  }


  /**
   * boolean AddProbablyFilter  AddProbablyFilter条件组合构造
   *
   * @return void
   */
  private static void AddProbablyFilter(BoolQueryBuilder boolQueryBuilder, FilterBDU bdu) {
    if (bdu.getQuerytype().equals(QueryType.Term)) {
      boolQueryBuilder.should(QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val())
          .boost(bdu.getInfo_point_weight()));
    } else if (bdu.getQuerytype().equals(QueryType.List)) {
      MultiList(boolQueryBuilder, bdu);
      //            highlightfield.add(bdu.getInfo_point());
      //            boolQueryBuilder.should(QueryBuilders.termsQuery(bdu.getInfo_point(), bdu.getInfo_point_val()).boost(bdu.getInfo_point_weight()));
    } else if (bdu.getQuerytype().equals(QueryType.Range)) {
      RangeQueryBuilder rangeQueryBuilder = GetRangeQueryBuilder(bdu);
      boolQueryBuilder.should(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
    } else if (bdu.getQuerytype().equals(QueryType.Match)) {
      highlightfield.add(bdu.getInfo_point());
      //                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
      if (bdu.isphrase()) {
        boolQueryBuilder.should(QueryBuilders
            .matchQuery(bdu.getInfo_point(), bdu.getInfo_point_val().toString().toLowerCase())
            .type(MatchQueryBuilder.Type.PHRASE).boost(bdu.getInfo_point_weight())
            .minimumShouldMatch("100%"));
      } else {
        boolQueryBuilder.should(QueryBuilders
            .multiMatchQuery(bdu.getInfo_point_val().toString().toLowerCase(), bdu.getInfo_point(),
                bdu.getInfo_point() + ".bigram^10").boost(bdu.getInfo_point_weight())
            .minimumShouldMatch("100%"));
      }
    } else if (bdu.getQuerytype().equals(QueryType.NotExist)) {
      //                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
      boolQueryBuilder
          .should(QueryBuilders.missingQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));

    } else if (bdu.getQuerytype().equals(QueryType.Exist)) {
      //                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
      boolQueryBuilder
          .should(QueryBuilders.existsQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));

    } else if (bdu.getQuerytype().equals(QueryType.Prefix)) {
      //                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
      boolQueryBuilder
          .must(QueryBuilders.prefixQuery(bdu.getInfo_point(), bdu.getInfo_point_val().toString()));

    } else {
      highlightfield.add(bdu.getInfo_point());
      boolQueryBuilder.should(QueryBuilders
          .wildcardQuery(bdu.getInfo_point(), "*" + bdu.getInfo_point_val().toString() + "*")
          .boost(bdu.getInfo_point_weight()));

    }
  }


  /**
   * boolean AddMustNotFilter  AddMustNotFilter条件组合构造
   *
   * @return void
   */
  private static void AddMustNotFilter(BoolQueryBuilder boolQueryBuilder, FilterBDU bdu) {
    if (bdu.getQuerytype().equals(QueryType.Term)) {
      boolQueryBuilder.mustNot(QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val())
          .boost(bdu.getInfo_point_weight()));
    } else if (bdu.getQuerytype().equals(QueryType.List)) {
      //            Object[] obj=(Object[])bdu.getInfo_point_val();
      MultiList(boolQueryBuilder, bdu);
      //            for(int i=0;i<obj.length;i++) {
      ////            System.out.println( );
      //                boolQueryBuilder.must(QueryBuilders.termsQuery(bdu.getInfo_point(),obj[i]).boost(bdu.getInfo_point_weight()));
      //            }
      //            boolQueryBuilder.must(QueryBuilders.termsQuery(bdu.getInfo_point(), bdu.getInfo_point_val()).boost(bdu.getInfo_point_weight()));
    } else if (bdu.getQuerytype().equals(QueryType.Range)) {
      RangeQueryBuilder rangeQueryBuilder = GetRangeQueryBuilder(bdu);
      boolQueryBuilder.mustNot(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
      //                    RangeBDU rangeBDU = (RangeBDU)bdu.getInfo_point_val();
      //                    boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(bdu.getInfo_point()).from(rangeBDU.getFrom()).to(rangeBDU.getTo()).includeLower(true).boost(bdu.getInfo_point_weight()));
    } else if (bdu.getQuerytype().equals(QueryType.Match)) {
      highlightfield.add(bdu.getInfo_point());
      //                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
      boolQueryBuilder.mustNot(QueryBuilders
          .multiMatchQuery(bdu.getInfo_point_val().toString().toLowerCase(), bdu.getInfo_point())
          .boost(bdu.getInfo_point_weight()).minimumShouldMatch("100%"));
    } else if (bdu.getQuerytype().equals(QueryType.NotExist)) {
      //                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
      boolQueryBuilder
          .mustNot(QueryBuilders.missingQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));

    } else if (bdu.getQuerytype().equals(QueryType.Exist)) {
      //                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
      boolQueryBuilder
          .mustNot(QueryBuilders.existsQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));

    } else if (bdu.getQuerytype().equals(QueryType.Prefix)) {
      //                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
      boolQueryBuilder
          .must(QueryBuilders.prefixQuery(bdu.getInfo_point(), bdu.getInfo_point_val().toString()));

    } else {
      highlightfield.add(bdu.getInfo_point());
      boolQueryBuilder.mustNot(QueryBuilders
          .wildcardQuery(bdu.getInfo_point(), "*" + bdu.getInfo_point_val().toString() + "*")
          .boost(bdu.getInfo_point_weight()));

    }
  }
}
