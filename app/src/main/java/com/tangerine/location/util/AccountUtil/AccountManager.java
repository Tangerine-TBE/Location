package com.tangerine.location.util.AccountUtil;

import com.tangerine.location.util.sharePerfenceUtil.LocationShareUtil;

public class AccountManager {
    private enum  SignTag{
        SIGN_TAG
    }
    public static void setSignState(boolean state){
        LocationShareUtil.setAppFlag(SignTag.SIGN_TAG.name(), state);
    }
    private static boolean isSignIn(){
        return LocationShareUtil.getAppFlag(SignTag.SIGN_TAG.name());
    }
    public static void checkAccount(IUserChecker checker){
        if (isSignIn()){
            checker.onSignIn();
        }else{
            checker.onNotSignIn();
        }
    }
}
