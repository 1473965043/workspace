package com.hq.fiveonejrq.jrq.common.bean;

/**
 * Created by guodong on 2017/6/5.
 * 文件下载类
 */

public class FileDownBean {
    /**
     * 是否下载完成
     */
    private boolean isDownFinished = false;

    /**
     * 文件下载远程地址
     */
    private String path;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 文件保存路径
     */
    private String targetPath;

    public FileDownBean(String path, String targetPath) {
        this.path = path;
        this.targetPath = targetPath;
    }
}
