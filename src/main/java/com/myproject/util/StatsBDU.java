package com.myproject.util;

import java.util.Map;


/**
 * Created by gengw on 10/26/16.
 */

public class StatsBDU {
    private String info_point;
    private String aliasName;
    private StatsType statstype;
    private Object statsval;
    private boolean isnestedfield;

    public boolean isnestedfield() {
        return isnestedfield;
    }

    public void setIsnestedfield(boolean isnestedfield) {
        this.isnestedfield = isnestedfield;
    }

    //返回统计结果记录数量
    private int size=0;



    private Map<String,String> trans;

    public StatsBDU()
    {

    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Map<String, String> getTrans() {
        return trans;
    }

    public void setTrans(Map<String, String> trans) {
        this.trans = trans;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public StatsBDU(String info_point, StatsType statstype)
    {
        this.info_point = info_point;
        this.statstype = statstype;
    }

    public StatsBDU(String aliasName,String info_point,StatsType statstype)
    {
        this.aliasName = aliasName;
        this.statstype = statstype;
        this.info_point = info_point;
    }
    public StatsBDU(String aliasName,String info_point,StatsType statstype,int size) {
        this.aliasName = aliasName;
        this.statstype = statstype;
        this.info_point = info_point;
        this.size = size;
    }

    public StatsBDU(String aliasName,String info_point,StatsType statstype,Map<String, String> trans)
    {
        this.aliasName = aliasName;
        this.statstype = statstype;
        this.info_point = info_point;
        this.trans = trans;
    }

    public StatsBDU(String aliasName,String info_point,StatsType statstype,Object statsval)
    {
        this.info_point = info_point;
        this.aliasName=aliasName;
        this.statstype = statstype;
        this.statsval = statsval;
    }


    public StatsBDU(String aliasName,String info_point,StatsType statstype,Object statsval,Map<String, String> trans)
    {
        this.info_point = info_point;
        this.aliasName=aliasName;
        this.statstype = statstype;
        this.statsval = statsval;
        this.trans = trans;
    }

    public String getInfo_point() {
        return info_point;
    }

    public void setInfo_point(String info_point) {
        this.info_point = info_point;
    }

    public StatsType getStatstype() {
        return statstype;
    }

    public void setStatstype(StatsType statstype) {
        this.statstype = statstype;
    }

    public Object getStatsval() {
        return statsval;
    }

    public void setStatsval(Object statsval) {
        this.statsval = statsval;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatsBDU)) return false;

        StatsBDU statsBDU = (StatsBDU) o;

        if (!info_point.equals(statsBDU.info_point)) return false;
        if (!aliasName.equals(statsBDU.aliasName)) return false;
        if (!statstype.equals(statsBDU.statstype)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        String in = info_point + aliasName + statstype;
        return in.hashCode();
    }
    // 类型

    public String toString()
    {
        return "[ 信息点=" + info_point + ",aliasName="+aliasName+",统计类型=" + statstype + ",统计条件值=" + statsval + "]";
    }
}
