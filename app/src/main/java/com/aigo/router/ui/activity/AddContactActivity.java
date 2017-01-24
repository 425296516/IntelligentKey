package com.aigo.router.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.NetGetVerifyCode;
import com.aigo.router.bussiness.bean.ResultObject;
import com.aigo.router.ui.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddContactActivity extends AppCompatActivity {

    private static final String TAG = AddContactActivity.class.getSimpleName();

    @Bind(R.id.et_add_remark)
    EditText etAddRemark;
    @Bind(R.id.et_input_phone)
    EditText etInputPhone;
    @Bind(R.id.btn_verification_code)
    Button btnVerificationCode;
    @Bind(R.id.et_input_verification_code)
    EditText etInputVerificationCode;
    @Bind(R.id.btn_finish)
    Button btnFinish;
    private NetGetVerifyCode mNetGetVerifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @OnClick(R.id.btn_verification_code)
    public void OnClickVerficationCode() {
        SceneModule.getInstance().getPhoneVerifyCode(etInputPhone.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetGetVerifyCode>() {
                    @Override
                    public void call(NetGetVerifyCode netGetVerifyCode) {
                        mNetGetVerifyCode = netGetVerifyCode;
                        Log.d(TAG, "MainActivity:test:getNetState:integer:" + netGetVerifyCode);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.d(TAG, "MainActivity:test:getNetState:error");
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Log.d(TAG, "MainActivity:test:getNetState:ok");
                    }
                });
    }

    @OnClick(R.id.btn_finish)
    public void OnClickFinish() {

        SceneModule.getInstance().checkVerifyCode(etInputPhone.getText().toString(), mNetGetVerifyCode.getIdentifier(), etInputVerificationCode.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<ResultObject>() {
                    @Override
                    public void call(ResultObject resultObject) {
                        if (resultObject.getResult().isResult()) {
                            ToastUtil.showToast(getApplicationContext(), "成功");

                            Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
                            intent.putExtra("CONTACT_NAME", etAddRemark.getText().toString());
                            intent.putExtra("CONTACT_PHONE", etInputPhone.getText().toString());
                            startActivity(intent);

                        }
                        Log.d(TAG, "MainActivity:test:getNetState:integer:" + resultObject);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.d(TAG, "MainActivity:test:getNetState:error");
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Log.d(TAG, "MainActivity:test:getNetState:ok");
                    }
                });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_add_scene_save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
