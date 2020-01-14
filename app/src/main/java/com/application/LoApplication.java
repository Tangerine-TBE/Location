package com.application;

import android.app.Application;

import com.tangerine.UI.icon.FrontLocationMoudle;

public class LoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppStart.init(this)
                .withBaiDuMap(this)
                .withIcon(new FrontLocationMoudle())
                .withObjectBox(this)
                .Configure();

    }
}
