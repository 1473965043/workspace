package com.hq.fiveonejrq.jrq.common.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guodong on 2017/8/22.
 * File类的常用操作
 */

public class FileUtils {

    /**
     * 要点：File类相关API
     * RandomAccessFile 的用法：既可以读文件，也可以写文件；随机读写，任意位置。
     */

    private static List<File> fileList;

    /**
     * 列出指定目录下（包括其子目录）的所有文件
     */
    public static void listDirectory(File dir){

        if(fileList == null){
            fileList = new ArrayList<>();
        }

        if(!dir.exists()){
            throw new IllegalArgumentException("目录"+dir+"不存在！");
        }

        if(!dir.isDirectory()){
            throw new IllegalArgumentException(dir + "不是目录");
        }

//        String[] fileName = dir.list();//获取当前目录下的文件名，不包括子目录下的文件名
        File[] files = dir.listFiles();//获取当前目录下的文件
        for (File file: files) {
            if(file.isDirectory()){
                listDirectory(file);
            }else{
                fileList.add(file);
                LogUtil.d("fileName", file.getName());
            }
        }
    }

    /**
     * 获取文件列表
     * @return
     */
    public static List<File> getListDirectory(File dir){
        listDirectory(dir);
        List<File> list = new ArrayList<>();
        if(fileList.size() > 0){
            list.addAll(fileList);
        }

        //释放资源
        if(fileList != null){
            fileList.clear();
            fileList = null;
        }
        return list;
    }
}
