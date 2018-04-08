package com.hq.fiveonejrq.jrq.common.interfaces;

import com.hq.fiveonejrq.jrq.common.bean.HttpResult;
import com.hq.fiveonejrq.jrq.common.bean.OilPrice;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

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
    Observable<HttpResult<List<OilPrice>>> onGetData(@Url String url);

    @GET
    Observable<String> onGetDatas(@Url String url);

    /**
     * post请求获取服务器数据
     * @param url 带参数，且拼接好的网址
     * @return
     */
    @POST
    Observable<Map<String, String>> onPostData(@Url String url);
}
