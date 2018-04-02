package com.myproject.util;

/**
 * Created by gengw on 10/24/16.
 */
public enum QueryType {

    //字段存在判断 不支持nested类型字段
    Exist,

    //字段不存在判断 不支持nested类型字段
    NotExist,

    //范围查询
    Range,

    //列表查询 不支持nested类型字段
    List,

    //项查询
    Term,

    //语义匹配查询 分词 不支持nested类型字段
    Match,

    //多字段语义匹配查询 分词 不支持nested类型字段
    MultiMatch,

    //模糊匹配查询 不分词 不支持nested类型字段
    Fuzzy,

    //前缀匹配查询 不分词 不支持nested类型字段
    Prefix,

    //正则表达式匹配查询 不分词 不支持nested类型字段
    Regex
}
