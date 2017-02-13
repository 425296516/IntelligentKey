package com.aigo.router.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.ExecuteRecords;
import com.aigo.router.ui.view.DividerItemDecoration;
import com.aigo.router.ui.view.PickerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
    private List<ExecuteRecords.LogListBean> mLogListBeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_reocrds);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SceneModule.getInstance().getSceneLog("ceshi1", 2017, 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<ExecuteRecords>() {
                    @Override
                    public void call(ExecuteRecords resultObject) {

                        mLogListBeen = resultObject.getLogList();

                        recyclerView.setLayoutManager(new LinearLayoutManager(ExecuteRecordsActivity.this));
                        recyclerView.addItemDecoration(new DividerItemDecoration(ExecuteRecordsActivity.this, DividerItemDecoration.VERTICAL_LIST));

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

     String year ;
     String month ;

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
            data.add(i+"");
        }
        for (int i = 1; i < 13; i++) {
            seconds.add(i < 10 ? "0" + i: "" + i);
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
        second_pv.setSelected(Integer.parseInt(tvMonth.getText().toString())-1);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(year)){
                    tvYear.setText(year);
                }

                if(!TextUtils.isEmpty(month)){
                    tvMonth.setText(month);
                }

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

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getApplicationContext()).inflate(R.layout.item_execute_record_recyclewview, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            holder.tvDeviceName.setText(mLogListBeen.get(position).getTrigger_device_name());
            holder.tvExecuteTrigger.setText(mLogListBeen.get(position).getExecuteInfo() + "");
            holder.tvTime.setText(mLogListBeen.get(position).getExecuteTime());
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
