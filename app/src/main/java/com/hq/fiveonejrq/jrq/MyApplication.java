package com.hq.fiveonejrq.jrq;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.hq.fiveonejrq.jrq.common.Utils.LogUtil;
import com.hq.fiveonejrq.jrq.common.service.DaemonService;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

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
        JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
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
