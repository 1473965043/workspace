package com.hq.fiveonejrq.jrq.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;

import com.hq.fiveonejrq.jrq.MainActivity;

/**
 * Created by guodong on 2017/3/13.
 */

public class Util {

    /**
     * fragment.getActivity()转为MainActivity
     * @param activity
     * @return
     */
    public static MainActivity getActivity(Activity activity){
        return (MainActivity) activity;
    }

    /**
     * 获取屏幕尺寸
     */
    public static int[] getScreenSize(Activity activity) {
        int[] screenSize = new int[2];
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenSize[0] = size.x;
        screenSize[1] = size.y;
        return screenSize;
    }

    /**
     * 获取状态栏高度
     */
    private int getStatusBarHeight(Context context) {
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return -1;
    }
}
