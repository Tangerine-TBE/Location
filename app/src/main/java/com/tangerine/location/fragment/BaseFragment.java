package com.tangerine.location.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tangerine.location.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

public abstract class BaseFragment extends SwipeBackFragment {
    private Unbinder mUnbind;

    public abstract Object setLayout();
    public abstract void onBindView(Bundle saveInstanceState, View rootView);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView;
        if (setLayout() instanceof Integer){
            rootView = inflater.inflate((Integer) setLayout(),container,false);
        }else if (setLayout() instanceof View){
            rootView = (View) setLayout();
        }else {
            throw new ClassCastException("setLayout() type must be int or view");
        }
        mUnbind = ButterKnife.bind(this,rootView);
        onBindView(savedInstanceState,rootView);
        return rootView;
    }
    public BaseActivity getBaseActivity(){
        return (BaseActivity) _mActivity;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbind != null){
            mUnbind.unbind();
        }
    }
}
