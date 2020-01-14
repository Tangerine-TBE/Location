package com.tangerine.UI.dbControl;

import com.application.AppStart;
import com.application.ConfiguratorType;
import com.tangerine.UI.dbBean.UserBean;

import java.util.List;

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
    public static Box<UserBean> userBeanBox;
    public void init(){
        boxStore = (BoxStore) AppStart.getConfigurations().get(ConfiguratorType.BOXSTORE.name());
        assert boxStore != null;
        userBeanBox = boxStore.boxFor(UserBean.class);
    }
    public static void addUser(UserBean userBean){
        userBeanBox.put(userBean);
    }
    public static List<UserBean> queryUser(){
        QueryBuilder<UserBean> builder = userBeanBox.query();
      return   builder.build().find();
    }
}
