package com.aigo.router.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.ResultObject;
import com.aigo.router.ui.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EditContactActivity extends AppCompatActivity {

    private static final String TAG = EditContactActivity.class.getSimpleName();
    @Bind(R.id.et_contact_name)
    EditText etContactName;
    @Bind(R.id.et_contact_phone)
    EditText etContactPhone;
    @Bind(R.id.btn_finish)
    Button btnFinish;
    private String contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contactId = getIntent().getStringExtra("CONTACT_ID");
        etContactName.setText(getIntent().getStringExtra("CONTACT_NAME") + "");
        etContactPhone.setText(getIntent().getStringExtra("CONTACT_PHONE") + "");
    }

    @OnClick(R.id.btn_finish)
    public void onClickFinish() {

        SceneModule.getInstance().modifyUserLinkman(contactId, etContactPhone.getText().toString(),
                etContactName.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<ResultObject>() {
                    @Override
                    public void call(ResultObject resultObject) {
                        if (resultObject.getResult().isResult()) {
                            Intent intent = new Intent(getApplicationContext(),ContactActivity.class);
                            intent.putExtra("CONTACT_ID",contactId);
                            intent.putExtra("CONTACT_NAME",etContactName.getText().toString());
                            intent.putExtra("CONTACT_PHONE",etContactPhone.getText().toString());
                            startActivity(intent);
                            finish();
                            ToastUtil.showToast(getApplicationContext(), "修改成功");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
