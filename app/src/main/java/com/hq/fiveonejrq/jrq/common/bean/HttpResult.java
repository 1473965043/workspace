package com.hq.fiveonejrq.jrq.common.bean;

/**
 * Created by Administrator on 2017/7/19.
 * 网络数据类
 */

public class HttpResult<T> {
    //返回码
    private int res_code;
    //返回内容
    private String res_msg;
    //数据对象
    private T res_data;

    public int getRes_code() {
        return res_code;
    }

    public void setRes_code(int res_code) {
        this.res_code = res_code;
    }
}
