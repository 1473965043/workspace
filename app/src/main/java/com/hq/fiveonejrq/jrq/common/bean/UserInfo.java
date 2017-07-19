package com.hq.fiveonejrq.jrq.common.bean;

import com.hq.fiveonejrq.jrq.common.interfaces.RetrofitService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by guodong on 2017/7/18.
 * app用户类
 */

public class UserInfo{

    /** 登录状态 */
    private boolean isLogin = false;

    /** 第一次打开App*/
    private boolean isFirstUsed = true;

    /** 唯一的类实例对象 */
    private static UserInfo mInstance;

    /** userID */
    private static String userId;

    /** 授权口令 */
    private String token;

    /** 签名 */
    private String sign;

    /** 单例模式 */
    public static UserInfo getInstance(){
        if(mInstance == null){
            mInstance = new UserInfo();
        }
        return mInstance;
    }

    public boolean getLoginStatus() {
        return isLogin;
    }

    private void setLoginStatus(boolean login) {
        isLogin = login;
    }

    public boolean getFirstUsedStatus() {
        return isFirstUsed;
    }

    private void setFirstUsedStatus(boolean firstUsed) {
        isFirstUsed = firstUsed;
    }

    private void setUserId(String id){
        userId = id;
    }

    public String getUserId(){
        return userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
