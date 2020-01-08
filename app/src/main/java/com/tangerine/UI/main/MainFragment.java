package com.tangerine.UI.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.core.view.KeyEventDispatcher;

import com.baidu.location.Address;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tangerine.UI.eventBean.MapInfoEvent;
import com.tangerine.UI.infoBean.CoordinateBean;
import com.tangerine.UI.infoBean.MapInfo;
import com.tangerine.location.R;
import com.tangerine.location.fragment.ShowFragment;
import com.tangerine.location.util.ConvertUtil.PositionConvertUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import butterknife.BindView;


public class MainFragment extends ShowFragment implements View.OnClickListener, LocationListener {
    private static final String TAG = "MainFragment";
    private static final String mLocationProvider = LocationManager.GPS_PROVIDER;
    @BindView(R.id.fb_add_area)
    FloatingActionButton btnArea = null;
    @BindView(R.id.btn_start)
    AppCompatButton btnStart = null;
    //target Information
    @BindView(R.id.tv_target_country)
    AppCompatTextView tvTarCountry = null;
    @BindView(R.id.tv_target_address)
    AppCompatTextView tvTarAddress = null;
    @BindView(R.id.tv_target_coordinate)
    AppCompatTextView tvTarCoordinate = null;
    @BindView(R.id.tv_target_province)
    AppCompatTextView tvTarProvince = null;
    @BindView(R.id.tag)
    AppCompatTextView tvTarTag = null;
    private LocationManager mLocationManager;
    private double longitude;
    private double latitude;


    @Override
    public Object setLayout() {
        return R.layout.fragment_main_layout;
    }

    @Override
    public void onBindView(Bundle saveInstanceState, View rootView) {
        initView();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {

    }

    private void initView() {
        btnStart.setOnClickListener(this);
        btnArea.setOnClickListener(this);
        if (TextUtils.isEmpty(tvTarCountry.getText().toString())) {
            tvTarCountry.setVisibility(View.INVISIBLE);
            tvTarProvince.setVisibility(View.INVISIBLE);
            tvTarTag.setVisibility(View.INVISIBLE);
            tvTarCoordinate.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_add_area:
                Intent intent = new Intent(getBaseActivity(), MapActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_start:
                initLocationManager();
                Thread thread = new Thread(new ChangeLocationTask());
                thread.start();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInfoChange(MapInfoEvent event) {
        if (event != null) {
            MapInfo mapInfo = event.mapInfo;
           CoordinateBean coordinateBean =  PositionConvertUtil.bd09tToGcj02(mapInfo.x,mapInfo.y);
            latitude = coordinateBean.getLatitude();
            longitude =  coordinateBean.getLongitude();
            setData( mapInfo);
        }
    }

    private void setData(  MapInfo mapInfo) {
        String strAddress = mapInfo.address;
        String country = mapInfo.country;
        String province = mapInfo.province;
        tvTarAddress.setText(strAddress.replace(country, "").replace(province, ""));
        tvTarCountry.setText(country);
        tvTarProvince.setText(province);
        tvTarCoordinate.setText(mapInfo.x + "，" + mapInfo.y);
        if (tvTarTag.getVisibility() == View.INVISIBLE) {
            tvTarCountry.setVisibility(View.VISIBLE);
            tvTarProvince.setVisibility(View.VISIBLE);
            tvTarTag.setVisibility(View.VISIBLE);
            tvTarCoordinate.setVisibility(View.VISIBLE);

        }
    }

    private boolean changeLocation() {
        LocationManager mLocationManager = (LocationManager) _mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return false;
        }
        try {
            mLocationManager.addTestProvider(LocationManager.NETWORK_PROVIDER, false, false, false, false, true, true, true, 0, 5);
            mLocationManager.setTestProviderEnabled(LocationManager.NETWORK_PROVIDER, true);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void openGpsSettingEvent() {
        Intent callGpsSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        _mActivity.startActivity(callGpsSettingIntent);
    }

    @SuppressLint("MissingPermission")
    private void initLocationManager() {
        mLocationManager = (LocationManager) _mActivity.getSystemService(Context.LOCATION_SERVICE);
        assert mLocationManager != null;
        mLocationManager.addTestProvider(mLocationProvider, true, true, true, true, true, true, true, 0, 5);
        mLocationManager.setTestProviderEnabled(mLocationProvider, true);
        mLocationManager.requestLocationUpdates(mLocationProvider, 0, 0, this);
    }
    private void openTestProviderLocationException(){
        Intent intent = new Intent("//");
        ComponentName cm = new ComponentName("com.android.settings","com.android.setting.DevelopmentSetting");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        _mActivity.startActivity(intent);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Log.e("gps", String.format("location: x=%s y=%s", lat, lng));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setLocation(double longitude, double latitude){
        Location location = new Location(mLocationProvider);
        location.setTime(System.currentTimeMillis());
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        location.setAltitude(2.0f);
        location.setAccuracy(3.0f);
        mLocationManager.setTestProviderLocation(mLocationProvider,location);
    }

    private class ChangeLocationTask implements  Runnable{
        private boolean run = true;
        public void stop(){
            run = false;
        }
        @Override
        public void run() {
            while (run){
                try {
                    Thread.sleep(500);
                }catch (Exception e){
                    return;
                }
                setLocation(longitude, latitude);
            }
        }
    }
}
