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

        int width = 0;
        int height = 0;

//        int childWidth = 0;
//        if(widthMode == MeasureSpec.EXACTLY){
//            childWidth = sizeWidth/rowCount;
//        }else{
//            new Exception("dimension assignment error");
//        }
//http://blog.csdn.net/lmj623565791/article/details/38352503
        int lineWidth = 0;//记录某行的宽度
        int lineHeight = 0;//记录某行的高度

        int childrenCount = getChildCount();
        for (int i = 0; i < childrenCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int cWidth = child.getMeasuredWidth();
            int cHeight = child.getMeasuredHeight();

            if(lineWidth + cWidth <= sizeWidth){
                lineWidth = lineWidth + cWidth;
                lineHeight = Math.max(lineHeight, cHeight);
            } else if(lineWidth + cWidth > sizeWidth){
                width = Math.max(lineWidth, cWidth);// 取最大的
                height += lineHeight;//高度累加
                lineWidth = cWidth;//将新的一行的宽度赋值给lineWidth
                lineHeight = cHeight;//将新的一行的高度赋值给lineHeight
            }

            if(i == childrenCount - 1){//最后一个子child
                width = Math.max(lineWidth, cWidth);// 取最大的
                height += lineHeight;
            }
        }
    }

    int rowCount = 3;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public class SizeException extends Exception{

    }
}
