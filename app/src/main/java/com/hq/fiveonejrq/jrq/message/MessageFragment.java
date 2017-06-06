package com.hq.fiveonejrq.jrq.message;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
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
import com.hq.fiveonejrq.jrq.common.service.DownService;

import java.io.File;

/**
 * Created by guodong on 2017/2/28.
 */

public class MessageFragment extends Fragment {

    /**
     * service与activity的桥梁
     */
    private DownService.MyBinder mBinder;

    /**
     * 通知栏管理器
     */
    private NotificationManager notificationManager;

    private NotificationCompat.Builder mBuilder;

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
        //绑定服务
        getActivity().bindService(new Intent(getActivity(), DownService.class), mConnection, Context.BIND_AUTO_CREATE);
        Toast.makeText(getActivity(), screenWidth+"与"+screenHeight, Toast.LENGTH_SHORT).show();
        return view;
    }

    /**
     * 下载APK文件
     */
    public void downApk(){
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
                },
                101
        );
        new Thread(){
            @Override
            public void run() {
                super.run();
                String path = "http://down.360safe.com/360ap/360freewifi_beta.apk";//远程地址
                mBinder.setParameters(path, getActivity(), "jfcaifu.apk");
                createProgressNotification();
                mBinder.startDown(mStatusListener);
            }
        }.start();
    }

    /**
     * 创建带进度条的通知栏
     */
    private void createProgressNotification(){
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setOngoing(true);
        mBuilder.setContentTitle("金蜂财富");
        mBuilder.setContentText("版本更新中...");
        mBuilder.setProgress(mBinder.getDownUtils().getDownFileLength(), 0, false);
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

    /**
     * 链接服务
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = (DownService.MyBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    /**
     * 状态监听
     */
    private DownUtils.FileDownStatusListener mStatusListener = new DownUtils.FileDownStatusListener() {
        @Override
        public void onProgress(int progress) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = progress;
            mHandler.sendMessage(msg);
//            Log.e("progress", "progress == " + progress + "\n进度："
//                    + progress * 100 / mBinder.getDownUtils().getDownFileLength()+"%");
        }

        @Override
        public void onCompleted(String targetPath) {
            new AlertDialog.Builder(getActivity()).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            })
            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            })
            .show();
            getActivity().unbindService(mConnection);
            mHandler.sendEmptyMessage(0);
        }

        @Override
        public void onDownloadFailed(String error) {
            Log.e("result", "异常信息：" + error);
        }
    };

    /**
     * 用来完成耗时操作
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 0:
                    mBuilder.setContentText("下载完成");
                    notificationManager.notify(101, mBuilder.build());
                    Toast.makeText(getActivity(), "下载完成！", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    int progress = (int) msg.obj;
                    mBuilder.setProgress(mBinder.getDownUtils().getDownFileLength(), progress, false);
                    notificationManager.notify(101, mBuilder.build());
                    break;
                default:
                    break;
            }
        }
    };
}
