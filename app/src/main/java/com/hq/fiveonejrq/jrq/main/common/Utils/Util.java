package com.hq.fiveonejrq.jrq.common.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Environment;
import android.support.compat.BuildConfig;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.hq.fiveonejrq.jrq.main.MyApplication;

import java.io.File;
import java.lang.reflect.Method;

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
     * @return -1代表获取失败
     */
    public static int getStatusBarHeight(Context context) {
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值px
            return context.getResources().getDimensionPixelSize(resourceId);
//            //根据资源ID获取响应的尺寸值dp
//            return pxTodp(context, context.getResources().getDimensionPixelSize(resourceId));
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

    /**
     * @param context
     * @return 获取屏幕原始尺寸高度，包括虚拟功能键高度
     */
    public static int getTotalHeight(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * @param context
     * @return 获取屏幕内容高度不包括虚拟按键
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取系统build.gradle文件中的versionName
     * @return 版本name
     */
    public static String getAppVersionName(){
        String versionName = BuildConfig.VERSION_NAME;
        return versionName;
    }

    /**
     * 获取系统build.gradle文件中的versionCode
     * @return 版本code
     */
    public static int getAppVersionCode(){
        int versionCode = BuildConfig.VERSION_CODE;
        return versionCode;
    }

    /**
     * 获取系统androidManifest文件中的versionName
     * @return 当前应用的版本name 返回""代表获取失败
     */
    public static String getVersionName() {
        Context context = MyApplication.getInstance();
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;
            return versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取系统androidManifest文件中的versionCode
     * @return 当前应用的版本code 返回-1 代表获取失败
     */
    public static int getVersionCode() {
        Context context = MyApplication.getInstance();
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
