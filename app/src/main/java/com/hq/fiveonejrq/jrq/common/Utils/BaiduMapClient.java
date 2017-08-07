package com.hq.fiveonejrq.jrq.common.Utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.hq.fiveonejrq.jrq.R;
import com.hq.fiveonejrq.jrq.common.bean.MarkerInfo;
import com.hq.fiveonejrq.jrq.common.custom.OrientationListener;

import java.util.List;

/**
 * Created by guodong on 2017/7/31.
 * 百度地图封装类
 */

public class BaiduMapClient{

    /**
     * 定位的客户端
     */
    private LocationClient mLocClient;
    //地图控件
    private MapView mMapView;
    //传感器管理服务
    private OrientationListener mOrientationListener;
    //定位参数设置
    private LocationClientOption option;

    private Context context;

    private BitmapDescriptor defaultBitmap, mBitmap;

    //覆盖物
    private Marker marker = null;

    public static BaiduMapClient create(Context context, MapView mapView){
        return new BaiduMapClient(context, mapView);
    }

    private BaiduMapClient(Context context, MapView mapView){
        this.context = context;
        this.mMapView = mapView;
        this.defaultBitmap = BitmapDescriptorFactory.fromResource(R.mipmap.bike_location);
        mOrientationListener = new OrientationListener(context);
        changeDefaultBaiduMapView();
    }

    /**
     * 修改默认百度地图的View,隐藏没用到的默认控件
     */
    private void changeDefaultBaiduMapView() {
        mMapView.getMap().setTrafficEnabled(true);
        //遍历百度地图中的所有子View,找到这个扩大和缩放的按钮控件View，然后设置隐藏View即可
        for (int i = 0; i < mMapView.getChildCount(); i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof RelativeLayout) {//缩放比例控件隐藏
                child.setVisibility(View.GONE);
            } else if (child instanceof ImageView) {//logo隐藏
                child.setVisibility(View.GONE);
            } else if (i == 2) {//缩放按钮隐藏
                child.setVisibility(View.GONE);
//                    mMapView.showZoomControls(false);
            }
        }
    }

    /**
     * 开启定位
     */
    public void openLocation(BDLocationListener listener) {
        mLocClient = new LocationClient(this.context.getApplicationContext());
        this.option = new LocationClientOption();
        // 开启定位图层
        mMapView.getMap().setMyLocationEnabled(true);
        // 定位初始化
        option.setOpenGps(true); // 打开gps
        option.setCoorType(CoorType.TYPE_BD_LL); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        if(listener != null){
            mLocClient.registerLocationListener(listener);
        }
    }

    public void refreshLocation(double lat, double lon){
        LatLng ll = new LatLng(lat, lon);
        mMapView.getMap().animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(ll, 18.0f));
    }

    /**
     * 添加覆盖物 -- marker overlay
     */
    public void addOverlay(List<LatLng> lngList, int descriptorId, BaiduMap.OnMarkerClickListener listener){
        // add marker overlay
        mMapView.getMap().clear();
        if(descriptorId == 0){
            mBitmap = defaultBitmap;
        }else{
            mBitmap = BitmapDescriptorFactory.fromResource(descriptorId);
        }
        for (LatLng ll: lngList) {
            MarkerOptions ooA = new MarkerOptions().position(ll).icon(mBitmap).zIndex(9).draggable(true);
            mMapView.getMap().addOverlay(ooA);
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
    }

    /**
     * 添加覆盖物 -- marker overlay
     */
    public void addInfosOverlay(List<MarkerInfo> infos, int descriptorId, BaiduMap.OnMarkerClickListener listener)
    {
        mMapView.getMap().clear();
        LatLng latLng = null;
        if(descriptorId == 0){
            mBitmap = defaultBitmap;
        }else{
            mBitmap = BitmapDescriptorFactory.fromResource(descriptorId);
        }

        if(listener != null){
            mMapView.getMap().setOnMarkerClickListener(listener);
        }

        for (MarkerInfo info : infos)
        {
            // 位置
            latLng = new LatLng(info.getLatitude(), info.getLongitude());
            // 图标
            MarkerOptions overlayOptions = new MarkerOptions().position(latLng).icon(mBitmap).zIndex(9);
            marker = (Marker) (mMapView.getMap().addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
        // 将地图移到到最后一个经纬度位置
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mMapView.getMap().setMapStatus(u);
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

    /**
     * 设置方向监听器
     * @param onOrientationListener
     */
    public void setOnOrientationListener(OrientationListener.OnOrientationListener onOrientationListener){
        this.mOrientationListener.setOnOrientationListener(onOrientationListener);
    }

    public void onDestroy(){
        this.mMapView.onDestroy();
        this.mMapView = null;
        if(mBitmap != null){
            mBitmap.recycle();
        }
        if(defaultBitmap != null){
            defaultBitmap.recycle();
        }
    }

    public void onStop(){
        // 关闭图层定位
        mMapView.getMap().setMyLocationEnabled(false);
        // 搁置时停止定位
        if(mLocClient != null){
            this.mLocClient.stop();
        }
        // 关闭定位图层
        this.mMapView.getMap().setMyLocationEnabled(false);
        //关闭传感器监听
        mOrientationListener.stop();
    }

    public void onResume(){
        this.mMapView.onResume();
        // 开启图层定位
        mMapView.getMap().setMyLocationEnabled(true);
        if(mLocClient != null){
            mLocClient.start();
        }
        //开启传感器监听
        mOrientationListener.start();
    }

    public void onPause(){
        this.mMapView.onPause();
    }

    /**
     * 坐标类型
     */
    public static class CoorType{
        public static String TYPE_GC = "gcj02";//国测局坐标
        public static String TYPE_BD_LL = "bd09ll";//百度经纬度坐标
        public static String TYPE_BD_MKT = "bd09";//百度墨卡托坐标
    }

    public void initInfoWindow(Marker marker, InfoWindow.OnInfoWindowClickListener infoWindowClickListener){
        MarkerInfo info = (MarkerInfo) marker.getExtraInfo().getSerializable("info");
        //生成一个TextView用户在地图中显示InfoWindow
        TextView location = new TextView(context);
        location.setBackgroundResource(R.drawable.location_tips);
        location.setPadding(30, 20, 30, 50);
        location.setText(info.getName());
        //将marker所在的经纬度的信息转化成屏幕上的坐标
        LatLng ll = marker.getPosition();
//        Point p = mMapView.getMap().getProjection().toScreenLocation(ll);
//        LatLng llInfo = mMapView.getMap().getProjection().fromScreenLocation(p);
        //为弹出的InfoWindow添加点击事件
        InfoWindow window = new InfoWindow(BitmapDescriptorFactory.fromView(location), ll, -47, infoWindowClickListener);
        //显示InfoWindow
        mMapView.getMap().showInfoWindow(window);
        //设置详细信息布局为可见
//        mMarkerInfoLy.setVisibility(View.VISIBLE);
//        //根据商家信息为详细信息布局设置信息
//        popupInfo(mMarkerInfoLy, info);
    }
}
