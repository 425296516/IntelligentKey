package com.aigo.router.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.aigo.router.R;
import com.aigo.router.bussiness.db.SPManager;
import com.aigo.usermodule.business.UserModule;
import com.aigo.usermodule.business.util.Constant;
import com.aigo.usermodule.ui.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("FINISH_LOGINACTIVITY");
        registerReceiver(mBroadcastReceiver, intentFilter);

        registerReceiver(receiver, new IntentFilter(Constant.BROADCAST_LOGIN));

        //友盟统计用户登录
        if (UserModule.getInstance().isLogin()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            //可以不添加设置，默认是自动关闭
            intent.putExtra(LoginActivity.BUNDLE_AUTO_FINISH, false);
            startActivity(intent);
        }

    }


    private boolean isSuccess = true;

    //接收到登录成功广播
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (isSuccess) {
                isSuccess = false;
                //关闭登录页面

                if (TextUtils.isEmpty(SPManager.getInstance().getSplashGuide())) {
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }

                finish();
            }

        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("FINISH_LOGINACTIVITY")) {
                finish();
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver);
        unregisterReceiver(receiver);
    }
}
