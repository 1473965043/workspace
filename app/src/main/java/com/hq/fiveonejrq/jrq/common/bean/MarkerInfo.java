package com.hq.fiveonejrq.jrq.common.bean;

import com.hq.fiveonejrq.jrq.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by guodong on 2017/8/7.
 * 覆盖物实体类
 */

public class MarkerInfo implements Serializable{
    /**
     * 纬度
     */
    private double latitude;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 名字
     */
    private String name;

    /**
     * 图片id
     */
    private int picId;

    /**
     * 描述
     */
    private String distance;

    /**
     * 点赞数量
     */
    private int thumbsUpNum;

    public static ArrayList<MarkerInfo> infos = new ArrayList<>();

    static {
        infos.add(new MarkerInfo(31.3024918658, 121.4944673885, "第一个地点", R.mipmap.gr, "距离XXX米", 123));
        infos.add(new MarkerInfo(31.3018134559,121.4923216213, "第二个地点", R.mipmap.ceshi, "距离XXX米", 123));
        infos.add(new MarkerInfo(31.3008416712,121.4949609149, "第三个地点", R.mipmap.user_ba, "距离XXX米", 123));
        infos.add(new MarkerInfo(31.3021159905,121.4968277327, "第四个地点", R.mipmap.gr, "距离XXX米", 123));
    }

    public MarkerInfo(double latitude, double longitude, String name, int picId, String distance, int thumbsUpNum) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.picId = picId;
        this.distance = distance;
        this.thumbsUpNum = thumbsUpNum;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getThumbsUpNum() {
        return thumbsUpNum;
    }

    public void setThumbsUpNum(int thumbsUpNum) {
        this.thumbsUpNum = thumbsUpNum;
    }
}
