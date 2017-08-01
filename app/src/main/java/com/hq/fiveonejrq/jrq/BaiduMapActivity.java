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
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hq.fiveonejrq.jrq.common.Utils.BaiduMapClient;
import com.hq.fiveonejrq.jrq.common.Utils.PopupWindowClient;
import com.hq.fiveonejrq.jrq.databinding.ActivityBaiduMapBinding;
import com.hq.fiveonejrq.jrq.databinding.PopClientLayoutBinding;

import java.util.ArrayList;
import java.util.List;


public class BaiduMapActivity extends Activity implements SensorEventListener {

    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;

    ActivityBaiduMapBinding mMapBinding;

    BaiduMapClient mMapClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapBinding = DataBindingUtil.setContentView(this, R.layout.activity_baidu_map);
        mMapBinding.setActivity(this);//绑定数据
        mMapView = mMapBinding.bmapView;
        mBaiduMap = mMapView.getMap();
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        List<LatLng> latLngs = new ArrayList<>();
        LatLng llA = new LatLng(32.079254, 118.787623);
        LatLng llB = new LatLng(32.064355, 118.787624);
        LatLng llC = new LatLng(28.7487420000, 115.8748860000);
        LatLng llD = new LatLng(28.7534890000, 115.8767960000);
        LatLng llE = new LatLng(31.301560, 121.494732);
        latLngs.add(llA);
        latLngs.add(llB);
        latLngs.add(llC);
        latLngs.add(llD);
        latLngs.add(llE);
        mMapClient = new BaiduMapClient.Builder(this, mMapView)
                .addOverlay(latLngs, R.mipmap.bike_location, mOnMarkerClickListener)
                .openLocation(myListener)
                .setSensorEventListener(this)
                .build();
    }

    public BaiduMap.OnMarkerClickListener mOnMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Toast.makeText(BaiduMapActivity.this, "瞅你咋地！", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    @Override
    protected void onPause() {
        mMapClient.onPause();
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
                .build().show();
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
        binding.addMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /** 获取标题 */
    public String getStr(){
        return "百度地图";
    }

    @Override
    protected void onResume() {
        mMapClient.onResume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        mMapClient.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mMapClient.onDestroy();
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
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
//            if (isFirstLoc) {
//                isFirstLoc = false;
//                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.current_location);
//                MarkerOptions ooA = new MarkerOptions().position(ll).icon(bitmapDescriptor).zIndex(9).draggable(true);
//                mMapView.getMap().addOverlay(ooA);
//                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll));
//            }
        }

        @Override
        public void onConnectHotSpotMessage(String var1, int var2){}
    }
}
