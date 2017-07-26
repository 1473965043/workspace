package com.hq.fiveonejrq.jrq.common.custom;

import com.google.gson.Gson;
import com.hq.fiveonejrq.jrq.common.Utils.LogUtil;
import com.hq.fiveonejrq.jrq.common.bean.HttpResult;
import com.hq.fiveonejrq.jrq.common.bean.HttpResults;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import rx.Subscriber;

/**
 * Created by guodong on 2017/7/19.
 * 自定义观察者
 */

public class CustomSubscriber extends Subscriber<String> {

    private CustomResultListener mResultListener;

    private CustomResultsListener mResultsListener;

    private Gson mGson;

    private Type objectType = null, objectsType = null;

    /**
     * 构造函数
     * 请求数据为单个对象实例
     */
    public CustomSubscriber(CustomResultListener listener, Class cls){
        mGson = new Gson();
        mResultListener = listener;
        objectType = type(HttpResult.class, cls);
    }

    /**
     * 构造函数
     * 请求数据为集合对象
     */
    public CustomSubscriber(CustomResultsListener listener, Class cls){
        mGson = new Gson();
        mResultsListener = listener;
        objectsType = type(HttpResults.class, cls);
    }

    /**
     * 初始化工作，只能做当前线程能做的事，不能做显示工作
     */
    @Override
    public void onStart() {
        super.onStart();
        if(mResultListener != null){
            mResultListener.onStart();
        }

        if(mResultsListener != null){
            mResultsListener.onStart();
        }
        LogUtil.d(LogUtil.NETWORK, "=========start========");
    }

    /** 结束操作 */
    @Override
    public void onCompleted() {
        if(mResultListener != null){
            mResultListener.onCompleted();
        }

        if(mResultsListener != null){
            mResultsListener.onCompleted();
        }
        LogUtil.d(LogUtil.NETWORK, "=========finished========");
    }

    /** 异常处理 */
    @Override
    public void onError(Throwable e) {
        if(mResultListener != null){
            mResultListener.onError(e.getMessage());
        }

        if(mResultsListener != null){
            mResultsListener.onError(e.getMessage());
        }
    }

    /** 数据处理 */
    @Override
    public void onNext(String str) {

        if(objectType != null && mResultListener != null){
            HttpResult result = mGson.fromJson(str, objectType);
            mResultListener.onSuperSuccess(str, result.getError_code(), result.getReason());
            mResultListener.onSuccess(result.getResult(), result.getError_code(), result.getReason());
        }

        if(objectsType != null && mResultsListener != null){
            HttpResults results = mGson.fromJson(str, objectsType);
            mResultsListener.onSuperSuccess(str, results.getError_code(), results.getReason());
            mResultsListener.onSuccess(results.getResult(), results.getError_code(), results.getReason());
        }

    }

    /**
     * 获取泛型type
     * @param raw
     * @param args
     * @return
     */
    public ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}
