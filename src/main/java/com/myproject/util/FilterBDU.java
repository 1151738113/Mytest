package com.myproject.util;

import java.util.List;

/**
 * Created by gengw on 10/24/16.
 */

public class FilterBDU {

    //过滤类型
    private FilterType filtertype;
    //查询类型
    private QueryType querytype;

    //信息点
    private String info_point;

    //信息点值
    private Object info_point_val;

    private String info_group="";

    //信息点权重
    private float info_point_weight = 1.0f;

    //是否为嵌套类型
    private  boolean isnestedfield = false;

    //是否为短语
    private boolean isphrase = false;

    //多层嵌套查询
    private List<GroupsBDU> info_group_bool;


    public boolean isnestedfield() {
        return isnestedfield;
    }

    public void setIsnestedfield(boolean isnestedfield) {
        this.isnestedfield = isnestedfield;
    }

    public boolean isphrase() {
        return isphrase;
    }

    public void setIsphrase(boolean isphrase) {
        this.isphrase = isphrase;
    }


    public FilterBDU()
    {

    }

    public  FilterBDU(FilterType filtertype,QueryType querytype,String info_point,Object info_point_val,List<GroupsBDU> info_group_bool){
      this.filtertype = filtertype;
      this.querytype = querytype;
      this.info_point = info_point;
      this.info_point_val = info_point_val;
      this.info_group_bool = info_group_bool;
    }

    public FilterBDU(FilterType filtertype,QueryType querytype,String info_point)
    {

        this.filtertype=filtertype;
        this.querytype=querytype;
        this.info_point=info_point;
    }

//    public FilterBDU(FilterType filtertype,QueryType querytype,String info_point,String info_group)
//    {
//        this.filtertype=filtertype;
//        this.querytype=querytype;
//        this.info_point=info_point;
//        this.info_group=info_group;
//    }

    public FilterBDU(FilterType filtertype,QueryType querytype,String info_point,Object info_point_val)
    {

        this.filtertype=filtertype;
        this.querytype=querytype;
        this.info_point=info_point;
        this.info_point_val=info_point_val;
    }

    public FilterBDU(FilterType filtertype,QueryType querytype,String info_point,Object info_point_val,float info_point_weight)
    {

        this.filtertype=filtertype;
        this.querytype=querytype;
        this.info_point=info_point;
        this.info_point_val=info_point_val;
        this.info_point_weight = info_point_weight;
    }

    public FilterBDU(FilterType filtertype,QueryType querytype,String info_point,Object info_point_val,String info_group)
    {

        this.filtertype=filtertype;
        this.querytype=querytype;
        this.info_point=info_point;
        this.info_point_val=info_point_val;
        this.info_group=info_group;
    }


    public FilterBDU(FilterType filtertype,QueryType querytype,String info_point,Object info_point_val,String info_group,float info_point_weight)
    {
        this.filtertype=filtertype;
        this.querytype=querytype;
        this.info_point=info_point;
        this.info_point_val=info_point_val;
        this.info_point_weight = info_point_weight;
        this.info_group=info_group;
    }
    public FilterBDU(FilterType filtertype,QueryType querytype,boolean isphrase,Object info_point_val,String... info_point)
    {
        this.filtertype=filtertype;
        this.querytype=querytype;
//        this.info_point=info_point;
        this.info_point_val=info_point_val;
        this.isphrase = isphrase;
    }
    public FilterBDU(FilterType filtertype,QueryType querytype,String info_point,Object info_point_val,String info_group,float info_point_weight,boolean isphrase)
    {
        this.filtertype=filtertype;
        this.querytype=querytype;
        this.info_point=info_point;
        this.info_point_val=info_point_val;
        this.info_point_weight = info_point_weight;
        this.info_group=info_group;
        this.isphrase = isphrase;
    }

    public FilterBDU(FilterType filtertype,QueryType querytype,String[] info_point,Object info_point_val,float[] info_point_weight)
    {

        this.filtertype=filtertype;
        this.querytype=querytype;
//        this.info_point=info_point;
        this.info_point_val=info_point_val;
//        this.info_point_weight = info_point_weight;
    }

    public String getInfo_group() {
        return info_group;
    }

    public void setInfo_group(String info_group) {
        this.info_group = info_group;
    }

    public FilterType getFiltertype() {
        return filtertype;
    }

    public void setFiltertype(FilterType filtertype) {
        this.filtertype = filtertype;


    }

    public QueryType getQuerytype() {
        return querytype;
    }

    public void setQuerytype(QueryType querytype) {
        this.querytype = querytype;
    }


    public String getInfo_point() {
        return info_point;
    }

    public void setInfo_point(String info_point) {
        this.info_point = info_point;
    }


    public Object getInfo_point_val() {
        return info_point_val;
    }

    public void setInfo_point_val(Object info_point_val) {
        this.info_point_val = info_point_val;
    }

    public float getInfo_point_weight() {
        return info_point_weight;
    }

    public void setInfo_point_weight(float info_point_weight) {
        this.info_point_weight = info_point_weight;
    }

    public String toString()
    {
        return "[ info_point（信息点）=" + info_point + ",querytype（查询类型）=" + querytype +",info_group（分组）=" +info_group +  ",filtertype（过滤类型）=" + filtertype + ",info_point_val（信息点的值）=" + info_point_val + ",info_point_weight（信息点权重）=" + info_point_weight + ",isnestedfield(是否嵌套)="+isnestedfield+"]";
    }


  public List<GroupsBDU> getInfo_group_bool() {
    return info_group_bool;
  }

  public void setInfo_group_bool(List<GroupsBDU> info_group_bool) {
    this.info_group_bool = info_group_bool;
  }
}
