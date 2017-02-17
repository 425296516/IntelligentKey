package com.aigo.router.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.NetBindDeviceList;
import com.aigo.router.bussiness.bean.ResultObject;
import com.aigo.router.ui.adapter.MultiSettingSelectAdapter;
import com.aigo.router.ui.view.DividerItemDecoration;
import com.aigo.usermodule.business.UserModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DeviceListActivity extends AppCompatActivity {

    private static final String TAG = DeviceListActivity.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.linear_select_bottom)
    LinearLayout mLinearSelect;
    //private HomeAdapter mHomeAdapter;
    private List<NetBindDeviceList.DeviceListBean> deviceListBeen;
    private MultiSettingSelectAdapter mAdapter;
    @Bind(R.id.rl_all_select)
    RelativeLayout rlAllSelect;
    @Bind(R.id.radio_button)
    RadioButton mRadioButton;
    private boolean mSelectStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String type = getIntent().getStringExtra("TYPE");
        setTitle(getIntent().getStringExtra("TITLE"));

        recyclerView.setLayoutManager(new LinearLayoutManager(DeviceListActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(DeviceListActivity.this,DividerItemDecoration.VERTICAL_LIST));

        SceneModule.getInstance().getUserBindDeviceList(UserModule.getInstance().getUser().getUsername(), "" + type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetBindDeviceList>() {
                    @Override
                    public void call(NetBindDeviceList resultObject) {
                        if (resultObject.getResult().isResult()) {
                            deviceListBeen = resultObject.getDeviceList();

                            mAdapter = new MultiSettingSelectAdapter(DeviceListActivity.this);
                            mAdapter.addItems(deviceListBeen);

                            recyclerView.setAdapter(mAdapter);

                            mAdapter.setOnActionModeCallBack(new MultiSettingSelectAdapter.OnActionModeCallBack() {
                                @Override
                                public void showActionMode() {

                                    mAdapter.setIsActionModeShow(true);
                                    mAdapter.notifyDataSetChanged();

                                    mLinearSelect.setVisibility(View.VISIBLE);
                                    startSupportActionMode(mDeleteMode);
                                }
                            });
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

    @OnClick(R.id.rl_all_select)
    public void onClickAllSelect(){
        if (!mSelectStatus) {
            mSelectStatus = true;
        } else {
            mSelectStatus = false;
        }
        mRadioButton.setChecked(mSelectStatus);
        mAdapter.setAllSelectRadio(mSelectStatus);
    }

    @OnClick(R.id.button_delete)
    public void onClickDelete() {

        deleteKeyDialog();
    }

    public void deleteKeyDialog(){

        final AlertDialog exitDialog = new AlertDialog.Builder(this, R.style.Theme_Light_Dialog).create();
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.setContentView(R.layout.dialog_delete_linkman_tip);

        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

        TextView tVcontent = (TextView) window.findViewById(R.id.tv_content);
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);

        tVcontent.setText("场景删除后\n将会失效，并不再执行条件");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();

                final List<Integer> selectList = mAdapter.getMultiSelectPositions();

                List<String> deviceSNList = new ArrayList<String>();
                for(int i=0;i<selectList.size();i++){

                    String deviceSN = deviceListBeen.get(selectList.get(i)).getDeviceSN().replace(":", "").toLowerCase();
                    deviceSNList.add(deviceSN);
                }
                SceneModule.getInstance().UserUnbindDevice(UserModule.getInstance().getUser().getUsername(), deviceSNList)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Action1<ResultObject>() {
                            @Override
                            public void call(ResultObject resultObject) {
                                if (resultObject.getResult().isResult()) {
                                    recyclerView.getAdapter().notifyDataSetChanged();
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
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });

    }

    private ActionMode.Callback mDeleteMode = new ActionMode.Callback() {


        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mAdapter.setIsActionModeShow(false);

            mLinearSelect.setVisibility(View.GONE);
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_trigger_next, menu);
            actionMode.setTitle("智能按键");
            actionMode.getMenu().getItem(0).setTitle("完成");
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int i = menuItem.getItemId();
            if (i == R.id.action_next) {
                actionMode.finish();
                return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        menu.getItem(1).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }else if(id == R.id.add_scene_edit){
            mAdapter.setIsActionModeShow(true);
            mAdapter.notifyDataSetChanged();

            mLinearSelect.setVisibility(View.VISIBLE);
            startSupportActionMode(mDeleteMode);
        }
        return super.onOptionsItemSelected(item);
    }

}
