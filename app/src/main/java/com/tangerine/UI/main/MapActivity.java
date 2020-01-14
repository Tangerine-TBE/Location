package com.tangerine.UI.main;

import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.Address;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tangerine.UI.dbControl.DaoManager;
import com.tangerine.UI.eventBean.MapInfoEvent;
import com.tangerine.UI.infoBean.MapInfo;
import com.tangerine.location.R;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;


public class MapActivity extends AppCompatActivity implements View.OnClickListener, BaiduMap.OnMapClickListener, BaiduMap.OnMapLoadedCallback, OnGetGeoCoderResultListener {
    private static final String TAG = "MapActivity";
    private MapView mapView;
    private static final int GET_LOCATION_SUCCESS = 20;
    private BaiduMap mBaiduMap;
    private boolean isFirstLo = true;
    private LocationClient mLocationClient;
    private MapInfo mapInfo;
    private MyHandler myHandler;
    private Marker marker;
    private GeoCoder geoCoder;
    private LatLng ll;
    private BDAbstractLocationListener mBDAbstractLocationListener =new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //获取当前定位信息
            //构造定位数据
            if (isFirstLo) {
                Address address = bdLocation.getAddress();
                mapInfo = new MapInfo(bdLocation.getLatitude(),bdLocation.getLongitude(),address.address,address.country,address.province);
                //构造定位数据
//            MyLocationData data = new MyLocationData.Builder()
//                    .accuracy(bdLocation.getRadius())//定位精度
//                    .latitude(bdLocation.getLatitude())//纬度
//                    .longitude(bdLocation.getLongitude())//经度
//                    .direction(100)//方向 可利用手机方向传感器获取 此处为方便写死
//                    .build();
                ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                marker = (Marker) mBaiduMap.addOverlay(new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.mipmap.mapicon)));
                isFirstLo = false;
                MapStatus.Builder builder = new MapStatus.Builder()
                        .target(ll)//地图缩放中心点
                        .zoom(20f);//缩放倍数 百度地图支持缩放21级 部分特殊图层为20级
                //改变地图状态
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                Message message = new Message();
                message.what = GET_LOCATION_SUCCESS;
                Log.e(TAG, "onReceiveLocation: " + address.address );

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map_layout);
        myHandler = new MyHandler(this);
        initView();
        initLoc();
        mLocationClient.start();
    }
    private void initLoc() {

        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //定位相关参数设置
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //1. gcj02：国测局坐标；
        //2. bd09：百度墨卡托坐标；
        //3. bd09ll：百度经纬度坐标；

        option.setScanSpan(0);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(false);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        //加载设置
        mLocationClient.setLocOption(option);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addInfo:
                DaoManager.addMapInfo(mapInfo);
                EventBus.getDefault().post(new MapInfoEvent(mapInfo));
                finish();
                break;
                default:
                    break;
        }
    }
    private void initView(){
        mapView = findViewById(R.id.mapId);
        FloatingActionButton addInfo = findViewById(R.id.addInfo);
        mBaiduMap =mapView.getMap();
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(mBDAbstractLocationListener);
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMapLoadedCallback(this);
        addInfo.setOnClickListener(this);
        geoCoder =GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

        this.ll = latLng;
        search(latLng);
        Log.e(TAG, "onMapClick: " + latLng.latitude +"........." + latLng.longitude );
        marker.remove();
        marker= (Marker) mBaiduMap.addOverlay(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.mapicon)));
        MapStatus.Builder builder = new MapStatus.Builder()
                .target(latLng)//地图缩放中心点
                .zoom(20f);//缩放倍数 百度地图支持缩放21级 部分特殊图层为20级
        //改变地图状态
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @Override
    public void onMapPoiClick(MapPoi mapPoi) {

    }

    @Override
    public void onMapLoaded() {
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        String address = reverseGeoCodeResult.getAddress();
        ReverseGeoCodeResult.AddressComponent addressDetail = reverseGeoCodeResult.getAddressDetail();
        mapInfo = new MapInfo(ll.latitude,ll.longitude,reverseGeoCodeResult.getAddress(),addressDetail.countryName,addressDetail.province);
        Log.e(TAG, "onGetReverseGeoCodeResult: " + address );
    }
    private void search(LatLng latLng){
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng).pageNum(0).pageSize(100));
    }

    private static class MyHandler extends Handler {
        private WeakReference<MapActivity> weakReference;

        MyHandler(MapActivity mapActivity){
            weakReference = new WeakReference<>(mapActivity);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            MapActivity mapActivity = weakReference.get();
            if (mapActivity == null){
                return;
            }
            if (msg.what == GET_LOCATION_SUCCESS){
                mapActivity.intMakerView();
                mapActivity.ToastInfo();
            }
        }
    }
    private void ToastInfo(){
        Toast.makeText(this, "获取当前定位成功",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();

    }

    @Override
    public void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }
    private void intMakerView(){
    }
}
