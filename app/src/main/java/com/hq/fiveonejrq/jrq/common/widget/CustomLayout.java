package com.hq.fiveonejrq.jrq.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by guodong on 2017/9/19.
 */

public class CustomLayout extends ViewGroup {

    /**
     * 1、一行多少列
     * 2、itemview
     * @param context
     */
    public CustomLayout(Context context) {
        super(context, null);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {

    }

    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 计算出所有的childView的宽和高
//        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0, height = 0;

        int childWidth = 0;
        if(widthMode == MeasureSpec.EXACTLY){
            childWidth = sizeWidth/rowCount;
        }else{
            new Exception("dimension assignment error");
        }
//http://blog.csdn.net/lmj623565791/article/details/38352503
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int columnHeight = 0;
        int childrenCount = getChildCount();
        for (int i = 0; i < childrenCount; i++) {
            View child = getChildAt(i);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();
            width = width + cWidth;
            if(width > sizeWidth){
                width = width - cWidth;
//                columnHeight =
            }
            columnHeight = Math.max(columnHeight, cHeight);
        }
    }

    int rowCount = 3;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public class SizeException extends Exception{

    }
}
