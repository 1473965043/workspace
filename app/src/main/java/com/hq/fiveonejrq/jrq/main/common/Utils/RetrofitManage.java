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
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by guodong on 2017/7/19.
 */

public class RetrofitManage {

    /** 唯一的实例对象 */
    private static RetrofitManage mInstance;
    /** 日志拦截器 */
    private static HttpLoggingInterceptor loggingInterceptor;

    private OkHttpClient okHttpClient;

    private Retrofit retrofit;

    /** 单例模式 */
    public static RetrofitManage getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitManage();
        }

        if(loggingInterceptor == null){
            loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LogUtil.w(LogUtil.NETWORK, message);
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
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(NetworkUtils.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(NetworkUtils.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(NetworkUtils.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }
        //创建Retrofit实例，添加RxJavaCallAdapterFactory、GsonConverterFactory、以及baseUrl
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())//Gson解析网络数据
                    .addConverterFactory(CustomConverterFactory.create())//自定义返回数据
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(AppUrlUtils.BASE_URL)
                    .client(okHttpClient)
                    .build();
        }
        toRxJava(url, observer);
    }

    /**
     * Retrofit与RxJava连用,发送网络请求
     * @param url 请求地址
     * @param observer 观察者
     */
    private void toRxJava(String url, Observer observer){
        retrofit.create(RetrofitApiService.class).onGetData(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}