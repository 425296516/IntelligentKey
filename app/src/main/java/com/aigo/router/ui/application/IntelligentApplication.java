package com.aigo.router.ui.application;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.db.SPManager;
import com.aigo.usermodule.business.UserModule;

/**
 * Created by zhangcirui on 2017/1/16.
 */

public class IntelligentApplication extends MultiDexApplication {

    private static final String TAG = IntelligentApplication.class.getSimpleName();
    public static IntelligentApplication mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");

        UserModule.getInstance().init(this);

        UserModule.getInstance().initKeys(
                "1104853635",
                "JvEktowaxrCGoxcT",
                "3903912865",
                "98ad1a1a3c78610cefa5ffa4c61b1bce",
                "https://api.weibo.com/oauth2/default.html");

        UserModule.getInstance().setIsShowNoInternet(true);
        UserModule.getInstance().userSettings().setRegisterFrom("KT03");

        SceneModule.init(this);

        SPManager.getInstance().init(this);

        mAppContext = this;


    }

    public static IntelligentApplication getInstance() {
        return mAppContext;
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}