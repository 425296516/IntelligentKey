package com.aigo.router.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.ExecuteRecords;
import com.aigo.router.bussiness.bean.NetDeviceList;
import com.aigo.router.ui.view.DividerItemDecoration;
import com.aigo.router.ui.view.PickerView;
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

import static com.aigo.router.ui.utils.TimeUtil.getDay;
import static com.aigo.router.ui.utils.TimeUtil.getDayAndWeek;
import static com.aigo.router.ui.utils.TimeUtil.getHourMinute;
import static com.aigo.router.ui.utils.TimeUtil.getNowMonth;
import static com.aigo.router.ui.utils.TimeUtil.getNowYear;

public class ExecuteRecordsActivity extends AppCompatActivity {

    private static final String TAG = ExecuteRecordsActivity.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tv_year)
    TextView tvYear;
    @Bind(R.id.tv_month)
    TextView tvMonth;
    @Bind(R.id.iv_month_arrow)
    ImageView ivMonthArrow;
    @Bind(R.id.tv_record_num)
    TextView tvRecordNum;
    @Bind(R.id.tv_select)
    TextView tvSelect;
    @Bind(R.id.iv_select_arrow)
    ImageView ivSelectArrow;

    private HomeAdapter mHomeAdapter;
    private List<ExecuteRecords.LogListBean> mLogListBeen = new ArrayList<>();
    private static final int TITLE = 1;
    private static final int CONTENT = 2;
    private int mCurrentDay;
    private List<NetDeviceList.DeviceListBean.DeviceBean> mDeviceBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_reocrds);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvYear.setText(getNowYear() + "");
        tvMonth.setText(getNowMonth() + "");

        recyclerView.setLayoutManager(new LinearLayoutManager(ExecuteRecordsActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(ExecuteRecordsActivity.this, DividerItemDecoration.VERTICAL_LIST));

        loadExecuteRecordData(getNowYear(), getNowMonth(), "");

        loadDeviceList();

    }

    public void loadDeviceList() {

        SceneModule.getInstance().getAllUserDeviceList(UserModule.getInstance().getUser().getUsername())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetDeviceList>() {
                    @Override
                    public void call(NetDeviceList resultObject) {
                        if (resultObject.getResult().isResult()) {
                            NetDeviceList.DeviceListBean.DeviceBean deviceBean = new NetDeviceList.DeviceListBean.DeviceBean();
                            deviceBean.setRemarks("全部");
                            mDeviceBeanList.add(deviceBean);

                            for (int i = 0; i < resultObject.getDeviceList().size(); i++) {
                                mDeviceBeanList.addAll(resultObject.getDeviceList().get(i).getDevice());
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

    public void loadExecuteRecordData(int year, int month, String deviceSn) {
        SceneModule.getInstance().getSceneLog("ceshi1", year, month, deviceSn)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<ExecuteRecords>() {
                    @Override
                    public void call(ExecuteRecords resultObject) {

                        mLogListBeen = resultObject.getLogList();

                        for (int i = 0; i < resultObject.getLogList().size(); i++) {

                            if (i == 0) {
                                mCurrentDay = getDay(resultObject.getLogList().get(i).getExecuteTime());
                                ExecuteRecords.LogListBean logListBean = new ExecuteRecords.LogListBean();
                                logListBean.setType(1);
                                logListBean.setExecuteTime(resultObject.getLogList().get(i).getExecuteTime());
                                mLogListBeen.add(0, logListBean);
                            } else {

                                if (getDay(mLogListBeen.get(i).getExecuteTime()) != mCurrentDay) {
                                    mCurrentDay = getDay(resultObject.getLogList().get(i).getExecuteTime());
                                    ExecuteRecords.LogListBean logListBean = new ExecuteRecords.LogListBean();
                                    logListBean.setType(1);
                                    logListBean.setExecuteTime(resultObject.getLogList().get(i).getExecuteTime());
                                    mLogListBeen.add(i, logListBean);
                                }
                            }
                        }

                        mHomeAdapter = new HomeAdapter();
                        recyclerView.setAdapter(mHomeAdapter);

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

    String year;
    String month;

    @OnClick(R.id.iv_month_arrow)
    public void onClickMonthArrow() {

        final AlertDialog exitDialog = new AlertDialog.Builder(this, R.style.Theme_Light_Dialog).create();
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.setContentView(R.layout.dialog_select_time);

        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

        Button ok = (Button) window.findViewById(R.id.btn_ok);
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);

        final PickerView minute_pv = (PickerView) window.findViewById(R.id.minute_pv);
        PickerView second_pv = (PickerView) window.findViewById(R.id.second_pv);
        List<String> data = new ArrayList<String>();
        List<String> seconds = new ArrayList<String>();
        for (int i = 2000; i < 2020; i++) {
            data.add(i + "");
        }
        for (int i = 1; i < 13; i++) {
            seconds.add(i < 10 ? "0" + i : "" + i);
        }

        minute_pv.setData(data);
        minute_pv.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                year = text;
            }
        });
        second_pv.setData(seconds);
        second_pv.setOnSelectListener(new PickerView.onSelectListener() {

            @Override
            public void onSelect(String text) {
                month = text;
            }
        });

        minute_pv.setSelected(Integer.parseInt(tvYear.getText().toString()));
        second_pv.setSelected(Integer.parseInt(tvMonth.getText().toString()) - 1);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(year)) {
                    tvYear.setText(year);
                }

                if (!TextUtils.isEmpty(month)) {
                    tvMonth.setText(month);
                }

                loadExecuteRecordData(Integer.parseInt(tvYear.getText().toString()), Integer.parseInt(tvMonth.getText().toString()), (mDeviceSN == null ? "" : mDeviceSN));

                exitDialog.dismiss();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });

    }

    @OnClick(R.id.tv_select)
    public void onClickSelect() {
        showPopupWindow();
    }

    PopupWindow popupWindow;

    private void showPopupWindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.dlg_trigger_bind_device, null);
        // 设置按钮的点击事件
        TextView button = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView baiduNavi = (TextView) contentView.findViewById(R.id.tv_confirm);

        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(ExecuteRecordsActivity.this));
        recyclerView.setAdapter(new SelectDeviceAdapter());

        float height = getWindow().getDecorView().getHeight();

        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, (int) (height * 3 / 10), true);

        popupWindow.setTouchable(true);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                popupWindow.dismiss();
            }
        });

        baiduNavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.drawable_ffffff));

        popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private String mDeviceSN;

    class SelectDeviceAdapter extends RecyclerView.Adapter<SelectDeviceAdapter.ViewHolder> {

        @Override
        public SelectDeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SelectDeviceAdapter.ViewHolder holder = new SelectDeviceAdapter.ViewHolder(LayoutInflater.from(
                    getApplicationContext()).inflate(R.layout.item_bind_device_trigger, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(SelectDeviceAdapter.ViewHolder holder, final int position) {

            holder.tvBindDevice.setText(mDeviceBeanList.get(position).getRemarks());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 0) {
                        mDeviceSN = null;
                    } else {
                        mDeviceSN = mDeviceBeanList.get(position).getDeviceSN();
                    }

                    loadExecuteRecordData(Integer.parseInt(tvYear.getText().toString()), Integer.parseInt(tvMonth.getText().toString()), (mDeviceSN==null ? "" : mDeviceSN));
                    tvSelect.setText(mDeviceBeanList.get(position).getRemarks());
                    popupWindow.dismiss();
                }
            });

        }

        @Override
        public int getItemCount() {

            return mDeviceBeanList.size();
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


    class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == CONTENT) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_execute_record_recyclewview, parent, false);
                return new MyViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bind_listview, parent, false);
                return new MyTitleViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

            if (viewHolder instanceof MyViewHolder) {
                MyViewHolder holder = (MyViewHolder) viewHolder;
                holder.tvDeviceName.setText(mLogListBeen.get(position).getTrigger_device_name());
                holder.tvExecuteTrigger.setText(mLogListBeen.get(position).getExecuteInfo() + "");
                holder.tvTime.setText(getHourMinute(mLogListBeen.get(position).getExecuteTime()));
                if(mLogListBeen.get(position).getDeviceTypeId().equals("2")){
                    holder.ivIcon.setImageResource(R.drawable.drw_1_intelligent_sos_icon);
                }

            } else if (viewHolder instanceof MyTitleViewHolder) {
                MyTitleViewHolder holder = (MyTitleViewHolder) viewHolder;
                holder.tvDeviceName.setText(getDayAndWeek(mLogListBeen.get(position).getExecuteTime()));
            }

        }


        @Override
        public int getItemViewType(int position) {
            if (mLogListBeen.get(position).getType() == 1) {
                return TITLE;
            } else {
                return CONTENT;
            }
        }


        @Override
        public int getItemCount() {

            return mLogListBeen.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.iv_icon)
            ImageView ivIcon;
            @Bind(R.id.tv_device_name)
            TextView tvDeviceName;
            @Bind(R.id.tv_execute_trigger)
            TextView tvExecuteTrigger;
            @Bind(R.id.tv_time)
            TextView tvTime;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        class MyTitleViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.tv_bind_device)
            TextView tvDeviceName;

            public MyTitleViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

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
