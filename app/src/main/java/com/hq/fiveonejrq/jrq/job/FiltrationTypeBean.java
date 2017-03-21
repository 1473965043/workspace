package com.hq.fiveonejrq.jrq.job;

import java.util.ArrayList;

/**
 * Created by guodong on 2017/3/20.
 * 筛选条件类，包括标题，数据集合
 */

public class FiltrationTypeBean {

    /**
     * 是否为单选
     */
    private boolean isSingleSelection = false;

    /**
     * 数据集合
     */
    private ArrayList<String> list;

    /**
     * 标题
     */
    private String title;

    public boolean isSingleSelection() {
        return isSingleSelection;
    }

    public void setSingleSelection(boolean singleSelection) {
        isSingleSelection = singleSelection;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
