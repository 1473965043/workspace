package com.hq.fiveonejrq.jrq.common.Utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/7/18.
 * 日志工具
 */

public class LogUtil {
    /** debug模式 */
    private static boolean debug = false;

    public static String NETWORK = "network";

    public static String ACTIVITY = "activity";

    public static void setDebug(boolean isDebug){
        debug = isDebug;
    }

    public static void e(String tag, String msg){
        if(debug){
            return;
        }
        Log.e(tag, msg);
    }

    public static void d(String tag, String msg){
        if(debug){
            return;
        }
        Log.d(tag, msg);
    }

    public static void i(String tag, String msg){
        if(debug){
            return;
        }
        Log.i(tag, msg);
    }

    public static void w(String tag, String msg){
        if(debug){
            return;
        }
        Log.w(tag, msg);
    }
}
