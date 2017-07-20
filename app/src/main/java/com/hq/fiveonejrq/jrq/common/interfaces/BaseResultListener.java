package com.hq.fiveonejrq.jrq.common.interfaces;

/**
 * Created by guodong on 2017/7/18.
 * 网络请求结果监听器
 */

public interface BaseResultListener<T> {

//    /**
//     * 请求成功回调
//     * @param response 服务器返回的数据
//     */
//    void onSuccess(String response, int code, String returnMsg);

    /**
     * 请求成功回调
     * @param t 服务器返回的数据
     * @param code 返回码
     * @param returnMsg 返回内容
     */
    void onSuccess(T t, int code, String returnMsg);

    /**
     * 请求出错回调
     * @param error 错误消息
     */
    void onError(String error);

    public class MyListener<T> implements BaseResultListener<T>{

//        @Override
//        public void onSuccess(String response, int code, String returnMsg) {
//            return;
//        }

        @Override
        public void onSuccess(T t, int code, String returnMsg) {
            return;
        }

        @Override
        public void onError(String error) {
            return;
        }
    }
}
