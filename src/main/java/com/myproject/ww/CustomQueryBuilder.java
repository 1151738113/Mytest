//package com.futuredata.ww;
//
//import com.futuredata.SimLawCase.Recommender.domain.FilterBDU;
//import com.futuredata.SimLawCase.Recommender.domain.FilterType;
//import com.futuredata.SimLawCase.Recommender.domain.QueryType;
//import com.futuredata.SimLawCase.Recommender.domain.RangeBDU;
//import org.elasticsearch.index.query.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.lang.reflect.Array;
//import java.util.*;
//
//import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;
//
///**
// * Created by gengw on 10/27/16.
// */
//
//public class CustomQueryBuilder {
//
//
//    /**
//     * 日志记录组件
//     *
//     * @return logger
//     */
//    private static Logger logger = LoggerFactory.getLogger(CustomQueryBuilder.class);
//
//    public static List<String> highlightfield ;
//    public static List<String> keywordList ;
//    public static BoolQueryBuilder myfilters;
//
//
//    /**
//     * boolean query and 条件组合查询
//     *
//     * @return QueryBuilder
//     */
//
//    public static QueryBuilder boolQuery(Map<String,FilterBDU> filters){
//      filters.put("meta_案号",new FilterBDU(FilterType.Must,QueryType.Exist,"meta_案号"));
//      filters.put("meta_判决类型1",new FilterBDU(FilterType.MustNot,QueryType.Term,"meta_判决类型","调解书"));
//      BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//      boolQuery2(boolQueryBuilder,filters);
//      return boolQueryBuilder;
//    }
//  public static QueryBuilder boolQuery1(Map<String,FilterBDU> filters){
//    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//    boolQuery2(boolQueryBuilder,filters);
//    return boolQueryBuilder;
//  }
//
//    public static QueryBuilder boolQuery2(BoolQueryBuilder boolQueryBuilder,Map<String,FilterBDU> filters) {
//
////        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//
//
////        boolQueryBuilder.filter(QueryBuilders.matchQuery("meta_全文",fulltext));
////        logger.error(JSON.toJSONString(filters,true));
////        boolQueryBuilder.must(QueryBuilders.existsQuery("meta_案号").queryName("meta_案号"));//.boost(0.0f);
//////        boolQueryBuilder.must(QueryBuilders.missingQuery("meta_级别").queryName("meta_级别"));
////        boolQueryBuilder.mustNot(QueryBuilders.termQuery("meta_判决类型", "调解书"));//.boost(0.0f));//.boost(0.0f);
//        Map<String, BoolQueryBuilder> groupsMap = new HashMap<String, BoolQueryBuilder>();
//        myfilters = new BoolQueryBuilder();
////        boolQueryBuilder.must(QueryBuilders.e)
////        BoolQueryBuilder boolgroupBuilder=null;
////        Set<String> set = new HashSet<String>();
//        Map<String,Set<FilterBDU>> nestedfilter=new HashMap<String,Set<FilterBDU>>();
//        highlightfield = new Vector<String>();
//        keywordList = new Vector<String>();
//        int shouldNum = 0,nestedNum=0,mustNum=0;
//        for (Map.Entry<String, FilterBDU> entry : filters.entrySet()) {
//            FilterBDU bdu = entry.getValue();
//            if (bdu.isnestedfield()) {
//                String info_point = bdu.getInfo_point();
//                if(bdu.getFiltertype().equals(FilterType.Probably))
//                    nestedNum++;
//                String nestedPath = info_point.substring(0, info_point.lastIndexOf("."));
//                if(bdu.getFiltertype().equals(FilterType.Must))
//                {
//                    mustNum++;
//                    if(nestedPath.equals("meta_人物信息.peopleAttrMap")) {
//                        if (bdu.getQuerytype().equals(QueryType.Term)) {
//                            QueryBuilder queryBuilder = QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val()).boost(bdu.getInfo_point_weight());
//                            myfilters.must(queryBuilder);
//
//                        }else if (bdu.getQuerytype().equals(QueryType.Range)) {
//                            RangeQueryBuilder rangeQueryBuilder = GetRangeQueryBuilder(bdu);
//                            myfilters.must(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
//                        }
//                    }
//
//                }else if(bdu.getFiltertype().equals(FilterType.MustNot))
//                {
//                    if(nestedPath.equals("meta_人物信息.peopleAttrMap")) {
//                        if (bdu.getQuerytype().equals(QueryType.Term)) {
//                            QueryBuilder queryBuilder = QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val()).boost(bdu.getInfo_point_weight());
//                            myfilters.mustNot(queryBuilder);
//
//                        }
//                    }
//                }
//                if (!nestedfilter.containsKey(nestedPath))
//                    nestedfilter.put(nestedPath, new HashSet<FilterBDU>());
//                nestedfilter.get(nestedPath).add(bdu);
//
//            }else {
//                String groups = bdu.getInfo_group();
//                if(bdu.getFiltertype().equals(FilterType.Probably))
//                    shouldNum++;
//                if (groups.equals("")) {
//                    AddGroupFilter(boolQueryBuilder, bdu);
//                } else {
//                    try {
//                        if (!groupsMap.containsKey(groups)) {
//                            BoolQueryBuilder boolgroupBuilder = new BoolQueryBuilder();
//                            boolgroupBuilder.minimumShouldMatch("1");
//                            groupsMap.put(groups, boolgroupBuilder);
//                            AddGroupFilter(boolgroupBuilder, bdu);
//                            boolQueryBuilder.must(boolgroupBuilder);
//                        } else {
//                            BoolQueryBuilder boolgroupBuilder = groupsMap.get(groups);
//                            AddGroupFilter(boolgroupBuilder, bdu);
////                            if(logger.isInfoEnabled()) {
////                                logger.info("group:" + boolgroupBuilder);
////                            }
//                        }
//                    } catch (Exception ex) {
//                        logger.error("boolQuery:" + ex);
//                    }
//                }
//            }
//        }
//        if(nestedfilter.size()>0)
//        {
//            AddNestedFilter(boolQueryBuilder,nestedfilter,mustNum);
//            int shouldCnt= Math.round((nestedNum)/2);
//            boolQueryBuilder.minimumShouldMatch(String.valueOf(shouldCnt));
////            if(mustNum<2)
////                boolQueryBuilder.must(nestedQuery("meta_人物信息.peopleAttrMap", QueryBuilders.boolQuery().must(QueryBuilders.existsQuery("meta_人物信息.peopleAttrMap.info_主刑罪名").queryName("meta_人物信息.peopleAttrMap.info_主刑罪名"))));
//        }else
//        {
////            boolQueryBuilder.minimumShouldMatch(String.valueOf(Math.round(shouldNum/2)));
//        }
////        logger.info("boolQueryBuilder:"+boolQueryBuilder);
//        return boolQueryBuilder;
//    }
//
//
//     /**
//     * boolean AddNestedFilter 添加nested组查询条件
//     *
//     * @return
//     */
//    private static void AddNestedFilter(BoolQueryBuilder boolQueryBuilder,Map<String,Set<FilterBDU>> nestedfilter,int mustNum) {
//        Map<String, BoolQueryBuilder> groupsMap = new HashMap<String, BoolQueryBuilder>();
//        Map<String, BoolQueryBuilder> nestedgroupsMap = new HashMap<String, BoolQueryBuilder>();
//        boolean isadd = false;
//        for (Map.Entry<String, Set<FilterBDU>> entry : nestedfilter.entrySet()) {
//            BoolQueryBuilder nestboolBuilder = new BoolQueryBuilder();
//            Set<FilterBDU> listnested = entry.getValue();
//            for (FilterBDU obj : listnested) {
////                logger.info("AddNestedFilter FilterBDU:" + obj);
//
////
//                if (obj.getFiltertype().equals(FilterType.Probably)) {
//                    if (obj.getQuerytype().equals(QueryType.Term)) {
//                        boolQueryBuilder.should(nestedQuery(entry.getKey(), QueryBuilders.boolQuery().should(QueryBuilders.termQuery(obj.getInfo_point(), obj.getInfo_point_val()).boost(obj.getInfo_point_weight()))));
//                    } else if (obj.getQuerytype().equals(QueryType.NotExist)) {
//                        boolQueryBuilder.should(QueryBuilders.notQuery(nestedQuery(entry.getKey(), QueryBuilders.boolQuery().must(QueryBuilders.existsQuery(obj.getInfo_point()).queryName(obj.getInfo_point())))));
//                    }
//                } else if (obj.getFiltertype().equals(FilterType.Must)) {
//                    String groups = obj.getInfo_group();
//                    if (!groupsMap.containsKey(groups)) {
//                        BoolQueryBuilder boolgroupBuilder = new BoolQueryBuilder();
////                        boolgroupBuilder.minimumShouldMatch("50%");
//                        groupsMap.put(groups, boolgroupBuilder);
//                        AddNestedGroupFilter(boolgroupBuilder, obj, entry.getKey());
//                        boolQueryBuilder.must(nestedQuery(entry.getKey(), boolgroupBuilder));
//
//                    } else {
//                        BoolQueryBuilder boolgroupBuilder = groupsMap.get(groups);
//                        AddNestedGroupFilter(boolgroupBuilder, obj, entry.getKey());
//                    }
//                } else if (obj.getFiltertype().equals(FilterType.MustNot)) {
//
//                    String groups = obj.getInfo_group();
//                    if (!groupsMap.containsKey(groups)) {
//                        BoolQueryBuilder boolgroupBuilder = new BoolQueryBuilder();
////                        boolgroupBuilder.minimumShouldMatch("50%");
//                        groupsMap.put(groups, boolgroupBuilder);
//                        AddNestedGroupFilter(boolgroupBuilder, obj, entry.getKey());
//                        boolQueryBuilder.must(nestedQuery(entry.getKey(), boolgroupBuilder));
//                    } else {
//                        BoolQueryBuilder boolgroupBuilder = groupsMap.get(groups);
//                        AddNestedGroupFilter(boolgroupBuilder, obj, entry.getKey());
//                    }
//                    if (mustNum < 2 && !isadd) {
//                        isadd = true;
//                        BoolQueryBuilder boolgroupBuilder = groupsMap.get(groups);
//                        boolgroupBuilder.must(QueryBuilders.existsQuery("meta_人物信息.peopleAttrMap.info_主刑罪名").queryName("meta_人物信息.peopleAttrMap.info_主刑罪名"));
//                    }
//                }
//            }
//
////            boolQueryBuilder.must(nestedQuery(entry.getKey(),nestboolBuilder));
//        }
//    }
//
//    /**
//     * boolean AddNestedGroupFilter  增加嵌套主从条件AddNestedGroupFilter
//     *
//     * @return void
//     */
//    private static void AddNestedGroupFilter(BoolQueryBuilder boolgroupBuilder,FilterBDU bdu,String nestedPath) {
//
//        try {
//
//            if (bdu.getFiltertype().equals(FilterType.Probably)) {
//                AddNestedProbablyFilter(boolgroupBuilder,bdu,nestedPath);
//            }
//            else if (bdu.getFiltertype().equals(FilterType.Must)) {
//                AddNestedMustFilter(boolgroupBuilder,bdu,nestedPath);
//            }
//            else if (bdu.getFiltertype().equals(FilterType.MustNot)) {
//                AddNestedMustNotFilter(boolgroupBuilder,bdu,nestedPath);
//            }
//        }catch (Exception ex)
//        {
//            logger.error("AddNestedGroupFilter:"+ex);
//        }
//    }
//
//    /**
//     * boolean AddNestedMustFilter  Must嵌套主从条件AddNestedMustFilter
//     *
//     * @return void
//     */
//    private static void AddNestedMustFilter(BoolQueryBuilder boolgroupBuilder,FilterBDU bdu,String nestedPath) {
////        BoolQueryBuilder nestboolBuilder = new BoolQueryBuilder();
//        if (bdu.getQuerytype().equals(QueryType.Term)) {
//            boolgroupBuilder.must(QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val()).boost(bdu.getInfo_point_weight()));
//        } else if (bdu.getQuerytype().equals(QueryType.Range)) {
//            RangeQueryBuilder rangeQueryBuilder = GetRangeQueryBuilder(bdu);
//            boolgroupBuilder.must(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
//        } else if (bdu.getQuerytype().equals(QueryType.NotExist)) {
//            boolgroupBuilder.must(QueryBuilders.missingQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()).existence(true).nullValue(true));        }
//
//    }
//
//    /**
//     * boolean AddNestedProbablyFilter  Probably嵌套主从条件AddNestedProbablyFilter
//     *
//     * @return void
//     */
//    private static void AddNestedProbablyFilter(BoolQueryBuilder boolgroupBuilder,FilterBDU bdu,String nestedPath) {
////        BoolQueryBuilder nestboolBuilder = new BoolQueryBuilder();
//        if (bdu.getQuerytype().equals(QueryType.Term)) {
//            boolgroupBuilder.should(QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val()).boost(bdu.getInfo_point_weight()));
//        } else if (bdu.getQuerytype().equals(QueryType.Range)) {
//            RangeQueryBuilder rangeQueryBuilder = GetRangeQueryBuilder(bdu);
//            boolgroupBuilder.should(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
//        } else if (bdu.getQuerytype().equals(QueryType.NotExist)) {
//            boolgroupBuilder.should(QueryBuilders.existsQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));
//        }
//    }
//
//    /**
//     * boolean AddNestedMustNotFilter  MustNot嵌套主从条件AddNestedMustNotFilter
//     *
//     * @return void
//     */
//    private static void AddNestedMustNotFilter(BoolQueryBuilder boolgroupBuilder,FilterBDU bdu,String nestedPath) {
////        BoolQueryBuilder nestboolBuilder = new BoolQueryBuilder();
//        if (bdu.getQuerytype().equals(QueryType.Term)) {
//            boolgroupBuilder.mustNot(QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val())).boost(bdu.getInfo_point_weight());
//        } else if (bdu.getQuerytype().equals(QueryType.Range)) {
//            RangeQueryBuilder rangeQueryBuilder = GetRangeQueryBuilder(bdu);
//            boolgroupBuilder.mustNot(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
//        }
////        return nestboolBuilder;
////        boolQueryBuilder.must(nestedQuery(nestedPath,nestboolBuilder));
//    }
//
//    /**
//     * boolean AddGroupFilter 添加组查询条件
//     *
//     * @return
//     */
//    private static void AddGroupFilter(BoolQueryBuilder boolQueryBuilder,FilterBDU bdu) {
//        try {
//            if (bdu.getFiltertype().equals(FilterType.Must)) {
//                AddMustFilter(boolQueryBuilder, bdu);
//            }
//            if (bdu.getFiltertype().equals(FilterType.Probably)) {
//                AddProbablyFilter(boolQueryBuilder, bdu);
//            }
//            if (bdu.getFiltertype().equals(FilterType.MustNot)) {
//                AddMustNotFilter(boolQueryBuilder, bdu);
//            }
//        }catch (Exception ex)
//        {
//            logger.error("AddGroupFilter:"+ex);
//        }
//    }
//
//    /**
//     * boolean RangeQueryBuilder  条件组合构造
//     *
//     * @return RangeQueryBuilder
//     */
//    private static RangeQueryBuilder GetRangeQueryBuilder(FilterBDU bdu)
//    {
//        RangeBDU rangeBDU = (RangeBDU)bdu.getInfo_point_val();
//        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder(bdu.getInfo_point());
//        rangeQueryBuilder.queryName(bdu.getInfo_point());
//        if(rangeBDU.getFrom()!=null) {
//            rangeQueryBuilder.gte(rangeBDU.getFrom());
//        }
//        if(rangeBDU.getTo()!=null) {
//            rangeQueryBuilder.lt(rangeBDU.getTo());
//        }
//
//        return rangeQueryBuilder;
//    }
//
//    /**
//     * void MultiList  对数组类型做处理
//     *
//     * @return void
//     */
//    @SuppressWarnings("rawtypes")
//	private static void MultiList(BoolQueryBuilder boolQueryBuilder,FilterBDU bdu) {
//        Object obj = bdu.getInfo_point_val();
//        // 传入数值非数组时, 不作为检索条件
//        if (!obj.getClass().isArray() && !(obj instanceof ArrayList)) {
//          return;
//        }
//
//        if(obj instanceof ArrayList){
//        	obj = ((ArrayList)obj).toArray();
//        }
//
//        int length = Array.getLength(obj);
//        // 传入数组为空时, 不作为检索条件
//        if (length == 0) {
//          return;
//        }
//
//        // 传入数值转数组
//        Object[] pointVals = new Object[length];
//        for (int i = 0; i < length; i++) {
//          pointVals[i] = (Array.get(obj, i));
//        }
//
//        // 构造ES的检索条件
//        TermsQueryBuilder tqBuilder = QueryBuilders.termsQuery(bdu.getInfo_point(), pointVals)
//            .boost(bdu.getInfo_point_weight());
//
//        if (bdu.getFiltertype().equals(FilterType.Must)) {
//          boolQueryBuilder.must(tqBuilder);
//        } else if (bdu.getFiltertype().equals(FilterType.Probably)) {
//          boolQueryBuilder.should(tqBuilder);
//        } else {
//          boolQueryBuilder.mustNot(tqBuilder);
//        }
//    }
//
//    /**
//     * boolean AddMustFilter  AddMustFilter条件组合构造
//     *
//     * @return void
//     */
//    private static void AddMustFilter(BoolQueryBuilder boolQueryBuilder,FilterBDU bdu)
//    {
//        if(bdu.getQuerytype().equals(QueryType.Term)) {
//            QueryBuilder queryBuilder=QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val()).boost(bdu.getInfo_point_weight());
//            if(!bdu.isnestedfield())
//            {
//                boolQueryBuilder.must(queryBuilder);
//            }
//        }
//        else if(bdu.getQuerytype().equals(QueryType.List)) {
//            MultiList(boolQueryBuilder,bdu);
////            highlightfield.add(bdu.getInfo_point());
//
//        }else if(bdu.getQuerytype().equals(QueryType.Range))
//        {
//            RangeQueryBuilder rangeQueryBuilder=GetRangeQueryBuilder(bdu);
//            boolQueryBuilder.must(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
//        }else if(bdu.getQuerytype().equals(QueryType.Match))
//        {
//            if(bdu.isphrase()) {
//                keywordList.add(bdu.getInfo_point_val().toString());
//                boolQueryBuilder.must(QueryBuilders.matchQuery(bdu.getInfo_point(),bdu.getInfo_point_val().toString().toLowerCase()).type(MatchQueryBuilder.Type.PHRASE).boost(bdu.getInfo_point_weight()).minimumShouldMatch("100%"));
//            }else
//            {
//                boolQueryBuilder.must(QueryBuilders.multiMatchQuery(bdu.getInfo_point_val().toString().toLowerCase(), bdu.getInfo_point(), bdu.getInfo_point() + ".bigram^10").boost(bdu.getInfo_point_weight()).minimumShouldMatch("100%"));
//            }
//            highlightfield.add(bdu.getInfo_point());
//        }else if(bdu.getQuerytype().equals(QueryType.MultiMatch))
//        {
//            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(bdu.getInfo_point_val().toString().toLowerCase(), bdu.getInfo_point(), bdu.getInfo_point() + ".bigram^10").boost(bdu.getInfo_point_weight()).minimumShouldMatch("100%"));
//            highlightfield.add(bdu.getInfo_point());
//        }
//        else if(bdu.getQuerytype().equals(QueryType.NotExist))
//        {
////                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
//            boolQueryBuilder.must(QueryBuilders.missingQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));
//
//        }else if(bdu.getQuerytype().equals(QueryType.Exist))
//        {
////                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
//            boolQueryBuilder.must(QueryBuilders.existsQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));
//
//        }else if(bdu.getQuerytype().equals(QueryType.Prefix))
//        {
////                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
//            boolQueryBuilder.must(QueryBuilders.prefixQuery(bdu.getInfo_point(),bdu.getInfo_point_val().toString()));
//
//        }else
//        {
//            boolQueryBuilder.must(QueryBuilders.wildcardQuery(bdu.getInfo_point(),"*"+bdu.getInfo_point_val().toString()+"*").boost(bdu.getInfo_point_weight()));
//            highlightfield.add(bdu.getInfo_point());
//
//        }
//    }
//
//
//    /**
//     * boolean AddProbablyFilter  AddProbablyFilter条件组合构造
//     *
//     * @return void
//     */
//    private static void AddProbablyFilter(BoolQueryBuilder boolQueryBuilder,FilterBDU bdu)
//    {
//        if(bdu.getQuerytype().equals(QueryType.Term)) {
//            boolQueryBuilder.should(QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val()).boost(bdu.getInfo_point_weight()));
//        }else if(bdu.getQuerytype().equals(QueryType.List)) {
//            MultiList(boolQueryBuilder,bdu);
////            highlightfield.add(bdu.getInfo_point());
////            boolQueryBuilder.should(QueryBuilders.termsQuery(bdu.getInfo_point(), bdu.getInfo_point_val()).boost(bdu.getInfo_point_weight()));
//        }
//        else if(bdu.getQuerytype().equals(QueryType.Range)) {
//            RangeQueryBuilder rangeQueryBuilder=GetRangeQueryBuilder(bdu);
//            boolQueryBuilder.should(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
//        }else if(bdu.getQuerytype().equals(QueryType.Match))
//        {
//            highlightfield.add(bdu.getInfo_point());
////                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
//            if(bdu.isphrase()) {
//                boolQueryBuilder.should(QueryBuilders.matchQuery(bdu.getInfo_point(),bdu.getInfo_point_val().toString().toLowerCase()).type(MatchQueryBuilder.Type.PHRASE).boost(bdu.getInfo_point_weight()).minimumShouldMatch("100%"));
//            }else
//            {
//                boolQueryBuilder.should(QueryBuilders.multiMatchQuery(bdu.getInfo_point_val().toString().toLowerCase(), bdu.getInfo_point(), bdu.getInfo_point() + ".bigram^10").boost(bdu.getInfo_point_weight()).minimumShouldMatch("100%"));
//            }
//        }else if(bdu.getQuerytype().equals(QueryType.NotExist))
//        {
////                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
//            boolQueryBuilder.should(QueryBuilders.missingQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));
//
//        }else if(bdu.getQuerytype().equals(QueryType.Exist))
//        {
////                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
//            boolQueryBuilder.should(QueryBuilders.existsQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));
//
//        }else if(bdu.getQuerytype().equals(QueryType.Prefix))
//        {
////                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
//            boolQueryBuilder.must(QueryBuilders.prefixQuery(bdu.getInfo_point(),bdu.getInfo_point_val().toString()));
//
//        }else
//        {
//            highlightfield.add(bdu.getInfo_point());
//            boolQueryBuilder.should(QueryBuilders.wildcardQuery(bdu.getInfo_point(),"*"+bdu.getInfo_point_val().toString()+"*").boost(bdu.getInfo_point_weight()));
//
//        }
//    }
//
//
//    /**
//     * boolean AddMustNotFilter  AddMustNotFilter条件组合构造
//     *
//     * @return void
//     */
//    private static void AddMustNotFilter(BoolQueryBuilder boolQueryBuilder,FilterBDU bdu)
//    {
//        if(bdu.getQuerytype().equals(QueryType.Term)) {
//            boolQueryBuilder.mustNot(QueryBuilders.termQuery(bdu.getInfo_point(), bdu.getInfo_point_val()).boost(bdu.getInfo_point_weight()));
//        }else if(bdu.getQuerytype().equals(QueryType.List)) {
////            Object[] obj=(Object[])bdu.getInfo_point_val();
//            MultiList(boolQueryBuilder,bdu);
////            for(int i=0;i<obj.length;i++) {
//////            System.out.println( );
////                boolQueryBuilder.must(QueryBuilders.termsQuery(bdu.getInfo_point(),obj[i]).boost(bdu.getInfo_point_weight()));
////            }
////            boolQueryBuilder.must(QueryBuilders.termsQuery(bdu.getInfo_point(), bdu.getInfo_point_val()).boost(bdu.getInfo_point_weight()));
//        }
//        else if(bdu.getQuerytype().equals(QueryType.Range)) {
//            RangeQueryBuilder rangeQueryBuilder=GetRangeQueryBuilder(bdu);
//            boolQueryBuilder.mustNot(rangeQueryBuilder).boost(bdu.getInfo_point_weight());
////                    RangeBDU rangeBDU = (RangeBDU)bdu.getInfo_point_val();
////                    boolQueryBuilder.mustNot(QueryBuilders.rangeQuery(bdu.getInfo_point()).from(rangeBDU.getFrom()).to(rangeBDU.getTo()).includeLower(true).boost(bdu.getInfo_point_weight()));
//        }else if(bdu.getQuerytype().equals(QueryType.Match))
//        {
//            highlightfield.add(bdu.getInfo_point());
////                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
//            boolQueryBuilder.mustNot(QueryBuilders.multiMatchQuery(bdu.getInfo_point_val().toString().toLowerCase(),bdu.getInfo_point()).boost(bdu.getInfo_point_weight()).minimumShouldMatch("100%"));
//        }else if(bdu.getQuerytype().equals(QueryType.NotExist))
//        {
////                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
//            boolQueryBuilder.mustNot(QueryBuilders.missingQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));
//
//        }else if(bdu.getQuerytype().equals(QueryType.Exist))
//        {
////                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
//            boolQueryBuilder.mustNot(QueryBuilders.existsQuery(bdu.getInfo_point()).queryName(bdu.getInfo_point()));
//
//        }else if(bdu.getQuerytype().equals(QueryType.Prefix))
//        {
////                    boolQueryBuilder.filter(QueryBuilders.moreLikeThisQuery(bdu.getInfo_point_val().toString()).boost(bdu.getInfo_point_weight()));
//            boolQueryBuilder.must(QueryBuilders.prefixQuery(bdu.getInfo_point(),bdu.getInfo_point_val().toString()));
//
//        }else {
//            highlightfield.add(bdu.getInfo_point());
//            boolQueryBuilder.mustNot(QueryBuilders.wildcardQuery(bdu.getInfo_point(),"*"+bdu.getInfo_point_val().toString()+"*").boost(bdu.getInfo_point_weight()));
//
//        }
//    }
//}
