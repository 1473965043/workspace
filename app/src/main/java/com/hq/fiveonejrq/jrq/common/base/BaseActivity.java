package com.hq.fiveonejrq.jrq.common.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hq.fiveonejrq.jrq.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;

/**
 * Created by guodong on 2017/2/27.
 */

public abstract class BaseActivity extends FragmentActivity {

    private static final String TAG = "BaseActivity";

    /** 状态栏默认颜色 */
    private int colorId = android.R.color.transparent;
    private View titleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.MyTranslucentTheme);
        super.onCreate(savedInstanceState);
        EaseUI.getInstance().init(this, null);
        EMClient.getInstance().setDebugMode(true);
        setContentView(setLayoutId());
        setStatusBarBackgroundColor();
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
//            Log.e("数量", ""+contentView.getChildCount());
            if (childAt != null) {
                //设置跟布局风格
                childAt.setFitsSystemWindows(true);
            }
            //给statusbar着色
            if(null == titleView){
                titleView = new View(this);
                titleView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(this)));
                titleView.setBackgroundColor(colorid);
                contentView.addView(titleView);
            }else{
                titleView.setBackgroundColor(colorid);
            }
        }
    }

    protected void setStatusBarBackgroundColor(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE ;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return -1;
    }

    protected View getTitleView(){
        if(titleView == null){
            try{
                throw new NullPointerException("titleView is null");
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        return titleView;
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
