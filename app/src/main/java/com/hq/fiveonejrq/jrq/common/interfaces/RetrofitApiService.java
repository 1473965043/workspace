package com.hq.fiveonejrq.jrq.common.interfaces;

import com.hq.fiveonejrq.jrq.common.bean.HttpResult;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/7/19.
 */

public interface RetrofitApiService {

    /**
     * get请求获取服务器数据
     * @param url 带参数，且拼接好的网址
     * @return
     */
    @GET
    Observable<String> onGetData(@Url String url);

    /**
     * post请求获取服务器数据
     * @param url 带参数，且拼接好的网址
     * @return
     */
    @POST
    Observable<Map<String, String>> onPostData(@Url String url);
}
