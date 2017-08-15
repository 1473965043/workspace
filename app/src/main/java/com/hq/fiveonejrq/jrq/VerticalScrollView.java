package com.hq.fiveonejrq.jrq;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/6/22.
 * 自定义Scrollview解决嵌套ViewPager滑动冲突问题
 */

public class VerticalScrollView extends ScrollView {

    public VerticalScrollView(Context context) {
        super(context);
    }

    public VerticalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float mDownPosX = 0;
    private float mDownPosY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();

        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownPosX = x;
                mDownPosY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (deltaX > deltaY) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        View childView = getChildAt(0);
        if (childView != null) {
            childView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = childView.getHeight();
            if (h > height) {
                height = h;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
