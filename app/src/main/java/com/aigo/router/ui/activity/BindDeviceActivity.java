package com.aigo.router.ui.activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.MiBand;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.ResultObject;
import com.aigo.router.ui.utils.ToastUtil;
import com.aigo.usermodule.business.UserModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BindDeviceActivity extends AppCompatActivity {

    private static final String TAG = BindDeviceActivity.class.getSimpleName();


    HashMap<String, BluetoothDevice> devices = new HashMap<String, BluetoothDevice>();
    @Bind(R.id.tv_bind_device)
    TextView tvBindDevice;
    @Bind(R.id.tv_bind_device_name)
    TextView tvBindDeviceName;
    @Bind(R.id.tv_bind_device_info)
    TextView tvBindDeviceInfo;
    @Bind(R.id.iv_bind_device_icon)
    ImageView ivBindDeviceIcon;
    @Bind(R.id.iv_point)
    ImageView ivPoint;
    @Bind(R.id.tv_wifi)
    TextView tvWifi;
    @Bind(R.id.tv_aigo_kt03)
    TextView tvAigoKt03;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.linear_bind_device_info)
    LinearLayout linearBindDeviceInfo;
    @Bind(R.id.btn_start_bind)
    Button btnStartBind;
    @Bind(R.id.tv_searching)
    TextView tvSearching;
    @Bind(R.id.btn_start_use)
    Button btnStartUse;

    private BluetoothManager bluetoothManager = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private ScanCallback scanCallback;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device);
        ButterKnife.bind(this);

        getSupportActionBar().hide();

        tvBindDevice.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        //检查蓝牙是否打开
        openBbletooth(this);

        adapter = new ArrayAdapter<String>(this, R.layout.item_bind_listview,
                new ArrayList<String>());

        //扫描附近的设备
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                BluetoothDevice device = result.getDevice();
                Log.d(TAG, "找到附近的蓝牙设备: name:" + device.getName() + ",uuid:" + device.getUuids() + ",add:" + device.getAddress() + ",type:" + device.getType() + ",bondState:" + device.getBondState() + ",rssi:" + result.getRssi());

                String item = device.getName() + "|" + device.getAddress();
                if (!devices.containsKey(item)) {
                    devices.put(item, device);
                    adapter.add(item);
                }

                MiBand.stopScan(scanCallback);

                btnStartBind.setVisibility(View.GONE);
                linearBindDeviceInfo.setVisibility(View.GONE);
                tvSearching.setVisibility(View.VISIBLE);
                tvSearching.setText("绑定中...");
                tvBindDeviceInfo.setText("请保持设备靠近路由器和手机");
                ivBindDeviceIcon.setImageResource(R.drawable.drw_1_bind_device_icon);

                SceneModule.getInstance().UserBindDevice(UserModule.getInstance().getUser().getUsername(),
                        device.getAddress(), device.getName(),
                        device.getType() + "", "客厅智能按键" + new Random().nextInt(1000))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Action1<ResultObject>() {
                            @Override
                            public void call(ResultObject resultObject) {
                                if (resultObject.getResult().isResult()) {
                                    btnStartUse.setVisibility(View.VISIBLE);
                                    btnStartUse.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                        }
                                    });
                                    tvBindDeviceName.setText("绑定成功");
                                    linearBindDeviceInfo.setVisibility(View.GONE);
                                    tvSearching.setVisibility(View.GONE);
                                    tvBindDeviceInfo.setText("请保持设备靠近路由器和手机");
                                    ivBindDeviceIcon.setImageResource(R.drawable.drw_1_bind_device_success);

                                    ToastUtil.showToast(getApplicationContext(), "绑定成功");
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
        };

        for(int i=0;i<2;i++){

            mDeviceSNList.add("sos"+i);

        }

        //onClickSelectBindDevice();

        onClickAddDeviceRemark();

        //onClickBindFailure();
    }


    public void onClickSelectBindDevice() {

        final AlertDialog exitDialog = new AlertDialog.Builder(this, R.style.Theme_Light_Dialog).create();
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.setContentView(R.layout.dialog_select_bind_device);

        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

        RecyclerView recyclerView = (RecyclerView) window.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SelectDeviceAdapter());

        Button ok = (Button) window.findViewById(R.id.btn_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exitDialog.dismiss();

            }
        });


    }


    public void onClickAddDeviceRemark() {

        final AlertDialog exitDialog = new AlertDialog.Builder(this, R.style.Theme_Light_Dialog).create();
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.setContentView(R.layout.dialog_add_device_remark);

        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

        EditText editText = (EditText) window.findViewById(R.id.et_input_remark);

        Button cancel = (Button) window.findViewById(R.id.btn_cancel);

        Button ok = (Button) window.findViewById(R.id.btn_ok);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exitDialog.dismiss();

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exitDialog.dismiss();

            }
        });


    }


    public void onClickBindFailure() {

        final AlertDialog exitDialog = new AlertDialog.Builder(this, R.style.Theme_Light_Dialog).create();
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.setContentView(R.layout.dialog_bind_device_failure);

        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

        Button ok = (Button) window.findViewById(R.id.btn_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exitDialog.dismiss();

            }
        });


    }

    private List<String> mDeviceSNList = new ArrayList<>();

    class SelectDeviceAdapter extends RecyclerView.Adapter<SelectDeviceAdapter.ViewHolder> {

        @Override
        public SelectDeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SelectDeviceAdapter.ViewHolder holder = new SelectDeviceAdapter.ViewHolder(LayoutInflater.from(
                    getApplicationContext()).inflate(R.layout.item_bind_device_trigger, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(SelectDeviceAdapter.ViewHolder holder, final int position) {

            holder.tvBindDevice.setText(mDeviceSNList.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

        @Override
        public int getItemCount() {

            return mDeviceSNList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.tv_bind_device)
            TextView tvBindDevice;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }


    @OnClick(R.id.btn_start_bind)
    public void OnClickStartBind() {

        MiBand.startScan(scanCallback);

        btnStartBind.setVisibility(View.GONE);
        linearBindDeviceInfo.setVisibility(View.GONE);
        tvSearching.setVisibility(View.VISIBLE);
        tvBindDeviceInfo.setText("最长可以居中编辑十二个字");
        ivBindDeviceIcon.setImageResource(R.drawable.drw_1_bind_device_search);

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

    /**
     * 检查蓝牙是否打开并且启动打开蓝牙的方法
     */
    public void openBbletooth(Context context) {
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //判断蓝牙是否开启
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            //打开蓝牙
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(enableIntent);
        }
    }

}
