package com.hq.fiveonejrq.jrq;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.hq.fiveonejrq.jrq.common.Utils.BaiduMapClient;
import com.hq.fiveonejrq.jrq.common.Utils.PopupWindowClient;
import com.hq.fiveonejrq.jrq.common.bean.MarkerInfo;
import com.hq.fiveonejrq.jrq.common.custom.OrientationListener;
import com.hq.fiveonejrq.jrq.databinding.ActivityBaiduMapBinding;
import com.hq.fiveonejrq.jrq.databinding.PopClientLayoutBinding;

public class BaiduMapActivity extends Activity implements OrientationListener.OnOrientationListener{

    private MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.location_arrow);
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private int mDirection;

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
        mMapClient = BaiduMapClient.create(this, mMapView);
        mMapClient.openLocation(myListener);
        mMapClient.setOnOrientationListener(this);
        mMapClient.addInfosOverlay(MarkerInfo.infos, 0, mOnMarkerClickListener);
    }

    public BaiduMap.OnMarkerClickListener mOnMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            mMapClient.initInfoWindow(marker, new InfoWindow.OnInfoWindowClickListener(){

                @Override
                public void onInfoWindowClick() {
                    new PopupWindowClient.Builder(BaiduMapActivity.this)
                            .setContentView(R.layout.pop_infowindow_layout)
                            .setProportion(1.0, 0.25)
                            .setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")))
                            .setFocusable(true)
                            .showAsDropDown(mMapBinding.titlebar, Gravity.TOP, 0)
                            .build().show();
                }
            });
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
                        mCurrentMode, true, mCurrentMarker));
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
                        mCurrentMode, true, mCurrentMarker));
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
                        mCurrentMode, true, mCurrentMarker));
            }
        });
        binding.addMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 点击定位
     * @param view 定位按钮
     */
    public void location(View view){
        mMapClient.refreshLocation(mCurrentLat, mCurrentLon);
    }

    /**
     * 刷新marker
     */
    public void refreshMarker(View view){

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
    public void onOrientationChanged(float x) {
        mDirection = (int) x;
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                //误差范围(蓝色圈)
                .accuracy(0)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(mDirection)
                .latitude(mCurrentLat)
                .longitude(mCurrentLon).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfiguration(config);
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
            // 构造定位数据
            locData = new MyLocationData.Builder()
                    .accuracy(0)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfiguration(config);
            if (isFirstLoc) {
                isFirstLoc = false;
                mMapClient.refreshLocation(mCurrentLat, mCurrentLon);
            }

        }

        @Override
        public void onConnectHotSpotMessage(String var1, int var2){}
    }
}
