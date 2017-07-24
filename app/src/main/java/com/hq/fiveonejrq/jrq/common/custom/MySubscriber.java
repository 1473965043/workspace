package com.hq.fiveonejrq.jrq.common.custom;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hq.fiveonejrq.jrq.common.Utils.LogUtil;
import com.hq.fiveonejrq.jrq.common.bean.Entity;
import com.hq.fiveonejrq.jrq.common.bean.HttpResult;
import com.hq.fiveonejrq.jrq.common.interfaces.BaseResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/7/19.
 */

public class MySubscriber extends Subscriber<String> {

    private BaseResultListener mBaseResultListener;

    private Gson mGson;

    private Type objectType;

    /**
     * 初始化工作，只能做当前线程能做的事，不能做显示工作
     */
    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d(LogUtil.NETWORK, "=========start========");
    }

    @Override
    public void onCompleted() {
        LogUtil.d(LogUtil.NETWORK, "=========finished========");
    }

    @Override
    public void onError(Throwable e) {
        mBaseResultListener.onError(e.getMessage());
    }

    @Override
    public void onNext(String str) {
        HttpResult result = mGson.fromJson(str, objectType);
        mBaseResultListener.onSuccess(result.getResult(), result.getError_code(), result.getReason());
    }

    public MySubscriber(BaseResultListener listener, Class clazz){
        mGson = new Gson();
        mBaseResultListener = listener;
//        objectType = type(HttpResult.class, clazz);
    }

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
