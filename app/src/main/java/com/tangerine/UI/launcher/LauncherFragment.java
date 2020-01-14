package com.tangerine.UI.launcher;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.tangerine.UI.main.MainFragment;
import com.tangerine.location.R;
import com.tangerine.location.R2;
import com.tangerine.location.fragment.ShowFragment;
import com.tangerine.location.util.AccountUtil.AccountManager;
import com.tangerine.location.util.AccountUtil.IUserChecker;
import com.tangerine.location.util.sharePerfenceUtil.LocationShareUtil;
import com.tangerine.location.util.timer.DelayUtil;
import com.tangerine.location.util.timer.ITimerListener;

import java.util.Timer;

import butterknife.BindView;

public class LauncherFragment extends ShowFragment implements ITimerListener {
    private static final int MY_REQUEST_PERMISSION_LOCATION = 1;
    private static final int MY_REQUEST_PERMISSION_WRITE = 2;
    private static final int MY_REQUEST_PERMISSION_LOCATION_FINE = 3;
    private static final int MY_REQUEST_PERMISSION_LOCATION_SERVICE = 4;
    private static final String TAG = "LauncherFragment";
    private int account = 5;
    private Timer mTimer;
    private ILauncherListener launcherListener;
    @BindView(R2.id.iv_launcher)
    AppCompatImageView imageView = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener) {
            launcherListener = (ILauncherListener) activity;
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_laucher_layout;
    }

    @Override
    public void onBindView(Bundle saveInstanceState, View rootView) {
        Glide.with(getBaseActivity()).load(R.mipmap.launcher).into(imageView);
        LocationManager locationManager = (LocationManager) getBaseActivity().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        boolean ok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!ok){
            Toast.makeText(getBaseActivity(),"检测到未开启gps定位服务",Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, MY_REQUEST_PERMISSION_LOCATION_SERVICE);
        }
        requestPermission(getBaseActivity(),Manifest.permission.ACCESS_FINE_LOCATION,MY_REQUEST_PERMISSION_LOCATION);
        requestPermission(getBaseActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE,MY_REQUEST_PERMISSION_WRITE);
        requestPermission(getBaseActivity(),Manifest.permission.ACCESS_COARSE_LOCATION,MY_REQUEST_PERMISSION_LOCATION_FINE);
        initTimer();
    }

    @Override
    public void onTimer() {
        account--;
        if (account < 0) {
            mTimer.cancel();
            mTimer = null;
            checkIsFirstRun();
        }
    }

    private void initTimer() {
        mTimer = new Timer();
        final DelayUtil delayUtil = new DelayUtil(this);
        mTimer.schedule(delayUtil, 0, 1000);
    }

    private void checkIsFirstRun() {
        if (!LocationShareUtil.getAppFlag(LauncherViewTag.HAS_FIRST_RUN.name())) {
            Log.e(TAG, "第一次进入APP");
            LocationShareUtil.setAppFlag(LauncherViewTag.HAS_FIRST_RUN.name(), true);
            startWithPop(new MainFragment());
        } else {
            AccountManager.checkAccount(new IUserChecker() {
                @Override
                public void onSignIn() {
                    if (launcherListener != null) {
                        launcherListener.onLauncherFinish(OnLauncherTag.SIGNED);
                    }
                }

                @Override
                public void onNotSignIn() {
                    if (launcherListener != null) {
                        launcherListener.onLauncherFinish(OnLauncherTag.NOT_SIGNED);
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_PERMISSION_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "onRequestPermissionsResult: MY_REQUEST_PERMISSION_LOCATION is Success");
                } else {
                    Log.e(TAG, "onRequestPermissionsResult: MY_REQUEST_PERMISSION_LOCATION is Error");
                }
                break;
            case MY_REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.e(TAG, "onRequestPermissionsResult: MY_REQUEST_PERMISSION_WRITE is Success");
                }else{
                    Log.e(TAG, "onRequestPermissionsResult: MY_REQUEST_PERMISSION_WRITE is Error");
                }
                break;
            case MY_REQUEST_PERMISSION_LOCATION_FINE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.e(TAG, "onRequestPermissionsResult: MY_RQEUEST_PERMISSION_LOCATION_FINE" );
                }else {
                    Log.e(TAG, "onRequestPermissionsResult: MY_REQUEST_PERMISSION_LOCATION_FINE");
                }
                break;
            default:
                break;
        }
    }
    private void requestPermission(Activity activity,String permission,int requestCode){
        if (ContextCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){
                ActivityCompat.requestPermissions(getBaseActivity(), new String[]{permission}, requestCode);

            }else{
                ActivityCompat.requestPermissions(getBaseActivity(), new String[]{permission}, requestCode);
            }
        }
    }

}
