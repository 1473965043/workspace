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
     * 移动过程中Y方向上的坐标
     */
    private int moveY = 0;

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("result", "onTouchEvent");
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //获取按下去的Y坐标
                initialY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (event.getY() - moveY);
                moveY = (int) event.getY();
                //赋值distance
                distance = (int) (event.getY() - initialY);
                if(dy > 0){//下滑
                    if(recyclerView.canScrollVertically(-1)){
                        int position = recyclerView.getScrollY() - dy;
                        recyclerView.scrollBy(0, position);
                       break;
                    }
                    if(distance > 0){//recyclerView到达顶部，达到下拉刷新的条件
                        if(refreshing){//正在刷新
                            distance = refreshDist + distance/2;
                        }else{
                            distance = distance/2;
                        }
                    }
                    changeDownLayout(distance);
                    Log.i("result", "dy > 0");
                }else{//上滑
                    if(refreshing){//正在刷新
                        distance = refreshDist + distance/2;
                    }else{
                        distance = distance/2;
                    }
                    if(distance < 0){//滑动变化量小于零，表示滑到初始位置的上方
                        int position = Math.abs(dy);
//                        ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(0, position);
                        recyclerView.scrollBy(0, position);
                        distance = 0;
                        consumeStatus = true;
                        moveStatus = true;
                        Log.i("result", "dy < 0");
                    }
                    changeDownLayout(distance);
                }
                Log.e("result", "onTouchEvent == " + MotionEvent.ACTION_MOVE);
                break;
            case MotionEvent.ACTION_UP:
                changeUpLayout(distance);
                break;
        }
        Log.e("result", "consumeStatus == " + consumeStatus);
        return !consumeStatus;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("result", "onInterceptTouchEvent == " + moveStatus);
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            Log.e("result", "ACTION_DOWN");
            return !moveStatus;
        }else if(ev.getAction() == MotionEvent.ACTION_MOVE){
            Log.e("result", "ACTION_MOVE");
            return !moveStatus;
        }
        return super.onInterceptTouchEvent(ev);
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
