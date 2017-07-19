package com.hq.fiveonejrq.jrq.common.interfaces;

/**
 * Created by guodong on 2017/7/18.
 * 网络请求结果监听器
 */

public interface BaseResultListener<T> {

    /**
     * 请求成功回调
     * @param t 服务器返回的数据
     */
    void onSuccess(T t);

    /**
     * 请求出错回调
     * @param error 错误消息
     */
    void onError(String error);
}
