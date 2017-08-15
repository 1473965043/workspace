package com.hq.fiveonejrq.jrq.common.bean;

/**
 * Created by Administrator on 2017/7/25.
 * 全国油价类
 */

public class OilPrice {
    private String city;
    private String b90;
    private String b93;
    private String b97;
    private String b0;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getB90() {
        return b90;
    }

    public void setB90(String b90) {
        this.b90 = b90;
    }

    public String getB93() {
        return b93;
    }

    public void setB93(String b93) {
        this.b93 = b93;
    }

    public String getB97() {
        return b97;
    }

    public void setB97(String b97) {
        this.b97 = b97;
    }

    public String getB0() {
        return b0;
    }

    public void setB0(String b0) {
        this.b0 = b0;
    }

    @Override
    public String toString() {
        return "OilPrice{" +
                "city='" + city + '\'' +
                ", b90='" + b90 + '\'' +
                ", b93='" + b93 + '\'' +
                ", b97='" + b97 + '\'' +
                ", b0='" + b0 + '\'' +
                '}';
    }
}
