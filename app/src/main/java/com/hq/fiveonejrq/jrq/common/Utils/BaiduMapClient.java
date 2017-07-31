package com.hq.fiveonejrq.jrq.common.Utils;

import android.content.Context;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.hq.fiveonejrq.jrq.R;

import java.util.ArrayList;

/**
 * Created by guodong on 2017/7/31.
 * 百度地图封装类
 */

public class BaiduMapClient {

    //定位类
    private LocationClient mLocClient;
    //地图控件
    private MapView mMapView;
    //传感器管理服务
    private SensorManager mSensorManager;
    //
    private LocationClientOption option;
    //实例
    private static BaiduMapClient mInstance;



    private BaiduMapClient(){}

    public static BaiduMapClient create(){
        return new BaiduMapClient();
    }

    public MapView getMapView(){
        return this.mMapView;
    }

    //初始化操作
    public void init(Context context, MapView mapView){
        mLocClient = new LocationClient(context.getApplicationContext());
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.mMapView = mapView;
        this.option = new LocationClientOption();
    }

    /**
     * 开启定位
     */
    public void openLocation() {
        // 开启定位图层
        mMapView.getMap().setMyLocationEnabled(true);
        // 定位初始化
        option.setOpenGps(true); // 打开gps
        option.setCoorType(CoorType.TYPE1); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 添加覆盖物 -- marker overlay
     */
    public void addOverlay(){
        // add marker overlay
        LatLng llA = new LatLng(39.963175, 116.400244);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.bike_location);
        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bitmap).zIndex(9).draggable(true);
        mMapView.getMap().addOverlay(ooA);
        ArrayList<BitmapDescriptor> giflist = new ArrayList<>();
        giflist.add(bitmap);
        // add ground overlay
        LatLng southwest = new LatLng(39.92235, 116.380338);
        LatLng northeast = new LatLng(39.947246, 116.414977);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast).include(southwest).build();

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bitmap).transparency(0.8f);
        mMapView.getMap().addOverlay(ooGround);

        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());
        mMapView.getMap().setMapStatus(u);

        mMapView.getMap().setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {

            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    /**
     * 坐标类型
     */
    public static class CoorType{
        public static String TYPE1 = "bd09ll";
        public static String TYPE2 = "bd09ll";
        public static String TYPE3 = "bd09ll";
    }
}
