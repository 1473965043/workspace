package com.hq.fiveonejrq.jrq.imageUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

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
    private String dispatchType = Constants.FIFO;

    //任务队列
    private LinkedList<Runnable> mTaskQueue = null;

    //后台轮询线程
    private Thread mPoolThread;

    //java并发
    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;

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
                        mThreadPool.execute(getTask());
                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                mSemaphorePoolThreadHandler.release();//释放信号
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

        mSemaphoreThreadPool = new Semaphore(threadCount);
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
    private void loadImage(final String path, final ImageView imageView){
        //设置tag
        imageView.setTag(path);
        //根据path从缓存中获取bitmap
        Bitmap bitmap = getBitmapFromLruche(path);
        if(bitmap != null){
            refreshBitmap(path, imageView, bitmap);
        }else{
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //获取图片需要显示的大小
                    ImageSize size = getImageSize(imageView);
                    //压缩图片
                    Bitmap bm = decodeSampledBitmapFromPath(path, size.width, size.height);
                    //把图片放到缓存中
                    addBitmapToLruCache(path, bm);
                    refreshBitmap(path, imageView, bm);

                    mSemaphoreThreadPool.release();
                }
            };
            addTask(runnable);
        }
    }

    /**
     * 刷新图片显示
     * @param path
     * @param imageView
     * @param bitmap
     */
    private void refreshBitmap(String path, ImageView imageView, Bitmap bitmap) {
        Message msg = new Message();
        ImageHolder holder = new ImageHolder();
        holder.bm = bitmap;
        holder.imageView = imageView;
        holder.path = path;
        msg.obj = holder;
        mUIHandler.sendMessage(msg);
    }

    /**
     * 根据图片显示的宽和高进行压缩
     * @param path
     * @param width
     * @param height
     * @return
     */
    private Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {
        //获取图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, width, height);
        //获取到新的inSampleSize，在对图片进行压缩
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(path, options);
        return bm;
    }

    /**
     * 根据需求的宽和高，以及图片实际的宽和高，得到SampleSize
     * @param options
     * @param width
     * @param height
     * @return
     */
    private int caculateInSampleSize(BitmapFactory.Options options, int width, int height) {
        int w = options.outWidth;
        int h = options.outHeight;
        int sampleSize = 1;
        if(w > width || h > height){
            int widthRadio = Math.round(w*1.0f/width);
            int heightRadio = Math.round(h*1.0f/height);
            sampleSize = Math.max(widthRadio, heightRadio);
        }
        return sampleSize;
    }

    /**
     * 根据imageview获取适当的压缩尺寸
     * @param imageView
     * @return
     */
    private ImageSize getImageSize(ImageView imageView){
        ImageSize size = new ImageSize();
        DisplayMetrics disPlayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        int width = imageView.getWidth();//获取imageview的实际宽度
        //lp.WRAP_CONTENT lp.MATCH_PARENT的值为-2，-1;
        if(width <= 0){
            width = lp.width;//获取imageview在layout中声明的宽度
        }
        //lp.WRAP_CONTENT lp.MATCH_PARENT的值为-2，-1;
        if(width <= 0){
            width = getImageViewFieldValue(imageView, "mMaxWidth");//检查最大宽度
        }
        //防止获取的最大值为零
        if(width <= 0){
            width = disPlayMetrics.widthPixels;//最大值为屏幕宽度
        }

        int height = imageView.getHeight();
        if(height <= 0){
            height = lp.height;//获取imageview在layout中声明的宽度
        }
        //lp.WRAP_CONTENT lp.MATCH_PARENT的值为-2，-1;
        if(height <= 0){
            height = getImageViewFieldValue(imageView, "mMaxHeight");//检查最大宽度
        }
        //防止获取的最大值为零
        if(height <= 0){
            height = disPlayMetrics.heightPixels;//最大值为屏幕宽度
        }
        size.width = width;
        size.height = height;
        return size;
    }

    /**
     * 反射获取ImageView的属性值
     */
    public int getImageViewFieldValue(Object object, String fieldName){
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if(fieldValue > 0 && fieldValue < Integer.MAX_VALUE){
                value = fieldValue;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

    //添加任务
    private synchronized void addTask(Runnable runnable) {
        try {
            mTaskQueue.add(runnable);
            if(mPoolThreadHandler == null){
                mSemaphorePoolThreadHandler.acquire();
            }
            mPoolThreadHandler.sendEmptyMessage(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片加入LruCache
     * @param path
     * @param bm
     */
    private void addBitmapToLruCache(String path, Bitmap bm) {
        if(bm != null){
            mCache.put(path, bm);
        }
    }

    //从任务队列中取出一个任务(Runnable)
    private Runnable getTask(){
        if(dispatchType.equals(Constants.FIFO)){
            return mTaskQueue.removeFirst();
        }else if(dispatchType.equals(Constants.LIFO)){
            return mTaskQueue.removeLast();
        }
        return null;
    }

    //根据path从缓存中获取bitmap
    private Bitmap getBitmapFromLruche(String path) {
        return mCache.get(path);
    }

    /**
     * 图片辅助类
     */
    private class ImageSize{
        int width;
        int height;
    }

    /**
     * 图片辅助类
     */
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
        String FIFO = "FIFO";

        //调度方式之LIFO
        String LIFO = "LIFO";
    }
}

