package com.hq.fiveonejrq.jrq.common.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hq.fiveonejrq.jrq.R;

/**
 * Created by guodong on 2017/3/3.
 * （带recyclerview）组合式下拉刷新
 */

public class MyRecyclerView extends LinearLayout{

    public static final int REFRESH_TO_NORMAL = 0;
    /**
     * 刷新状态---“下拉刷新”状态
     */
    public static final int REFRESH_TO_PULLDOWN = 1;

    /**
     * 刷新状态---“释放立即刷新”状态
     */
    public static final int REFRESH_TO_LOOSEN = 2;

    /**
     * 刷新状态---“正在刷新”状态
     */
    public static final int REFRESH_TO_LOADING = 3;

    /**
     * 刷新状态---“刷新成功”状态
     */
    public static final int REFRESH_TO_SUCCESS = 4;

    /**
     * 刷新状态---“刷新失败”状态
     */
    public static final int REFRESH_TO_FAIL = 5;

    /**
     * move事件---上滑与下滑
     * false为下滑，true上滑
     */
    private boolean moveStatus = false;

    /**
     * 自己是否消费触摸事件
     * false为不消费，true消费
     */
    private boolean consumeStatus = false;

    /**
     * 刷新布局
     */
    private View refreshView;

    /**
     * 内容布局
     */
    private RecyclerView recyclerView;

    /**
     * 是否为第一次加载
     */
    private boolean layoutOnce = false;

    /**
     * loadingView高度
     */
    private int loadingViewHeight = 0;

    /**
     * loadingView宽度
     */
    private int loadingViewWidth = 0;

    /**
     * 点击屏幕时手指按下去位置的Y坐标
     */
    private int initialY = 0;

    /**
     * 滑动时的始末间距
     */
    private int distance = 0;

    /**
     * 释放刷新时下拉的距离(控件的高度)
     */
    private int refreshDist = 0;

    /**
     * 状态显示textview
     */
    private TextView statusStr;

    /**
     * 判断正在刷新状态
     */
    private boolean refreshing = false;

    /**
     * 判断刷新完成
     */
    private boolean refreshFinished = false;

    /**
     * 刷新加载回调接口
     */
    public interface OnRefreshListener
    {
        /**
         * 刷新操作
         */
        void onRefresh();

        /**
         * 加载操作
         */
        void onLoadMore();
    }

    /**
     * 刷新监听器
     */
    public OnRefreshListener mRefreshListener;

