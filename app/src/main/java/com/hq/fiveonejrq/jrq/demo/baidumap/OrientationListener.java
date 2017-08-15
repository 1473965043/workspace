package com.hq.fiveonejrq.jrq.demo.baidumap;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by guodong on 2017/8/2.
 * 方向传感器封装类
 */

public class OrientationListener implements SensorEventListener {

    private Context context;
    private SensorManager sensorManager;
    private Sensor sensor;
    private float lastX ;

    private OnOrientationListener mOnOrientationListener;

    public interface OnOrientationListener {
        void onOrientationChanged(float x);
    }

    public void setOnOrientationListener(OnOrientationListener onOrientationListener){
        this.mOnOrientationListener = onOrientationListener;
    }

    public OrientationListener(Context context) {
        this.context = context;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 接受方向感应器的类型
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            // 这里我们可以得到数据，然后根据需要来处理
            float x = event.values[SensorManager.DATA_X];
            if( Math.abs(x- lastX) > 1.0 && mOnOrientationListener != null) {
                mOnOrientationListener.onOrientationChanged(x);
            }
            lastX = x ;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 开始检测
     */
    public void start(){
        // 获得传感器管理器
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            // 获得方向传感器
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
        // 注册传感器
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    /**
     * 停止检测
     */
    public void stop(){
        sensorManager.unregisterListener(this);
    }
}
