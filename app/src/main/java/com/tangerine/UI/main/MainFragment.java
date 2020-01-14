package com.tangerine.UI.main;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tangerine.UI.dbControl.DaoManager;
import com.tangerine.UI.eventBean.MapInfoEvent;
import com.tangerine.UI.infoBean.MapInfo;
import com.tangerine.location.R;
import com.tangerine.location.fragment.ShowFragment;
import com.tangerine.location.util.ConvertUtil.PositionConvertUtil;
import com.wang.avi.AVLoadingIndicatorView;

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
    @BindView(R.id.avi)
    AVLoadingIndicatorView Avi = null;
    private LocationManager mLocationManager;
    private double longitude;
    private double latitude;
    private boolean startLocation;
    private static ChangeLocationTask task;
    private MapInfo mapInfo;

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
        mapInfo = DaoManager.queryMapInfo();
        if (mapInfo != null){
            double[] coordinate  =  PositionConvertUtil.bd09_To_gps84(mapInfo.x,mapInfo.y);
            latitude = coordinate[0];
            longitude =  coordinate[1];
            setData(mapInfo);
        }

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
                if (startLocation){
                    task.stop();
                    startLocation = false;
                    btnStart.setText("启动模拟");
                }
                Intent intent = new Intent(getBaseActivity(), MapActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_start:
                //启动
                if (!startLocation){
                    Avi.show();
                    new Handler().postDelayed(() -> {
                        if (initLocationManager()){
                            task = new ChangeLocationTask();
                            Thread thread = new Thread(task);
                            thread.start();
                            startLocation = true;
                            btnStart.setText("停止模拟");
                        }
                        Avi.hide();
                    },2000);
                }else{
                    task.stop();
                    startLocation = false;
                    btnStart.setText("启动模拟");
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInfoChange(MapInfoEvent event) {
        if (event != null) {
            MapInfo mapInfo = event.mapInfo;
           double[] coordinate  =  PositionConvertUtil.bd09_To_gps84(mapInfo.x,mapInfo.y);
            latitude = coordinate[0];
            longitude =  coordinate[1];
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
    private boolean initLocationManager() {
        mLocationManager = (LocationManager) _mActivity.getSystemService(Context.LOCATION_SERVICE);
        assert mLocationManager != null;
        try{
            mLocationManager.addTestProvider(mLocationProvider, true, true, true, true, true, true, true, 0, 5);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            mLocationManager.setTestProviderEnabled(mLocationProvider, true);
        }catch (Exception e){
            startDevelopmentActivity();
            return false;
        }
        mLocationManager.requestLocationUpdates(mLocationProvider, 0, 0, this);
        return true;
    }
    private void startDevelopmentActivity() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            try {
                ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.DevelopmentSettings");
                Intent intent = new Intent();
                intent.setComponent(componentName);
                intent.setAction("android.intent.action.View");
                startActivity(intent);
            } catch (Exception e1) {
                try {
                    Intent intent = new Intent("com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS");//部分小米手机采用这种方式跳转
                    startActivity(intent);
                } catch (Exception e2) {

                }

            }
        }
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
