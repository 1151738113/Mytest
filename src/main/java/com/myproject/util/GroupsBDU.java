package com.myproject.util;

/**
 * Created by wei.wang on 2018/1/5.
 */
public class GroupsBDU {
  //每层的过滤方式
  private FilterType filterType;
  //每层的组名
  private String groups;

  public GroupsBDU(){

  }

  public GroupsBDU(FilterType filterType,String groups){
  this.filterType = filterType;
  this.groups = groups;
  }

  public FilterType getFilterType() {
    return filterType;
  }

  public void setFilterType(FilterType filterType) {
    this.filterType = filterType;
  }

  public String getGroups() {
    return groups;
  }

  public void setGroups(String groups) {
    this.groups = groups;
  }
}