    /**
     * 设置监听器
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener){
        mRefreshListener = listener;
    }

    /**
     * 构造函数，此方法在xml中使用此控件是调用
     * @param context
     * @param attrs
     */
    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        refreshView = LayoutInflater.from(getContext()).inflate(R.layout.loading_layout, null);
        setOrientation(VERTICAL);
        addView(refreshView, 0);
    }

    private Handler mHandler = new Handler();

    /**
     * 加载布局
     * @param changed 判断布局是否改变
     * @param l 左边框距离父控件左边框的距离
     * @param t 上边框距离父控件上边框的距离
     * @param r 右边框距离父控件左边框的距离
     * @param b 下边框距离父控件上边框的距离
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(!layoutOnce){
            loadingViewHeight = refreshView.getMeasuredHeight();
            loadingViewWidth = refreshView.getMeasuredWidth();
            refreshDist = refreshView.getMeasuredHeight();
            statusStr = (TextView) refreshView.findViewById(R.id.refresh_status);
            recyclerView = (RecyclerView) getChildAt(1);
//            recyclerView.setOnTouchListener(this);
            layoutOnce = true;
        }
        refreshView.layout(0, distance - loadingViewHeight, loadingViewWidth, distance);
        recyclerView.layout(0, distance, loadingViewWidth, distance + getHeight());
    }

    /**
     * 事件消费
     * 返回true则代表消费了事件
     * @param event 事件
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("result", "onTouchEvent");
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                //获取按下去的Y坐标
//                Log.e("result", "ACTION_DOWN");
//                initialY = (int) event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //移动的变化量
//                Log.e("result", "ACTION_MOVE");
//                int moveY = (int) (event.getY() - initialY);
//                //赋值distance
//                distance = moveY;
//                //recyclerView到达顶部
//                if(!recyclerView.canScrollVertically(-1)){
//                    if(refreshing){//正在刷新
//                        changeDownLayout(refreshDist + distance/2);
//                    }else{
//                        changeDownLayout(distance/2);
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.e("result", "ACTION_UP");
//                changeUpLayout(distance);
//                break;
//        }
        return super.onTouchEvent(event);
    }

    /**
     * 事件拦截 返回true则表示拦截向下传递事件，自己消费事件调用onTouchEvent
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("result", "onInterceptTouchEvent");
//        if(ev.getAction() == MotionEvent.ACTION_MOVE){
//            if(recyclerView.getTop()<0) {
//                Log.e("result", "top == " + recyclerView.getTop());
//                return false;
//            }else{
//
//            }
//        }
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                //获取按下去的Y坐标
//                Log.e("result", "ACTION_DOWN");
//                initialY = (int) ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //移动的变化量
//                Log.e("result", "ACTION_MOVE");
//                int moveY = (int) (ev.getY() - initialY);
//                //赋值distance
//                distance = moveY;
//                //recyclerView到达顶部
//                if(!recyclerView.canScrollVertically(-1)){
//                    if(refreshing){//正在刷新
//                        changeDownLayout(refreshDist + distance/2);
//                    }else{
//                        changeDownLayout(distance/2);
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.e("result", "ACTION_UP");
//                changeUpLayout(distance);
//                break;
//        }  

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 事件分发 super.dispatchTouchEvent(ev)源码里有调用onInterceptTouchEvent方法的代码
     * 不执行super.dispatchTouchEvent(ev),则不会调用自己的onInterceptTouchEvent方法
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("result", "dispatchTouchEvent");
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //获取按下去的Y坐标
                Log.e("result", "ACTION_DOWN");
                initialY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动的变化量
                Log.e("result", "ACTION_MOVE");
                int moveY = (int) (ev.getY() - initialY);
                //赋值distance
                distance = moveY;
                //recyclerView到达顶部
                if(!recyclerView.canScrollVertically(-1)){
                    if(refreshing){//正在刷新
                        changeDownLayout(refreshDist + distance/2);
                    }else{
                        changeDownLayout(distance/2);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("result", "ACTION_UP");
                changeUpLayout(distance);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 按下拖动时更新布局
     * @param change 下拉变化量
     */
    private void changeDownLayout(int change){
        if(change > refreshDist){
            if(refreshing){
                changeStatus(REFRESH_TO_LOADING);
            }else{
                changeStatus(REFRESH_TO_LOOSEN);
            }
        }else if(change > 0 && change <= refreshDist){
            if(refreshing){
                changeStatus(REFRESH_TO_LOADING);
            }else{
                changeStatus(REFRESH_TO_PULLDOWN);
            }
        }
        distance = change;
        requestLayout();
    }

    /**
     * 松开/放开是刷新布局
     * @param change 变化量
     */
    private void changeUpLayout(int change){
        if(change > refreshDist){
            change = refreshDist;
            changeStatus(REFRESH_TO_LOADING);
        }else if(change > 0 && change <= refreshDist){
            change = 0;
            changeStatus(REFRESH_TO_NORMAL);
        }else if(change == 0){
            changeStatus(REFRESH_TO_NORMAL);
        }
        distance = change;
        requestLayout();
    }

    /**
     * 更新下拉状态
     * @param status
     */
    private void changeStatus(int status){
        switch (status){
            case REFRESH_TO_NORMAL:
                //做一些关闭处理
                refreshing = false;
                refreshFinished = true;
                break;
            case REFRESH_TO_PULLDOWN:
                statusStr.setText("下拉刷新");
                break;
            case REFRESH_TO_LOOSEN:
                statusStr.setText("松开刷新");
                break;
            case REFRESH_TO_LOADING:
                refreshing = true;
                statusStr.setText("正在刷新...");
                //执行刷新操作
                if(mRefreshListener != null){
                    mRefreshListener.onRefresh();
                }
                break;
            case REFRESH_TO_SUCCESS:
                statusStr.setText("刷新成功");
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        changeUpLayout(0);
//                    }
//                }, 500);
                break;
            case REFRESH_TO_FAIL:
                statusStr.setText("刷新失败");
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        changeUpLayout(0);
//                    }
//                }, 500);
                break;
        }
    }

    /**
     * 刷新完成,对外公开
     */
    public void refreshFinish(){
        changeStatus(REFRESH_TO_SUCCESS);
    }

    /**
     * 刷新失败，对外公开
     */
    public void refreshFail(){
        changeStatus(REFRESH_TO_FAIL);
    }
}
