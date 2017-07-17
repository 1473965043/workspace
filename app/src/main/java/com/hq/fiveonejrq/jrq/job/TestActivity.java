package com.hq.fiveonejrq.jrq.job;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hq.fiveonejrq.jrq.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    private String url = "http://apis.baidu.com/txapi/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportActionBar().hide();
    }

    public void onclick1(View view){
        printConstructor(JobBean.class.getName());
    }

    public static void printConstructor(String className) {
        Log.i("Reflect", className);
        try {
            Class<?> aClass = Class.forName(className);
            Constructor<?>[] constructors = aClass.getConstructors();
            for (int i = 0; i < constructors.length; i++) {
                Log.i("Reflect", "公共构造方法：  "+constructors[i]);
            }
            Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
            for (int i = 0; i < declaredConstructors.length; i++) {
                Log.i("Reflect", "所有构造方法：  "+declaredConstructors[i]);
            }
            Log.i("Reflect", "===============父类与父类接口========================");
            Log.i("Reflect", "父类：" + aClass.getSuperclass().getName());
            Class<?> intes[] = aClass.getInterfaces();
            for (int i = 0; i < intes.length; i++) {
                Log.i("Reflect", "所有父接口：" + intes[i]);
            }
            Log.i("Reflect", "===============本类属性========================");
            java.lang.reflect.Field[] field = aClass.getDeclaredFields();
            for (int i = 0; i < field.length; i++) {
                String modifier = Modifier.toString(field[i].getModifiers());//属性修饰符
                String typeName = field[i].getType().getName();//属性类型
                String fieldName = field[i].getName();//属性名
                Log.i("Reflect", "本类属性： "+ modifier + typeName + fieldName);
            }
            Log.i("Reflect", "===============父类属性========================");
            java.lang.reflect.Field[] fields = aClass.getFields();
            for (int i = 0; i < fields.length; i++) {
                String modifier = Modifier.toString(fields[i].getModifiers());//属性修饰符
                String typeName = fields[i].getType().getName();//属性类型
                String fieldName = fields[i].getName();//属性名
                Log.i("Reflect", "父类属性： "+ modifier + typeName + fieldName);
            }
            Log.i("Reflect", "===============调用类方法========================");
            Method method = aClass.getMethod("publics", String.class);
            method.invoke(aClass.getDeclaredConstructor().newInstance(), "1");
            //调用私有构造函数必须加c.setAccessible(true);
            Constructor c = aClass.getDeclaredConstructor(String.class);
            c.setAccessible(true);
            method.invoke(c.newInstance("1"), "1");
            method.invoke(aClass.getDeclaredConstructor(int.class).newInstance(1), "1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onclick(View view){
        // 创建retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())//解析方法
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//设置支持Rxjava
                .baseUrl(url)//主机地址
                .build();
        // 实例化我们的moviesService对象
        retrofit.create(Api.class).tiYu("world", "10", "10")
        .subscribeOn(Schedulers.io())//添加耗时线程
        .doOnSubscribe(new Action0() {
            @Override
            public void call() {
                Toast.makeText(TestActivity.this, "初始化", Toast.LENGTH_SHORT).show();
            }
        })
        .subscribeOn(AndroidSchedulers.mainThread())//初始化线程
        .observeOn(AndroidSchedulers.mainThread())//主线程显示数据
        .subscribe(new Observer<News>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("onResponse:", e.getMessage());
            }

            @Override
            public void onNext(News news) {
                Log.i("onResponse:", "结果");
            }
        });

    }

    public interface Api {

        @Headers({"apikey:81bf9da930c7f9825a3c3383f1d8d766" ,"Content-Type:application/json"})
        @GET("world/world")
        Call<News> getNews(@Query("num") String num,@Query("page")String page);

        @FormUrlEncoded
        @Headers({"apikey:81bf9da930c7f9825a3c3383f1d8d766" ,"Content-Type:application/json"})
        @POST("world/world")
        Call<News> postNews(@Field("num") String num, @Field("page")String page);

        @Headers({"apikey:81bf9da930c7f9825a3c3383f1d8d766" ,"Content-Type:application/json"})
        @GET("{type}/{type}")
        Observable<News> tiYu(@Path("type") String type, @Query("num") String num,@Query("page")String page);

        @Headers({"apikey:81bf9da930c7f9825a3c3383f1d8d766" ,"Content-Type:application/json"})
        @GET("world/world")
        Call<News> tiYu(@Query("num") String num,@Query("page")String page);

        @Headers({"apikey:81bf9da930c7f9825a3c3383f1d8d766" ,"Content-Type:application/json"})
        @GET("{type1}/{type2}")
        Call<News> tiYu(@Path("type1") String type1,@Path("type2") String type2,  @Query("num") String num,@Query("page")String page);

        @FormUrlEncoded
        @Headers({"apikey:81bf9da930c7f9825a3c3383f1d8d766" ,"Content-Type:application/json"})
        @POST("keji/keji")
        Call<News> keji(@Query("num") String num,@Query("page")String page);

    }

    public class News {

        public int code;
        public String msg;
        /**
         * ctime : 2016-10-01 18:13
         * title : 新加坡防长提南海争端：各国恐因渔船相持不下
         * description : 搜狐国际
         * picUrl : http://photocdn.sohu.com/20161001/Img469502343_ss.jpeg
         * url : http://news.sohu.com/20161001/n469502342.shtml
         */

        public List<NewslistBean> newslist;

        public class NewslistBean {
            public String ctime;
            public String title;
            public String description;
            public String picUrl;
            public String url;

            @Override
            public String toString() {
                return "NewslistBean{" +
                        "ctime='" + ctime + '\'' +
                        ", title='" + title + '\'' +
                        ", description='" + description + '\'' +
                        ", picUrl='" + picUrl + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "News{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", newslist=" + newslist +
                    '}';
        }
    }
}
