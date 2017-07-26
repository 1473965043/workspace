package com.hq.fiveonejrq.jrq.common.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

/**
 * @author AigeStudio
 * @since 2016-05-05
 */
public class DaemonService extends Service {
    private static boolean sPower = true, isRunning;

    @Override
    public void onCreate() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            Notification.Builder builder = new Notification.Builder(this);
//            builder.setSmallIcon(R.mipmap.ic_launcher);
//            startForeground(250, builder.build());
//            startService(new Intent(this, CancelService.class));
//        } else {
//            startForeground(250, new Notification());
//        }
    }

    //        startService(new Intent(this, DaemonService.class));
//        PendingIntent intent = PendingIntent.getService(this, 0x123,
//                new Intent(this, DaemonService.class), PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, AlarmManager.INTERVAL_HALF_HOUR,
//                AlarmManager.INTERVAL_HALF_HOUR, intent);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            isRunning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (sPower) {
                        if (System.currentTimeMillis() >= 123456789000000L) {
                            sPower = false;
                        }
                        Log.d("AigeStudio", "DaemonService");
//                        Daemon.start(DaemonService.this);
                        startService(new Intent(DaemonService.this, ProtectService.class));
                        SystemClock.sleep(3000);
                    }
                }
            }).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}