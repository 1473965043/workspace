package com.hq.fiveonejrq.jrq.common.bean;

/**
 * Created by guodong on 2017/6/5.
 * 文件类
 */

public class FileDownBean {

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
