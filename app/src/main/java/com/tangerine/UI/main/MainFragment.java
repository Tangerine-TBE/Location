package com.tangerine.UI.main;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.location.LocationManagerCompat;

import com.baidu.location.Address;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tangerine.UI.eventBean.MapInfoEvent;
import com.tangerine.UI.infoBean.MapInfo;
import com.tangerine.location.R;
import com.tangerine.location.fragment.ShowFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import butterknife.BindView;


public class MainFragment extends ShowFragment implements View.OnClickListener {
    private static final String TAG = "MainFragment";
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
    private void initView(){
        btnStart.setOnClickListener(this);
        btnArea.setOnClickListener(this);
        if (TextUtils.isEmpty(tvTarCountry.getText().toString())){
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
        switch (v.getId()){
            case R.id.fb_add_area:
                Intent intent = new Intent(getBaseActivity(),MapActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_start:
                LocationManager locationManager = (LocationManager) getBaseActivity().getSystemService(Context.LOCATION_SERVICE);
                break;
                default:
                    break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventInfoChange(MapInfoEvent event){
        if (event != null){
            Address address = event.address;
            MapInfo mapInfo = event.mapInfo;
            setData(address, mapInfo);
            Log.e(TAG, "onMapInfoChangeEvent: " + address.address  + mapInfo.y + mapInfo.x );
        }
    }
    private void setData(Address address,MapInfo mapInfo){
        String strAddress = address.address;
        String country = address.country;
        String province = address.province;
        tvTarAddress.setText(strAddress.replace(country,"").replace(province,""));
        tvTarCountry.setText(country);
        tvTarProvince.setText(province);
        tvTarCoordinate.setText(mapInfo.x + "，" + mapInfo.y);
        if (tvTarTag.getVisibility() == View.INVISIBLE){
            tvTarCountry.setVisibility(View.VISIBLE);
            tvTarProvince.setVisibility(View.VISIBLE);
            tvTarTag.setVisibility(View.VISIBLE);
            tvTarCoordinate.setVisibility(View.VISIBLE);

        }
    }
}
