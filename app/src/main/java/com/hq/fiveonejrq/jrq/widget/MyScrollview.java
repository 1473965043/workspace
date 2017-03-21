package com.hq.fiveonejrq.jrq.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

import com.hq.fiveonejrq.jrq.base.Pullable;

/**
 * 屏蔽 滑动事件
 */
public class MyScrollview extends ScrollView implements Pullable {
    private int downX;
    private int downY;
    private int mTouchSlop;

    public MyScrollview(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyScrollview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean canPullDown() {
        if (getScrollY() == 0) {//判断是否滑到顶端
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean canPullUp() {
        if(getChildAt(0).getMeasuredHeight() >= getHeight()){//判断子控件的高度是否超过本身高度

            if (getScrollY() >= (getChildAt(0).getMeasuredHeight() - getHeight())){//判断是否滑到底部
                return true;
            } else {
                return false;
            }

        }else{
            return false;
        }
    }
}