package com.hq.fiveonejrq.jrq.common.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.Display;

import java.io.File;

/**
 * Created by guodong on 2017/3/13.
 */

public class Util {

    /**
     * fragment.getActivity()转为MainActivity
     * @param activity
     * @return
     */
    public static <T extends Activity> T getActivity(Activity activity, Class<T> cls){
        Log.i("ActivityName:", cls.getName());
        T t = (T)activity;
        return t;
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
    public static int getStatusBarHeight(Context context) {
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值

            return pxTodp(context, context.getResources().getDimensionPixelSize(resourceId));
        }
        return -1;
    }

    /**
     * 像素转dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int pxTodp(Context context,float pxValue){
        float scale =context.getResources().getDisplayMetrics().density;
        return(int)(pxValue / scale +0.5f);
    }

    /**
     * 判断SD是否插入,返回SD根目录
     */
    public static String getExternalStorageDirectory(){
        String rootpath;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            rootpath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }else{
            rootpath = null;
        }
        return rootpath;
    }

    /**
     * 获取文件保存地址
     * @return
     */
    public static String getFileTargetPath(Context context, String fileName){
        String targetPath;
        if(null != getExternalStorageDirectory()){
            File file = new File(getExternalStorageDirectory() + File.separator + getAppName(context));
            if(!file.exists()){
                file.mkdir();
            }
            targetPath = file.getAbsolutePath() + File.separator + fileName;
        }else{
            targetPath = null;
        }
        return targetPath;
    }

    /**
     * 获取应用程序名
     * @param context
     * @return
     */
    public static String getAppName(Context context){
        PackageManager pm = context.getPackageManager();
        return context.getApplicationInfo().loadLabel(pm).toString();
    }

}
