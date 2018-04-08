package com.hq.fiveonejrq.jrq.common.Utils;

import com.hq.fiveonejrq.jrq.common.custom.CustomConverterFactory;
import com.hq.fiveonejrq.jrq.common.interfaces.RetrofitApiService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
        return mInstance;
    }

    private RetrofitManage(){
        loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtil.w(LogUtil.NETWORK, message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(NetworkUtils.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(NetworkUtils.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NetworkUtils.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())//Gson解析网络数据
                    .addConverterFactory(CustomConverterFactory.create())//自定义返回数据
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(AppUrlUtils.BASE_URL)
                .client(okHttpClient)
                .build();
    }

    public void addTask(String url, Observer observer){
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

    /**
     * 提交任务
     * @param
     */
    public void execute(Observable<?> observable, Observer observer){
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public <T> T getService(Class<T> cls){
        return retrofit.create(cls);
    }
}