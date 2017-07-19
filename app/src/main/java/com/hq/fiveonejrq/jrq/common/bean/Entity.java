package com.hq.fiveonejrq.jrq.common.bean;

import org.json.JSONArray;

/**
 * Created by Administrator on 2017/7/19.
 */

public class Entity {
    JSONArray borrowList;
    double readNumb;

    public JSONArray getBorrowList() {
        return borrowList;
    }

    public void setBorrowList(JSONArray borrowList) {
        this.borrowList = borrowList;
    }

    public double getReadNumb() {
        return readNumb;
    }

    public void setReadNumb(double readNumb) {
        this.readNumb = readNumb;
    }
}
