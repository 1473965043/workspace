package com.hq.fiveonejrq.jrq.imageUtils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by guodong on 2017/9/19.
 * 图片加载类
 */

public class ImageLoadingHelper {

    //单例对象
    private static ImageLoadingHelper mHelper = null;

    //图片缓存核心对象
    private LruCache<String, Bitmap> mCache = null;

    //线程池
    private ExecutorService mThreadPool = null;

    //队列默认调度方式
    private String dispatchType = Constants.FIFI;

    //任务队列
    private LinkedList<Runnable> mTaskQueue = null;

    //后台轮询线程
    private Thread mPoolThread;

    //线程的handler对象
    private Handler mPoolThreadHandler;

    //构造函数
    private ImageLoadingHelper(int threadCount, String type){
        init(threadCount, type);
    }

    //获取单利对象
    public static ImageLoadingHelper getInstance() {
        /*
            1、双重判断
            2、同步处理
            目的：提高处理效率
            原因：当mHelper=null时，可能有多个线程进入getInstance()方法；加入synchronized
            使得程序有序进行，双重判断防止创建多个对象
         */
        if(mHelper == null){
            synchronized (ImageLoadingHelper.class){
                if(mHelper == null){
                    mHelper = new ImageLoadingHelper(Constants.DEAFULT_THREAD_COUNT, Constants.LIFO);
                }
            }
        }
        return mHelper;
    }

    //初始化
    private void init(int threadCount, String type){
        //后台轮询线程
        mPoolThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        //到线程池中取出一个任务去执行

                    }
                };
                Looper.loop();//循环
            }
        };
        mPoolThread.start();
        //获取应用的最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory/8;
        mCache = new LruCache<String, Bitmap>(cacheMemory){
            //测量bitmap的大小 size = 每行字节*高度
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<>();
        dispatchType = type;
    }

    //UI的handler对象
    private Handler mUIHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //获取图片，让imageview显示
            ImageHolder holder = (ImageHolder) msg.obj;
            //判断tag防止图片乱跳
            if(holder.imageView.getTag().equals(holder.path)){
                holder.imageView.setImageBitmap(holder.bm);
            }
        }
    };

    //根据path加载图片
    private void loadImage(String path, ImageView imageView){
        //设置tag
        imageView.setTag(path);
        //根据path从缓存中获取bitmap
        Bitmap bitmap = getBitmapFromLruche(path);
        if(bitmap != null){
            Message msg = new Message();
            ImageHolder holder = new ImageHolder();
            holder.bm = bitmap;
            holder.imageView = imageView;
            holder.path = path;
            msg.obj = holder;
            mUIHandler.sendMessage(msg);
        }
    }

    //根据path从缓存中获取bitmap
    private Bitmap getBitmapFromLruche(String path) {
        return mCache.get(path);
    }

    private class ImageHolder{
        String path;
        Bitmap bm;
        ImageView imageView;
    }

    /**
     * 常量
     */
    public interface Constants {

        //默认线程池数量
        int DEAFULT_THREAD_COUNT = 1;

        //调度方式之FIFI
        String FIFI = "FIFI";

        //调度方式之LIFO
        String LIFO = "LIFO";
    }
}

