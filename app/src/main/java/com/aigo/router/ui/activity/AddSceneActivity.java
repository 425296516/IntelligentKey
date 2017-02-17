package com.aigo.router.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.NetBehaviour;
import com.aigo.router.bussiness.bean.NetBindDeviceList;
import com.aigo.router.bussiness.bean.NetDeviceType;
import com.aigo.router.bussiness.bean.NetScene;
import com.aigo.router.bussiness.bean.ResultObject;
import com.aigo.router.ui.utils.ToastUtil;
import com.aigo.router.ui.view.UISwitchButton;
import com.aigo.usermodule.business.UserModule;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class AddSceneActivity extends AppCompatActivity {

    private static final String TAG = AddSceneActivity.class.getSimpleName();
    @Bind(R.id.btn_scene_status)
    UISwitchButton btnSceneStatus;
    @Bind(R.id.iv_add_method)
    ImageView btnAddMethod;
    @Bind(R.id.iv_add_action)
    ImageView btnAddAction;
    @Bind(R.id.iv_trumpet)
    ImageView ivTrumpet;
    @Bind(R.id.tv_trigger)
    TextView tvTrigger;
    @Bind(R.id.tv_add_action)
    TextView tvAddAction;

    private NetDeviceType.TypeListBean mTypeListBean;
    //private NetBehaviour mNetBehaviour;
    private String triggerId;
    private String executeId;
    private NetScene.SceneListBean mSceneListBean;
    private String contactPhone;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scene);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSceneListBean = (NetScene.SceneListBean) getIntent().getSerializableExtra("SceneBean");
        if (mSceneListBean != null) {
            triggerId = mSceneListBean.getTriggerId();
            executeId = mSceneListBean.getExecuteId();
            tvTrigger.setText(mSceneListBean.getTriggerName());
            tvAddAction.setText(mSceneListBean.getExecuteName());

            if(mSceneListBean.getIsPush().equals("1")){
                btnSceneStatus.setChecked(true);
                ivTrumpet.setImageResource(R.drawable.drw_1_trumpet);
            }else{
                btnSceneStatus.setChecked(false);
                ivTrumpet.setImageResource(R.drawable.drw_1_trumpet_offline);
            }

            btnSceneStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        ivTrumpet.setImageResource(R.drawable.drw_1_trumpet);
                    }else{
                        ivTrumpet.setImageResource(R.drawable.drw_1_trumpet_offline);
                    }
                }
            });
        }

    }

    private NetBindDeviceList.DeviceListBean mDeviceListBean;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //mNetBehaviour = (NetBehaviour) intent.getSerializableExtra("NetBehaviour");
        mTypeListBean = (NetDeviceType.TypeListBean) intent.getSerializableExtra("TypeListBean");

        NetBindDeviceList.DeviceListBean deviceListBean = (NetBindDeviceList.DeviceListBean) intent.getSerializableExtra("DeviceListBean");
        if (deviceListBean != null) {
            mDeviceListBean = deviceListBean;
        }

        contactPhone = intent.getStringExtra("CONTACT_PHONE");
        message = intent.getStringExtra("MESSAGE");

        String type = null;
        if (mTypeListBean.getType().equals("1")) {
            type = "1";
        } else if (mTypeListBean.getType().equals("2")) {
            type = "2";
        }

        SceneModule.getInstance().getBehaviorList(mTypeListBean.getDeviceTypeId(), type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetBehaviour>() {
                    @Override
                    public void call(NetBehaviour resultObject) {
                        if (resultObject.getResult().isResult()) {
                            if (mTypeListBean.getType().equals("1")) {
                                triggerId = resultObject.getBehaviourList().get(0).getBehaviourId();
                                tvTrigger.setText(mTypeListBean.getTypeName());
                            } else if (mTypeListBean.getType().equals("2")) {
                                executeId = resultObject.getBehaviourList().get(0).getBehaviourId();
                                tvAddAction.setText(mTypeListBean.getTypeName());
                            }

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

    @OnClick(R.id.iv_add_method)
    public void addMethod() {
        Intent intent = new Intent(this, SelectDeviceActivity.class);
        intent.putExtra("ACTION_TYPE", "SELECT_DEVICE");
        startActivity(intent);

    }

    @OnClick(R.id.iv_add_action)
    public void addAction() {
        Intent intent = new Intent(this, SelectDeviceActivity.class);
        intent.putExtra("ACTION_TYPE", "EXECUTE_ACTION");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_scene_save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.add_scene_save) {

            if (mSceneListBean == null) {
                addScene();
            } else {
                editScene();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public void editScene() {
        SceneModule.getInstance().modifyScene(mSceneListBean.getSceneId(),
                mSceneListBean.getTrigger_deviceSN(), mSceneListBean.getExecute_deviceSN(),
                mSceneListBean.getTriggerId(), mSceneListBean.getExecuteId(), mSceneListBean.getTrigger_describe(),
                mSceneListBean.getExecute_describe(), mSceneListBean.getLink_id(),
                mSceneListBean.getMessage(), mSceneListBean.getIsActivate(), (btnSceneStatus.isChecked() == true ? "1" : "0"))

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<ResultObject>() {
                    @Override
                    public void call(ResultObject resultObject) {
                        if (resultObject.getResult().isResult()) {
                            ToastUtil.showToast(getApplicationContext(), "编辑成功");
                            Log.d(TAG, "MainActivity:test:getNetState:integer:" + resultObject);
                        }
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

    public void addScene() {

        SceneModule.getInstance().addScene(UserModule.getInstance().getUser().getUsername(),
                mDeviceListBean.getDeviceSN(), "null", triggerId, executeId,
                mDeviceListBean.getNotes_name(), "智能按键111", contactPhone, message, "1",
                (btnSceneStatus.isChecked() == true ? "1" : "0"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<ResultObject>() {
                    @Override
                    public void call(ResultObject resultObject) {
                        if (resultObject.getResult().isResult()) {
                            ToastUtil.showToast(getApplicationContext(), "保存成功");
                            Log.d(TAG, "MainActivity:test:getNetState:integer:" + resultObject);
                        }
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

}
