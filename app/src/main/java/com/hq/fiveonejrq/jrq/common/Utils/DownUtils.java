package com.hq.fiveonejrq.jrq.common.Utils;

import android.util.Log;
import android.widget.Toast;

import com.hq.fiveonejrq.jrq.common.bean.DownThread;
import com.hq.fiveonejrq.jrq.common.bean.FileDownBean;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by guodong on 2017/6/5.
 * 下载工具类
 */

public class DownUtils {

    /**
     * 线程个数
     */
    public static final int THREADCOUNT = 3;

    /**
     * 线程完成的个数
     */
    private int threadFinishCount = 0;

    /**
     * 需要下载的文件类对象
     */
    private FileDownBean mFileDownBean;

    /**
     * 下载的总长度
     */
    private int totalDownloadLength = 0;

    /**
     * 文件大小
     */
    private int fileSize;

    /**
     * 获取服务器上文件的大小
     * @return
     */
    public int getDownFileLength(){
        return fileSize;
    }

    public DownUtils(FileDownBean fileDownBean){
        this.mFileDownBean = fileDownBean;
    }

    /**
     * 定义文件下载状态接口
     */
    public interface FileDownStatusListener{
        /**
         * 下载进度
         * @param progress 进度
         */
        void onProgress(int progress);

        /**
         * 下载完成
         * @param targetPath 文件的保存地址
         */
        void onCompleted(String targetPath);

        /**
         * 下载失败
         * @param error 错误信息
         */
        void onDownloadFailed(String error);
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

    /**
     * 下载操作
     */
    public void downFile(){
        try {
            //连接服务器
            URL url = new URL(mFileDownBean.getPath());
            //获取HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式
            conn.setRequestMethod("GET");
            //设置请求超时
            conn.setConnectTimeout(5000);
            //请求码
            int code = conn.getResponseCode();
            if(code == 200){
                //从服务器上获取文件长度
                int length = conn.getContentLength();
                fileSize = length;
                int blockSize = length/THREADCOUNT;
                Log.d("result", "文件长度 == " + length);
                for(int threadId = 1; threadId <= THREADCOUNT; threadId ++){
                    int startIndex = (threadId - 1) * blockSize;//开始位置下标
                    int endIndex = threadId * blockSize - 1;//结束位置下标
                    if(threadId == THREADCOUNT){
                        endIndex = length;
                    }
                    Log.d("result", "线程" + threadId + "：从" + startIndex + "开始下载，到" + endIndex + "结束");
                    DownThread downThread = new DownThread(
                            mFileDownBean.getPath(),
                            startIndex,
                            endIndex,
                            threadId,
                            mFileDownBean.getTargetPath());
                    downThread.setOnLengthChangeListenenr(mOnLengthChangeListenenr);
                    downThread.start();
                }
            }else{
                if(mStatusListener != null){
                    mStatusListener.onDownloadFailed("网络异常...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(mStatusListener != null){
                mStatusListener.onDownloadFailed(e.getMessage());
            }
        }
    }

    /**
     * 下载线程下载监听
     */
    private DownThread.OnLengthChangeListenenr mOnLengthChangeListenenr = new DownThread.OnLengthChangeListenenr() {
        @Override
        public void onProgress(int length) {
            totalDownloadLength += length;
            if(mStatusListener != null){
                mStatusListener.onProgress(totalDownloadLength);
            }
        }

        @Override
        public void onFailed(String error, int threadId) {
            if(mStatusListener != null){
                mStatusListener.onDownloadFailed(error);
            }
        }

        @Override
        public void onCompleted(String targetPath) {
            threadFinishCount ++;
            if(threadFinishCount < THREADCOUNT){
                return;
            }
            //当所有线程都现在完成时才算文件下载成功
            if(mStatusListener != null){
                mStatusListener.onCompleted(targetPath);
            }
        }
    };
}
