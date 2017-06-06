package com.hq.fiveonejrq.jrq.common.bean;

import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by guodong on 2017/6/6.
 * 自定义线程，用来从服务器下载文件
 */

public class DownThread extends Thread{

    /**
     * 线程id
     */
    private int threadId;

    /**
     * 下载的初始位置
     */
    private int startIndex;

    /**
     * 下载的结束位置
     */
    private int endIndex;

    /**
     * 要下载的文件地址
     */
    private String path;

    /**
     * 文件保存路劲
     */
    private String targetPath;

    /**
     * 下载是否成功
     */
    private boolean successful = false;

    public DownThread(String path, int startIndex, int endIndex, int threadId, String targetPath){
        this.threadId = threadId;
        this.endIndex = endIndex;
        this.startIndex = startIndex;
        this.path = path;
        this.targetPath = targetPath;
    }

    @Override
    public void run() {
        super.run();
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            //设置参数,请求服务器下载部分文件，下载原文件的某个位置到某个位置
            conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
            int code = conn.getResponseCode();
            Log.d("result", "请求下载码code = " + code);//请求全部资源code为200，部分资源为206
            //获取文件输入流
            InputStream is = conn.getInputStream();
            File targetFile = new File(targetPath);
            Log.e("result", targetPath);
            if(!targetFile.exists()){
                targetFile.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(targetFile, "rwd");
            //设置写文件的位置
            raf.seek(startIndex);
            int len = 0;
            int total = 0;//已下载的长度
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1){
                raf.write(buffer, 0, len);
                total += len;
                if(mOnLengthChangeListenenr != null){
                    mOnLengthChangeListenenr.onProgress(total);
                }
            }
            is.close();
            raf.close();
            successful = true;
            Log.d("result", "线程"+threadId+"下载完毕...");
        } catch (Exception e) {
            e.printStackTrace();
            if(mOnLengthChangeListenenr != null){
                mOnLengthChangeListenenr.onFailed(e.getMessage(), threadId);
            }
            successful = false;
        } finally {
            if(mOnLengthChangeListenenr != null && successful){
                mOnLengthChangeListenenr.onCompleted(targetPath);
            }
        }
    }

    /**
     * 下载长度变化接口
     */
    public interface OnLengthChangeListenenr{
        /**
         * 下载的长度
         * @param length
         */
        void onProgress(int length);

        /**
         * 下载失败
         * @param error 错误信息
         * @param threadId id
         */
        void onFailed(String error, int threadId);

        /**
         * 下载完成
         * @param targetPath 文件的保存地址
         */
        void onCompleted(String targetPath);
    }

    /**
     * 申明下载状态变化接口
     */
    private OnLengthChangeListenenr mOnLengthChangeListenenr;

    /**
     * 设置下载状态变化接口
     * @param onLengthChangeListenenr
     */
    public void setOnLengthChangeListenenr(OnLengthChangeListenenr onLengthChangeListenenr){
        this.mOnLengthChangeListenenr = onLengthChangeListenenr;
    }
}
