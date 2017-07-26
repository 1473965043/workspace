package com.hq.fiveonejrq.jrq.common.custom;

import com.hq.fiveonejrq.jrq.common.interfaces.BaseResultListener;

import java.util.List;

/**
 * Created by guodong on 2017/7/25.
 */

public abstract class CustomResultsListener<T> implements BaseResultListener {

    public abstract void onSuccess(List<T> list, int code, String returnMsg);

    /** 结束操作 */
    public void onCompleted(){}

    /** 开始操作 */
    public void onStart() {}

    @Override
    public void onSuperSuccess(String response, int code, String returnMsg) {}

}
