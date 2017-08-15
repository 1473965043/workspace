package com.hq.fiveonejrq.jrq.common.Utils;

import com.hq.fiveonejrq.jrq.common.bean.UserInfo;
import com.hq.fiveonejrq.jrq.common.interfaces.BaseResultListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by guodong on 2017/7/18.
 * 封装一些网络请求方法
 */

public class NetworkUtils {

    /**
     * 发送网络请求
     * @param map 封装数据--参数
     * @param resultListener 结果监听器
     * @param url 服务器对应地址
     */
    public static void send(String url,Map<String, String> map, BaseResultListener resultListener){
        if(url.isEmpty() || null == map || map.isEmpty()){
            LogUtil.e(LogUtil.NETWORK, "请求的网址或参数为空，请检查。");
            return;
        }
        getUrl(url, map, resultListener);
    }

    /**
     * 获取拼接后的地址
     * @param url 原始地址
     * @param map 参数
     * @param resultListener 监听器
     */
    private static void getUrl(String url, Map<String, String> map, BaseResultListener resultListener){
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        String[] keys = sortParams(map);
        for (int i = 0; i < keys.length; i++) {
            if(i == 0){
                sb.append("?").append(keys[i]).append("=").append(map.get(keys[i]));
            }else{
                sb.append("&").append(keys[i]).append("=").append(map.get(keys[i]));
            }
        }
    }

    /***
     * 获取map所有的key，并按照一定的规则排序
     * @param map
     * @return
     */
    private static String[] sortParams(Map<String, String> map) {
        Object[] objs = map.keySet().toArray();
        String[] keys = new String[objs.length];
        int i = 0;
        for (Object key : objs) {
            keys[i] = (String) key;
            i++;
        }
        Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
        return keys;
    }

    /** 授权口令name */
    private static String TOKEN = "token";
    /** 签名name */
    private static String SIGN = "sign";
    /** 时间戳name */
    private static String TS = "ts";
    /** userId */
    private static String USERID = "user_id";
    /** 设置超时 */
    public static final int DEFAULT_TIMEOUT = 5;
    /** POST请求 */
    public static final String POST = "post";
    /** GET请求 */
    public static final String GET = "get";

    public static Map<String, String> getHttpBaseParame(){
        Map<String, String> map = new HashMap<>();
        if(UserInfo.getInstance().getLoginStatus()){//判断登录
            map.put(TOKEN, UserInfo.getInstance().getToken());//授权命令
            map.put(USERID, UserInfo.getInstance().getUserId());//用户id
            map.put(TS, System.currentTimeMillis()+"");//时间戳
            map.put(SIGN, UserInfo.getInstance().getSign());//签名
        }
        return map;
    }
}