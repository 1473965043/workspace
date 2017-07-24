package com.hq.fiveonejrq.jrq.job;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.common.Utils.LogUtil;
import com.hq.fiveonejrq.jrq.common.Utils.RetrofitManage;
import com.hq.fiveonejrq.jrq.common.bean.Entity;
import com.hq.fiveonejrq.jrq.common.bean.HttpResult;
import com.hq.fiveonejrq.jrq.common.custom.MySubscriber;
import com.hq.fiveonejrq.jrq.common.interfaces.BaseResultListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public class TestActivity extends AppCompatActivity {

    private String url = "https://www.jfcaifu.com/";

    private String path = "app/index/indexBorrowsNew.html?appkey=V%2FSQ%2FyTyYjDmNLXB2unELw%3D%3D&signa=CA3B4D88E89FC21BB4077F66C0FC1E87&ts=1500371481286&user_id=20657&sgn=D090EE4AECAA8D492579689CFFC85D08";

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        getSupportActionBar().hide();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                Log.d("Retrofit",message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(10000, TimeUnit.SECONDS)
                .writeTimeout(10000, TimeUnit.SECONDS)
                .build();
    }

    public void test3(View view){
        // 创建retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())//解析方法
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//设置支持Rxjava
                .baseUrl(url)//主机地址
                .client(client)
                .build();
        Call<Map<Object, Object>> call = retrofit.create(Api.class)
        .reftrofit(url + path);
        call.enqueue(new Callback<Map<Object, Object>>() {
            @Override
            public void onResponse(Call<Map<Object, Object>> call, Response<Map<Object, Object>> response) {
                Log.i("onResponse", response.body().toString());
            }

            @Override
            public void onFailure(Call<Map<Object, Object>> call, Throwable t) {
                Log.e("onResponse", t.getMessage());
            }
        });
    }

    public void test2(View view){
        printConstructor(JobBean.class.getName());
    }

    //反射机制
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

    //RxJava与Retrofit结合使用
    public void test1(View view){
        BaseResultListener resultListener = new BaseResultListener<Entity>() {

            @Override
            public void onSuccess(Entity entity, int code, String returnMsg) {
                for (Entity.Data data: entity.getData()) {
                    LogUtil.d("onResponse", data.toString());
                }
            }

            @Override
            public void onError(String error) {
                LogUtil.e("onResponse", error);
            }
        };
        RetrofitManage.getInstance().addTask("http://apis.juhe.cn/cook/query?key=fc163e68f84eedc609f843140f856855&menu=%E8%BE%A3%E6%A4%92&rn=1&pn=1", new MySubscriber(resultListener, Entity.class));
    }

    public interface Api {

        /**
         * 半静态的url地址请求
         * 适合单节url，多节url中newsId字段“/”会转为"%2F"
         * @param newsId
         * @return
         */
        @GET("app/{newsId}")
        Call<Map<Object, Object>> reftrofit(@Path("newsId") String newsId, @QueryMap Map<String, String> map);

        /**
         * 动态的url地址请求
         * @param url
         * @return
         */
        @GET
        Call<Map<Object, Object>> reftrofit(@Url String url);

        /**
         * 与RxJava连用
         * @param url
         * @return
         */
        @GET
        Observable<News> retrofitAndRxjava(@Url String url);

    }

    public class News {

        double account;
        double account_yes;
        String name;
        String appName;

        public double getAccount() {
            return account;
        }

        public void setAccount(double account) {
            this.account = account;
        }

        public double getAccount_yes() {
            return account_yes;
        }

        public void setAccount_yes(double account_yes) {
            this.account_yes = account_yes;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        @Override
        public String toString() {
            return "News{" +
                    "account=" + account +
                    ", account_yes=" + account_yes +
                    ", name='" + name + '\'' +
                    ", appName='" + appName + '\'' +
                    '}';
        }
    }
}
