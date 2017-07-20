package com.hq.fiveonejrq.jrq.common.Utils;

import com.hq.fiveonejrq.jrq.common.custom.CustomConverterFactory;
import com.hq.fiveonejrq.jrq.common.interfaces.RetrofitApiService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/7/19.
 */

public class RetrofitManage {

    /** 唯一的实例对象 */
    private static RetrofitManage mInstance;
    /** 日志拦截器 */
    private static HttpLoggingInterceptor loggingInterceptor;

    /** 单例模式 */
    public static RetrofitManage getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitManage();
        }

        if(loggingInterceptor == null){
            loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LogUtil.logW(LogUtil.NETWORK, message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return mInstance;
    }

    /**
     * 提交任务
     * @param url 链接
     */
    public void addTask(String url, Observer observer){

        //创建OkHttpClient对象进行一些网络配置
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(NetworkUtils.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(NetworkUtils.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NetworkUtils.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        //创建Retrofit实例，添加RxJavaCallAdapterFactory、GsonConverterFactory、以及baseUrl
        Retrofit retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(CustomConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(AppUrlUtils.BASE_URL)
                .client(okHttpClient)
                .build();
        retrofit.create(RetrofitApiService.class).onGetData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
//        toRxJava(retrofit.create(RetrofitApiService.class).onGetData(url), observer);
    }

    /**
     * Retrofit与RxJava连用,发送网络请求
     * @param observable 被观察这
     * @param observer 观察者
     */
    private void toRxJava(Observable observable, Observer observer){
        observable
                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())//取消订阅线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}