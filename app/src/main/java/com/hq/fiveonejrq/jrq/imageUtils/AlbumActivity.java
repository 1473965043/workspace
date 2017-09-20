package com.hq.fiveonejrq.jrq.imageUtils;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hq.fiveonejrq.jrq.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

public class AlbumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
    }

    /**
     * 扫描相册
     */
    public void scanAlbum(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = AlbumActivity.this.getContentResolver();
                Cursor cursor = cr.query(imgUri
                        , null
                        , MediaStore.Images.Media.MIME_TYPE + "= ? or "
                                + MediaStore.Images.Media.MIME_TYPE + " = ?"
                        , new String[]{"image/jpeg", "image/png"}
                        , MediaStore.Images.Media.DATE_MODIFIED);
                Set<String> mDirPaths = new HashSet<>();
                while(cursor.moveToNext()){
                    String imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(imgPath).getParentFile();
                    if(parentFile == null){
                        continue;
                    }
                    String dirPath = parentFile.getAbsolutePath();
                    FolderInfo info = new FolderInfo();
                    //防止多次扫描(多张图片可能存在同一个文件夹里)
                    if(mDirPaths.contains(dirPath)){
                        continue;
                    }else{
                        mDirPaths.add(dirPath);
                        info.setDirPath(dirPath);
                        info.setFirstImgPath(imgPath);
                    }

                    if(parentFile.list() == null){
                        continue;
                    }
                    //获取图片数量
                    int picCount = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String fileName) {
                            if(fileName.endsWith(".jpg") || fileName.endsWith("jpeg") || fileName.endsWith("png")){
                                return true;
                            }
                            return false;
                        }
                    }).length;
                    info.setImgCount(picCount);
                    cursor.close();
                    //扫描完成，通知mHandler
                    mHandler.sendEmptyMessage(1);
                }
            }
        };
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public class FolderInfo{
        private String dirPath;
        private String firstImgPath;
        private String dirName;
        private int imgCount;

        public String getDirPath() {
            return dirPath;
        }

        public void setDirPath(String dirPath) {
            this.dirPath = dirPath;
            int lastIndexOf = this.dirPath.indexOf("/");
            this.dirName = this.dirPath.substring(lastIndexOf);
        }

        public String getFirstImgPath() {
            return firstImgPath;
        }

        public void setFirstImgPath(String firstImgPath) {
            this.firstImgPath = firstImgPath;
        }

        public String getDirName() {
            return dirName;
        }

        public void setDirName(String dirName) {
            this.dirName = dirName;
        }

        public int getImgCount() {
            return imgCount;
        }

        public void setImgCount(int imgCount) {
            this.imgCount = imgCount;
        }
    }
}
