package com.tangerine.UI;

import android.util.Log;
import android.widget.Toast;

import com.tangerine.UI.launcher.ILauncherListener;
import com.tangerine.UI.launcher.LauncherFragment;
import com.tangerine.UI.launcher.OnLauncherTag;
import com.tangerine.UI.main.MainFragment;
import com.tangerine.location.activity.BaseActivity;
import com.tangerine.location.fragment.ShowFragment;

public class ProxyActivity extends BaseActivity implements ILauncherListener {
    private static final String TAG = "ProxyActivity";
    @Override
    public ShowFragment setRootFragment() {
        return new LauncherFragment();
    }

    @Override
    public void onLauncherFinish(OnLauncherTag tag) {
        switch (tag) {
            case SIGNED:
                Log.e(TAG, "onLauncherFinish: 你已经登录了" );
                startWithPop(new MainFragment());
                break;
            case NOT_SIGNED:
                Log.e(TAG, "onLauncherFinish: 你还没有登录" );
                startWithPop(new MainFragment());

                break;
            default:
                break;
        }
    }
}
