package com.hq.fiveonejrq.jrq.common.bean;

import com.hq.fiveonejrq.jrq.common.Utils.LogUtil;
import com.hq.fiveonejrq.jrq.common.interfaces.BaseResultListener;

import org.json.JSONObject;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/7/19.
 */

public class MySubscriber extends Subscriber<JSONObject> {

    private BaseResultListener mBaseResultListener;

    /**
     * 初始化工作，只能做当前线程能做的事，不能做显示工作
     */
    @Override
    public void onStart() {
        super.onStart();
        LogUtil.logD(LogUtil.NETWORK, "=========start========");
    }

    @Override
    public void onCompleted() {
        LogUtil.logD(LogUtil.NETWORK, "=========finished========");
    }

    @Override
    public void onError(Throwable e) {
        mBaseResultListener.onError(e.getMessage());
    }

    @Override
    public void onNext(JSONObject object) {
        LogUtil.logD(LogUtil.NETWORK, object.toString());
        mBaseResultListener.onSuccess(object.toString());
    }

    public MySubscriber(BaseResultListener listener){
        mBaseResultListener = listener;
    }
}
