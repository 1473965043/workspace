package com.hq.fiveonejrq.jrq.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hq.fiveonejrq.jrq.R;

/**
 * Created by guodong on 2017/3/9.
 */

public class RefreshView extends LinearLayout implements View.OnTouchListener {

    /**
     * 刷新状态---“正常”状态
     */
    public final int REFRESH_TO_NORMAL = 0;

    /**
     * 刷新状态---“下拉刷新”状态
     */
    public final int REFRESH_TO_PULLDOWN = 1;

    /**
     * 刷新状态---“释放立即刷新”状态
     */
    public final int REFRESH_TO_LOOSEN = 2;

    /**
     * 刷新状态---“正在刷新”状态
     */
    public final int REFRESH_TO_LOADING = 3;

    /**
     * 刷新状态---“刷新成功”状态
     */
    public final int REFRESH_TO_SUCCESS = 4;

    /**
     * 触摸事件---move
     */
    private final int moveStatus = 5;

    /**
     * 触摸事件---up
     */
    private final int upStatus = 6;

    /**
     * loading布局
     */
    private View refreshView;

    /**
     * 下拉图标
     */
    private ImageView pullIcon;

    /**
     * 刷新图标
     */
    private ImageView loadingIcon;

    /**
     * 刷新成功图标
     */
    private ImageView successIcon;

    /**
     * 状态显示textview
     */
    private TextView statusStr;

    /**
     * 下拉箭头的转180°动画
     */
    private RotateAnimation rotateAnimation;

    /**
     * 均匀旋转动画
     */
    private RotateAnimation refreshingAnimation;

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
     * 释放刷新的距离
     */
    private int refreshDist = 0;

    /**
     * 刷新过程中滑动操作
     */
    private boolean isRefreshing = false;

    /**
     * 刷新加载回调接口
     */
    public interface OnRefreshListener
    {
        /**
         * 刷新操作
         */
        void onRefresh(RefreshView refreshView);

        /**
         * 加载操作
         */
        void onLoadMore(RefreshView refreshView);
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
    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

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
            refreshDist = ((RelativeLayout) refreshView).getChildAt(0).getMeasuredHeight();
            recyclerView = (RecyclerView) getChildAt(1);
            recyclerView.setOnTouchListener(this);
            layoutOnce = true;
        }
        refreshView.layout(0, distance - loadingViewHeight, loadingViewWidth, distance);
        recyclerView.layout(0, distance, recyclerView.getMeasuredWidth(), distance + getHeight());
    }

    /**
     * 初始化
     */
    private void initialize(Context context){
        refreshView = LayoutInflater.from(context).inflate(R.layout.refresh_layout, null);
        pullIcon = (ImageView) refreshView.findViewById(R.id.pull_icon);
        loadingIcon = (ImageView) refreshView.findViewById(R.id.pull_loading);
        statusStr = (TextView) refreshView.findViewById(R.id.refresh_status);
        successIcon = (ImageView) refreshView.findViewById(R.id.state_iv);
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.rotating);
        setOrientation(VERTICAL);
        addView(refreshView, 0);
    }

    /**
     * 触摸事件
     * @param v 控件
     * @param event 事件
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                initialY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                distance = (int) (event.getY() - initialY);
                //recyclerView到达顶部并向下滑动
                if(!recyclerView.canScrollVertically(-1) && distance>0){
                    //执行下拉操作
                    if(isRefreshing){
                        distance = refreshDist + distance/2;
                        changeMoveLayout(distance);
                    }else{
                        changeMoveLayout(distance/2);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(distance > refreshDist){
                    isRefreshing = true;
                    changeUpLayout(refreshDist);
                }else if(distance <= refreshDist){
                    changeUpLayout(0);
                }

                break;
        }
        return false;
    }

    /**
     * 下拉时更新布局
     * @param change 下拉变化量
     */
    private void changeMoveLayout(int change){
        if(change > refreshDist){
            changeStatus(REFRESH_TO_LOOSEN);
        }else if(change <= refreshDist){
            changeStatus(REFRESH_TO_PULLDOWN);
        }else if(change == 0){
            changeStatus(REFRESH_TO_NORMAL);
        }
        distance = change;
        requestLayout();
    }

    /**
     * 手指移开时更新布局
     * @param change 下拉变化量
     */
    private void changeUpLayout(int change){
        if(change == refreshDist){
            changeStatus(REFRESH_TO_LOADING);
        }else if(change == 0){
            changeStatus(REFRESH_TO_NORMAL);
        }
        distance = change;
        requestLayout();
    }

    /**
     * 下拉刷新完成
     */
    private void refreshFinsih(){

    }

    /**
     * 上拉加载更多完成
     */
    private void loadmoreFinish(){

    }

    /**
     * 更新下拉状态
     * @param status
     */
    private void changeStatus(int status){

        switch (status){
            case REFRESH_TO_NORMAL:
                break;
            case REFRESH_TO_PULLDOWN:
                statusStr.setText("下拉刷新");
                break;
            case REFRESH_TO_LOOSEN:
                statusStr.setText("松开刷新");
                break;
            case REFRESH_TO_LOADING:
                statusStr.setText("正在刷新...");
//                mRefreshListener.onRefresh(this);
                break;
            case REFRESH_TO_SUCCESS:
                statusStr.setText("刷新成功");
                break;
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH_TO_SUCCESS:
                    changeStatus(REFRESH_TO_NORMAL);
                    break;
            }
        }
    };

    /**
     * 刷新完成
     */
    public void refreshFinish(){
        changeStatus(REFRESH_TO_SUCCESS);
    }
}
