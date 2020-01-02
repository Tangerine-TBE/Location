package com.tangerine.UI.main;

import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.map.MapView;
import com.tangerine.location.R;
import com.tangerine.location.fragment.ShowFragment;

import butterknife.BindView;

public class MapFragment extends ShowFragment {
    @BindView(R.id.mapId)
    MapView mapView = null;

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_map_layout;
    }

    @Override
    public void onBindView(Bundle saveInstanceState, View rootView) {

    }
}
