package com.myproject.util;

/**
 * Created by gengw on 10/26/16.
 */
public enum StatsType {
    //范围统计
    Range,

    //枚举列表统计
    List,

    //日期时间统计
    HDate,

    //直方图统计
    Histogram,

    //百分位统计
    Percentile,

    //百分位等级
    PercentileRank,

    //描述性基本统计avg,max,min,
    StatsDes,

    //扩展描述性统计标准差,方差
    ExtentsStats,

    // 嵌套类型匹配
    Term
}
