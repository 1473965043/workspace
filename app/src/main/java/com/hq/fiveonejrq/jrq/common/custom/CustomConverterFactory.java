package com.hq.fiveonejrq.jrq.common.custom;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.hq.fiveonejrq.jrq.common.Utils.LogUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/7/20.
 * 自定义factory实现直接接收Json String
 */

public class CustomConverterFactory extends Converter.Factory {

    public static CustomConverterFactory create(){
        return new CustomConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new CustomResponseBodyConverter();
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new CustomRequestBodyConverter();
    }

    public class CustomResponseBodyConverter implements Converter<ResponseBody, Object>{

        @Override
        public Object convert(ResponseBody value) throws IOException {
            //获取value的值
            String response = value.string();
            //返回json字符串
            LogUtil.d(LogUtil.NETWORK, response);
            return response;
        }
    }

    public class CustomRequestBodyConverter<T> implements Converter<T, RequestBody>{

        @Override
        public RequestBody convert(T value) throws IOException {
            String string = new Gson().toJson(value);
            LogUtil.i(LogUtil.NETWORK, string);
            return RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), string);
        }
    }
}
