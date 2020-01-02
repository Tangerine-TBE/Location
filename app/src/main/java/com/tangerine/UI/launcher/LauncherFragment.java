package com.tangerine.UI.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
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
    private static final String TAG = "LauncherFragment";
    private int  account = 5;
    private Timer mTimer;
    private ILauncherListener launcherListener;
    @BindView(R2.id.iv_launcher)
    AppCompatImageView imageView = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener){
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

        initTimer();
    }

    @Override
    public void onTimer() {
        account --;
        if (account < 0){
            mTimer.cancel();
            mTimer = null;
            checkIsFirstRun();
        }
    }
    private void initTimer(){
        mTimer = new Timer();
        final DelayUtil delayUtil =new DelayUtil(this);
        mTimer.schedule(delayUtil,0,1000);
    }
    private void checkIsFirstRun(){
        if (!LocationShareUtil.getAppFlag(LauncherViewTag.HAS_FIRST_RUN.name())){
            Log.e(TAG,"第一次进入APP");
            LocationShareUtil.setAppFlag(LauncherViewTag.HAS_FIRST_RUN.name(), true);

        }else{
            AccountManager.checkAccount(new IUserChecker() {
                @Override
                public void onSignIn() {
                    if (launcherListener != null){
                        launcherListener.onLauncherFinish(OnLauncherTag.SIGNED);
                    }
                }

                @Override
                public void onNotSignIn() {
                    if (launcherListener != null){
                        launcherListener.onLauncherFinish(OnLauncherTag.NOT_SIGNED);
                    }
                }
            });
        }
    }
}
