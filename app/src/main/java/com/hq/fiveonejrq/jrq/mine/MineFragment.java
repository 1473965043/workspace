package com.hq.fiveonejrq.jrq.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hq.fiveonejrq.jrq.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.openapi.RefreshTokenApi;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/28.
 */

public class MineFragment extends Fragment {

    private Button btn, btn1;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;
    private AuthInfo mAuthInfo;
    /** 请求参数 */
    private String str = "";
    /** 登陆认证对应的listener */
    private AuthListener mAuthListener = new AuthListener();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_layout, container, false);
        initViews(view);
        initData();
        return view;
    }

    private void initViews(View view){
        btn = (Button) view.findViewById(R.id.show);
        btn1 = (Button) view.findViewById(R.id.shouquan);
    }

    private void initData() {
        // 创建授权认证信息
        mAuthInfo = new AuthInfo(getContext(), Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(getActivity(), mAuthInfo);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler.authorize(mAuthListener);
            }
        });
    }

    /**
     * 获取用户信息
     *
     */
    private void getInfo(){
        final String url = "https://api.weibo.com/2/friendships/friends.json";
        final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        final Request request = new Request.Builder().url(url+str).build();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    Log.e("response", "response == " + response+"\n"+url+str);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                str = "?access_token="+accessToken.getToken()+"&uid="+accessToken.getUid();
                AccessTokenKeeper.writeAccessToken(getContext(), accessToken);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getContext(), "取消授权", Toast.LENGTH_SHORT).show();
        }
    }
}
