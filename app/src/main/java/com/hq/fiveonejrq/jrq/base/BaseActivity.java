package com.hq.fiveonejrq.jrq.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hq.fiveonejrq.jrq.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by guodong on 2017/2/27.
 */

public abstract class BaseActivity extends AppCompatActivity {

    /** 状态栏默认颜色 */
    private int colorId = Color.TRANSPARENT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.MyTranslucentTheme);
        super.onCreate(savedInstanceState);
        EaseUI.getInstance().init(this, null);
        EMClient.getInstance().setDebugMode(true);
        setContentView(setLayoutId());
        setStatusBarBackgroundColor(colorId);
        initViews();
        initEvents();
        initDatas();
    }

    /**
     * 设置状态栏颜色
     * @param colorid 状态栏颜色
     */
    protected void setStatusBarBackgroundColor(int colorid){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //设置contentview为fitsSystemWindows
            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
            //获得跟布局
            View childAt = contentView.getChildAt(0);
            Log.e("数量", ""+contentView.getChildCount());
            if (childAt != null) {
                //设置跟布局风格
                childAt.setFitsSystemWindows(true);
            }
            //给statusbar着色
            View view = new View(this);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(this)));
            view.setBackgroundColor(colorid);
            contentView.addView(view);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 设置布局文件
     * @return
     */
    protected abstract int setLayoutId();

    /**
     * 找到控件
     */
    protected abstract void initViews();

    /**
     * 初始化事件
     */
    protected abstract void initEvents();

    /**
     * 初始化数据
     */
    protected abstract void initDatas();
}
