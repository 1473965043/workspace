package com.hq.fiveonejrq.jrq.message;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.common.Utils.DownUtils;
import com.hq.fiveonejrq.jrq.common.Utils.Util;
import com.hq.fiveonejrq.jrq.common.bean.FileDownBean;

import java.io.File;

/**
 * Created by guodong on 2017/2/28.
 */

public class MessageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_layout, container, false);
        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = getActivity().getWindowManager();
        DisplayMetrics dp = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dp);
        int screenWidth = dp.widthPixels;
        int screenHeight = dp.heightPixels;
        view.findViewById(R.id.feedback_of_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downApk();
            }
        });
        view.findViewById(R.id.invitation_of_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFile();
            }
        });
        view.findViewById(R.id.contacts_of_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMkdir();
            }
        });
        Toast.makeText(getActivity(), screenWidth+"与"+screenHeight, Toast.LENGTH_SHORT).show();
        return view;
    }

    public void downApk(){
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
                },
                101
        );
        Log.e("result", "准备下载");
        new Thread(){
            @Override
            public void run() {
                super.run();
                String path = "http://down.360safe.com/360ap/360freewifi_beta.apk";//远程地址
                String targetPath = Util.getFileTargetPath(getActivity(), "jfcaifu.apk");
                FileDownBean fileDownBean = new FileDownBean(path, targetPath);
                DownUtils downUtils = new DownUtils(fileDownBean);
                downUtils.downFile();
            }
        }.start();
    }

    public void deleteFile(){
        File file = new File(Util.getFileTargetPath(getActivity(), "jfcaifu.apk"));
        if(file.exists()){
            file.delete();
        }else{
            Toast.makeText(getActivity(), "文件已不存在！", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteMkdir(){
        File file = new File(Util.getExternalStorageDirectory() + File.separator + Util.getAppName(getActivity()));
        if(file.exists()){
            file.delete();
        }else{
            Toast.makeText(getActivity(), "文件已不存在！", Toast.LENGTH_SHORT).show();
        }
    }
}
