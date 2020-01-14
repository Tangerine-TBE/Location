package com.application;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;
import com.tangerine.UI.dbBean.MyObjectBox;
import com.tangerine.UI.dbControl.DaoManager;
import com.tangerine.location.BuildConfig;

import java.util.ArrayList;
import java.util.HashMap;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public class Configurator {
    private static final HashMap<String, Object> LO_CONFIGS = new HashMap<>();
    private static final ArrayList<IconFontDescriptor> DESCRIPTORS = new ArrayList<>();

    private Configurator() {
        LO_CONFIGS.put(ConfiguratorType.CONFIGURE_READY.name(), false);
    }

    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }

    public static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    public final void Configure() {
        intIcons();
        DaoManager.getInstance().init();
        LO_CONFIGS.put(ConfiguratorType.CONFIGURE_READY.name(), true);
    }
    public final Configurator withApiHost (String host){
        LO_CONFIGS.put(ConfiguratorType.API_HOST.name(),host);
        return this;
    }
    final HashMap<String,Object> getLoConfigs(){
        return LO_CONFIGS;
    }
    public final Configurator withActivity(Activity activity){
        LO_CONFIGS.put(ConfiguratorType.ACTIVITY.name(),activity);
        return this;
    }
    private void checkConfiguration(){
        final boolean isReady = (boolean) LO_CONFIGS.get(ConfiguratorType.CONFIGURE_READY.name());
        if (!isReady){
            throw new RuntimeException("Configuration is not ready,please check the configurator");
        }
    }
    public final Configurator withBaiDuMap(Context context){
        SDKInitializer.initialize(context);
        SDKInitializer.setCoordType(CoordType.BD09LL);
        return this;
    }
    public final Configurator withObjectBox(Context context){
        BoxStore boxStore = MyObjectBox.builder().androidContext(context).build();
        if (BuildConfig.DEBUG){
            boolean started = new AndroidObjectBrowser(boxStore).start(context);
            Log.e("ObjectBrowser", "Started: " + started);
        }
        LO_CONFIGS.put(ConfiguratorType.BOXSTORE.name(),boxStore);
        return this;
    }
    @SuppressWarnings("unchecked")
    final <T>T getConfiguration(Enum<ConfiguratorType> key){
        checkConfiguration();
        return (T)LO_CONFIGS.get(key.name());
    }
    public final Configurator withIcon(IconFontDescriptor descriptor){
        DESCRIPTORS.add(descriptor);

        return this;
    }
    private void intIcons(){
        if (DESCRIPTORS.size() > 0){
            final Iconify.IconifyInitializer initializer = Iconify.with(DESCRIPTORS.get(0));
            for (int i  = 1 ; i < DESCRIPTORS.size(); i ++){
                initializer.with(DESCRIPTORS.get(i));
            }
        }
    }
}
