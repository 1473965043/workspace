package com.hq.fiveonejrq.jrq;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by guodong on 2017/6/5.
 * 自定义Application
 */

public class MyApplication extends MultiDexApplication {

    public static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        //初始化sdk
        SDKInitializer.initialize(this);
        JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        ZXingLibrary.initDisplayOpinion(this);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("guodong");
        JPushInterface.setTags(this, set, null);//设置标签
    }

    /**
     * 单例模式，获取MyApplication实例
     * @return
     */
    public static MyApplication getInstance(){
        if(mInstance == null){
            mInstance = new MyApplication();
        }
        return mInstance;
    }
}