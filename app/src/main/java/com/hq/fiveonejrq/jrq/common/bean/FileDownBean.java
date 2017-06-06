package com.hq.fiveonejrq.jrq.common.bean;

/**
 * Created by guodong on 2017/6/5.
 * 文件类
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

    public boolean isDownFinished() {
        return isDownFinished;
    }

    public void setDownFinished(boolean downFinished) {
        isDownFinished = downFinished;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
}
