package com.tangerine.UI.main;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tangerine.location.R;
import com.tangerine.location.fragment.ShowFragment;

import butterknife.BindView;


public class MainFragment extends ShowFragment implements View.OnClickListener {
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fb_add_area:
                startWithPop(new MapFragment());
                default:
                    break;
        }
    }
}
