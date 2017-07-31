package com.hq.fiveonejrq.jrq;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.hq.fiveonejrq.jrq.common.Utils.PopupWindowClient;
import com.hq.fiveonejrq.jrq.databinding.ActivityBaiduMapBinding;
import com.hq.fiveonejrq.jrq.databinding.PopClientLayoutBinding;

import java.util.ArrayList;

public class BaiduMapActivity extends Activity implements SensorEventListener {

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    MapView mMapView;
    BaiduMap mBaiduMap;

    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;

    ActivityBaiduMapBinding mMapBinding;

    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapBinding = DataBindingUtil.setContentView(this, R.layout.activity_baidu_map);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mMapView = mMapBinding.bmapView;
        mMapBinding.setActivity(this);//绑定数据
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public void initOverlay() {
        // add marker overlay
        LatLng llA = new LatLng(39.963175, 116.400244);
        LatLng llB = new LatLng(39.942821, 116.369199);
        LatLng llC = new LatLng(39.939723, 116.425541);
        LatLng llD = new LatLng(39.906965, 116.401394);

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.bike_location);

        MarkerOptions ooA = new MarkerOptions().position(llA).icon(bitmap)
                .zIndex(9).draggable(true);

        Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(bitmap);

        // add ground overlay
        LatLng southwest = new LatLng(39.92235, 116.380338);
        LatLng northeast = new LatLng(39.947246, 116.414977);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                .include(southwest).build();

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bitmap).transparency(0.8f);
        mBaiduMap.addOverlay(ooGround);

        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(u);

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(
                        getApplicationContext(),
                        "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                                + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
    }

    public void addMaker(){
//        mBaiduMap.setMapStatus(msu);
        //定义Maker坐标点
        LatLng point = new LatLng(121.494718, 50.301549);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.arrow);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    /** 关闭页面 */
    public void closeActivity(View view){
        finish();
    }

    /** 设置 */
    public void setting(View view){
        PopClientLayoutBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.pop_client_layout, null, false);
        new PopupWindowClient.Builder(this)
                .setContentView(binding.llSetting)
                .setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")))
                .setFocusable(true)
                .setDimension(200, ViewGroup.LayoutParams.WRAP_CONTENT)
                .showAsDropDown(mMapBinding.imageSetting, -136, 32)
                .show();
        binding.normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                        mCurrentMode, true, null));
                MapStatus.Builder builder1 = new MapStatus.Builder();
                builder1.overlook(0);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
            }
        });
        binding.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                        mCurrentMode, true, null));
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.overlook(0);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        });
        binding.compass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                        mCurrentMode, true, null));
            }
        });
    }

    /** 获取标题 */
    public String getStr(){
        return "百度地图";
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * 定位SDK监听函数, 需实现BDLocationListener里的方法
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            Log.e("地理位置", "经度：" + location.getLongitude() + "纬度："+location.getLatitude());
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        @Override
        public void onConnectHotSpotMessage(String var1, int var2){}
    }
}
