package com.hq.fiveonejrq.jrq.common.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hq.fiveonejrq.jrq.common.Utils.DownUtils;
import com.hq.fiveonejrq.jrq.common.Utils.Util;
import com.hq.fiveonejrq.jrq.common.bean.FileDownBean;

/**
 * Created by guodong on 2017/6/5.
 * 后台下载服务
 */

public class DownService extends Service{

    private DownUtils mDownUtils;

    /**
     * 与ServiceConnection连用
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    /**
     * 与onStartCommand连用
     * @param service
     * @return
     */
    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyBinder extends Binder {

        /**
         * 下载操作
         * @param statusListener 下载监听器
         */
        public void startDown(DownUtils.FileDownStatusListener statusListener){
            mDownUtils.setStatusListener(statusListener);
            mDownUtils.downFile();
        }

        /**
         * 配置参数
         * @param path 地址
         * @param context 上线文
         * @param fileName 文件名
         */
        public void setParameters(String path, Context context, String fileName){
            String targetPath = Util.getFileTargetPath(context, fileName);
            mDownUtils = new DownUtils(new FileDownBean(path, targetPath));
        }

        public DownUtils getDownUtils(){
            return mDownUtils;
        }
    }
}
