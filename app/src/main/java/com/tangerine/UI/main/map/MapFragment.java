package com.tangerine.UI.main.map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.tangerine.location.R;
import com.tangerine.location.fragment.ShowFragment;

import butterknife.BindView;

public class MapFragment extends ShowFragment {
    private static final String TAG = "MapFragment";
    @BindView(R.id.mapId)
    MapView mapView = null;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;

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

    @Override
    public Object setLayout() {
        return R.layout.fragment_map_layout;
    }

    @Override
    public void onBindView(Bundle saveInstanceState, View rootView) {
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getBaseActivity());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setCoorType("bd0911");
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            mapView.renderMap();
            String  startX = String.valueOf(bdLocation.getLatitude());
            String  startY = String.valueOf(bdLocation.getLongitude());
            Log.e(TAG, "onReceiveLocation: " + startX + " "  + startY);
        }
    }
}
