package com.application;

import android.content.Context;

import java.util.HashMap;

public class AppStart {
    public static Configurator init(Context context){
        getConfigurations().put(ConfiguratorType.APPLICATION_CONTEXT.name(),context.getApplicationContext());
        return Configurator.getInstance();
    }
    public static HashMap<String,Object> getConfigurations(){
        return Configurator.getInstance().getLoConfigs();
    }
    public static Context getApplication(){
        return (Context) getConfigurations().get(ConfiguratorType.APPLICATION_CONTEXT.name());
    }
    public static Configurator getConfigurator(){
        return Configurator.getInstance();
    }
}
