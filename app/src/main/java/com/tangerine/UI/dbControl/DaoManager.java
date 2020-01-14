package com.tangerine.UI.dbControl;


import com.application.AppStart;
import com.application.ConfiguratorType;
import com.tangerine.UI.infoBean.MapInfo;


import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.QueryBuilder;

public class DaoManager {
    private static DaoManager daoManager;
    public static synchronized DaoManager getInstance(){
        if (daoManager == null){
            daoManager = new DaoManager();
        }
        return daoManager;
    }
    public  BoxStore boxStore;
    public static Box<MapInfo> mapInfoBox;
    public void init(){
        boxStore = (BoxStore) AppStart.getConfigurations().get(ConfiguratorType.BOXSTORE.name());
        assert boxStore != null;
        mapInfoBox =  boxStore.boxFor(MapInfo.class);
    }
    public  static void addMapInfo(MapInfo mapInfo){
        QueryBuilder<MapInfo> builder = mapInfoBox.query();
        builder.build().remove();
        mapInfoBox.put(mapInfo);
    }
    public static MapInfo queryMapInfo(){
        QueryBuilder<MapInfo> builder = mapInfoBox.query();
        return builder.build().findFirst();
    }

}
