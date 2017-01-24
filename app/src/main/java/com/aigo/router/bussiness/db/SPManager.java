package com.aigo.router.bussiness.db;

import android.content.Context;

/**
 * Created by zhangcirui on 15/8/24.
 */
public class SPManager {

    private static final String TAG = SPManager.class.getSimpleName();
    public static final String IS_GUIDE = "IS_GUIDE";
    public static final String SPLASH_GUIDE = "SPLASH_GUIDE";

    private static SPManager spMasterManager;

    private static Context mContext;

    public void init(Context context) {
        mContext = context;
    }

    public static SPManager getInstance() {
        if (spMasterManager == null) {
            spMasterManager = new SPManager();
        }
        return spMasterManager;
    }

    public String getIsGuide() {

        return SPreferences.getString(mContext, IS_GUIDE, null);
    }

    public boolean setIsGuide(String info) {

        return SPreferences.putStr(mContext, IS_GUIDE, info);
    }

    public String getSplashGuide() {

        return SPreferences.getString(mContext, SPLASH_GUIDE, null);
    }

    public boolean setSplashGuide(String info) {

        return SPreferences.putStr(mContext, SPLASH_GUIDE, info);
    }





}
