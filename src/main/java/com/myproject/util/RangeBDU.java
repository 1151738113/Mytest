package com.myproject.util;

/**
 * Created by gengw on 10/25/16.
 */
public class RangeBDU {
    private String info_points = "";
    private Object from;
    private Object to;

    public RangeBDU()
    {

    }

    public RangeBDU(Object from,Object to)
    {

        this.from=from;
        this.to=to;
    }

    public RangeBDU(String info_points,Object from,Object to)
    {
        this.info_points=info_points;
        this.from=from;
        this.to=to;
    }

    public String getInfo_points() {
        return info_points;
    }

    public void setInfo_points(String info_points) {
        this.info_points = info_points;
    }

    public Object getFrom() {
        return from;
    }

    public void setFrom(Object from) {
        this.from = from;
    }

    public Object getTo() {
        return to;
    }

    public void setTo(Object to) {
        this.to = to;
    }


    public String toString()
    {
        return "[ 信息点=" + info_points + ",from=" + from + ",to=" + to + "]";
    }
}
