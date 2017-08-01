package com.hq.fiveonejrq.jrq.common.Utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guodong on 2017/7/31.
 * 百度地图封装类
 */

public class BaiduMapClient{

    //定位类
    private LocationClient mLocClient;
    //地图控件
    private MapView mMapView;
    //传感器管理服务
    private SensorManager mSensorManager;
    //定位参数设置
    private LocationClientOption option;
    //marker图标
    private List<BitmapDescriptor> markerBitmapList;
    //方向传感器注册监听器
    private SensorEventListener eventListener;

    private BaiduMapClient(Builder builder){
        this.mMapView = builder.mMapView;
        this.mSensorManager = builder.mSensorManager;
        this.mLocClient = builder.mLocationClient;
        this.markerBitmapList = builder.markerBitmapList;
        this.eventListener = builder.eventListener;
    }

    public MapView getMapView(){
        return this.mMapView;
    }

    public SensorManager getSensorManager() {
        return this.mSensorManager;
    }

    /**
     * @author mikyou
     * 是否打开实时交通
     * */
    public void switchRoadCondition() {
        if (mMapView.getMap().isTrafficEnabled()) {//如果是开着的状态，当点击后，就会出关闭状态
            mMapView.getMap().setTrafficEnabled(false);
        }else{//如果是的关闭的状态，当点击后，就会处于开启的状态
            mMapView.getMap().setTrafficEnabled(true);
        }
    }

    public void onDestroy(){
        // 退出时销毁定位
        if(mLocClient != null){
            this.mLocClient.stop();
        }
        // 关闭定位图层
        this.mMapView.getMap().setMyLocationEnabled(false);
        this.mMapView.onDestroy();
        this.mMapView = null;
        if(markerBitmapList != null && markerBitmapList.size() > 0){
            for (BitmapDescriptor bitmap: markerBitmapList) {
                bitmap.recycle();
            }
        }
    }

    public void onStop(){
        if(mSensorManager != null && eventListener != null){
            this.mSensorManager.unregisterListener(this.eventListener);
        }
    }

    public void onResume(){
        this.mMapView.onResume();
        if(mSensorManager != null && eventListener != null){
            this.mSensorManager.registerListener(this.eventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void onPause(){
        this.mMapView.onPause();
    }

    public static class Builder{

        private LocationClient mLocationClient;
        private SensorManager mSensorManager;
        private MapView mMapView;
        private LocationClientOption option;
        private List<BitmapDescriptor> markerBitmapList;
        private SensorEventListener eventListener;
        private Context context;

        public Builder(Context context, MapView mapView){
            this.context = context;
            this.mMapView = mapView;
            changeDefaultBaiduMapView();
        }

        /**
         * @author zhongqihong
         * 修改默认百度地图的View,隐藏没用到的默认控件
         * */
        private void changeDefaultBaiduMapView() {
            mMapView.getMap().setTrafficEnabled(true);
            //设置隐藏缩放和扩大的百度地图的默认的比例按钮
            for (int i = 0; i < mMapView.getChildCount(); i++) {//遍历百度地图中的所有子View,找到这个扩大和缩放的按钮控件View，然后设置隐藏View即可
                View child = mMapView.getChildAt(i);
                if (child instanceof RelativeLayout) {//缩放比例控件隐藏
                    child.setVisibility(View.GONE);
                }else if(child instanceof ImageView){//logo隐藏
                    child.setVisibility(View.GONE);
                }else if(i == 2){//缩放按钮隐藏
                    child.setVisibility(View.GONE);
//                    mMapView.showZoomControls(false);
                }
            }
        }

        /**
         * 开启定位
         */
        public Builder openLocation(BDLocationListener listener) {
            mLocationClient = new LocationClient(this.context.getApplicationContext());
            mSensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
            this.option = new LocationClientOption();
            // 开启定位图层
            mMapView.getMap().setMyLocationEnabled(true);
            // 定位初始化
            option.setOpenGps(true); // 打开gps
            option.setCoorType(CoorType.TYPE_BD_LL); // 设置坐标类型
            option.setScanSpan(1000);
            mLocationClient.setLocOption(option);
            if(listener != null){
                mLocationClient.registerLocationListener(listener);
            }
            mLocationClient.start();
            return this;
        }

        /**
         * 添加覆盖物 -- marker overlay
         */
        public Builder addOverlay(List<LatLng> lngList, int descriptorId, BaiduMap.OnMarkerClickListener listener){
            this.markerBitmapList = new ArrayList<>();
            // add marker overlay
//            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.bike_location);
            mMapView.getMap().clear();
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(descriptorId);
            for (LatLng ll: lngList) {
                MarkerOptions ooA = new MarkerOptions().position(ll).icon(bitmap).zIndex(9).draggable(true);
                mMapView.getMap().addOverlay(ooA);
                markerBitmapList.add(bitmap);
            }
            // add ground overlay
            //标志物点击事件
            if(listener != null){
                mMapView.getMap().setOnMarkerClickListener(listener);
            }
            if(lngList != null && lngList.size() > 0){
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(lngList.get(0));//通过这个经纬度对象，地图就可以定位到该点
                mMapView.getMap().animateMapStatus(msu);
            }
            return this;
        }

        public Builder setSensorEventListener(SensorEventListener eventListener){
            this.eventListener = eventListener;
            return this;
        }

        public BaiduMapClient build(){
            return new BaiduMapClient(this);
        }

    }

    /**
     * 坐标类型
     */
    public static class CoorType{
        public static String TYPE_GC = "gcj02";//国测局坐标
        public static String TYPE_BD_LL = "bd09ll";//百度经纬度坐标
        public static String TYPE_BD_MKT = "bd09";//百度墨卡托坐标
    }
}
