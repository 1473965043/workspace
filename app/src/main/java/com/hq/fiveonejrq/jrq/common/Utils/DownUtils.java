package com.hq.fiveonejrq.jrq.common.Utils;

import com.hq.fiveonejrq.jrq.common.bean.FileDownBean;

/**
 * Created by guodong on 2017/6/5.
 * 下载工具类
 */

public class DownUtils {

    private FileDownBean mFileDownBean;

    public DownUtils(FileDownBean fileDownBean){
        this.mFileDownBean = fileDownBean;
    }

    /**
     * 定义文件下载状态接口
     */
    public interface FileDownStatusListener{
        void onProgress(int progress);//下载进度
        void onCompleted();//下载完成
        void onDownloadFailed(String error);//下载失败
    }

    /**
     * 申明文件下载状态接口
     */
    private FileDownStatusListener mStatusListener;

    /**
     * 设置文件下载状态接口
     */
    public void setStatusListener(FileDownStatusListener statusListener){
        this.mStatusListener = statusListener;
    }
}
