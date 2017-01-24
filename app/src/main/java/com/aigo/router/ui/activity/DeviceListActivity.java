package com.aigo.router.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.NetBindDeviceList;
import com.aigo.router.bussiness.bean.ResultObject;
import com.aigo.router.ui.utils.ToastUtil;
import com.aigo.usermodule.business.UserModule;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DeviceListActivity extends AppCompatActivity {

    private static final String TAG = DeviceListActivity.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private HomeAdapter mHomeAdapter;
    private List<NetBindDeviceList.DeviceListBean> deviceListBeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String type = getIntent().getStringExtra("TYPE");
        setTitle(getIntent().getStringExtra("TITLE"));

        SceneModule.getInstance().getUserBindDeviceList(UserModule.getInstance().getUser().getUsername(), "" + type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetBindDeviceList>() {
                    @Override
                    public void call(NetBindDeviceList resultObject) {
                        if (resultObject.getResult().isResult()) {
                            deviceListBeen = resultObject.getDeviceList();
                            recyclerView.setLayoutManager(new LinearLayoutManager(DeviceListActivity.this));
                            mHomeAdapter = new HomeAdapter();
                            recyclerView.setAdapter(mHomeAdapter);
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

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HomeAdapter.MyViewHolder holder = new HomeAdapter.MyViewHolder(LayoutInflater.from(
                    getApplicationContext()).inflate(R.layout.item_contact_recyclerview, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, final int position) {

            holder.tvContactName.setText(deviceListBeen.get(position).getNotes_name());

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final AlertDialog exitDialog = new AlertDialog.Builder(DeviceListActivity.this).create();
                    exitDialog.setCancelable(true);
                    exitDialog.show();
                    Window window = exitDialog.getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    window.setContentView(R.layout.dialog_delete_device);

                    TextView llDeleteDevice = (TextView) window.findViewById(R.id.tv_delete);
                    TextView llUpdateDevice = (TextView) window.findViewById(R.id.tv_update_remark);

                    llDeleteDevice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exitDialog.dismiss();
                            SceneModule.getInstance().UserUnbindDevice(UserModule.getInstance().getUser().getUsername(), deviceListBeen.get(position).getDeviceSN())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(new Action1<ResultObject>() {
                                        @Override
                                        public void call(ResultObject resultObject) {
                                            if (resultObject.getResult().isResult()) {
                                                deviceListBeen.remove(position);

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

                    llUpdateDevice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exitDialog.dismiss();
                            setRemarkName(position);

                        }
                    });

                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {

            return deviceListBeen.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.tv_contact_name)
            TextView tvContactName;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    public void setRemarkName(final int position) {

        // 隐藏输入法
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        final AlertDialog exitDialog = new AlertDialog.Builder(this).create();
        exitDialog.setCancelable(true);
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        window.setContentView(R.layout.dialog_update_device);
        final EditText mRemark = (EditText) window.findViewById(R.id.et_input_remark);
        Button okBtn = (Button) window.findViewById(R.id.btn_ok);
        Button cancelBtn = (Button) window.findViewById(R.id.btn_cancel);

        mRemark.setText(deviceListBeen.get(position).getNotes_name());

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();

                SceneModule.getInstance().modifyRemark(UserModule.getInstance().getUser().getUsername(),
                        deviceListBeen.get(position).getDeviceSN(), mRemark.getText().toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Action1<ResultObject>() {
                            @Override
                            public void call(ResultObject resultObject) {
                                if (resultObject.getResult().isResult()) {
                                    deviceListBeen.get(position).setNotes_name(mRemark.getText().toString());
                                    recyclerView.getAdapter().notifyItemChanged(position);
                                    ToastUtil.showToast(getApplicationContext(), "备注成功");
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
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_main, menu);

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
