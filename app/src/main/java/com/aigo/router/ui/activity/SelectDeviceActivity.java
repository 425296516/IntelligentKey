package com.aigo.router.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.NetDeviceType;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SelectDeviceActivity extends AppCompatActivity {

    private static final String TAG = SelectDeviceActivity.class.getSimpleName();
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private String mAction;
    private String mType;

    private List<NetDeviceType.TypeListBean> mTypeListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);
        ButterKnife.bind(this);

        mAction = getIntent().getStringExtra("ACTION_TYPE");

        if (mAction.equals("SELECT_DEVICE")) {
            mType = "1";
            setTitle("选择设备");
        } else if (mAction.equals("EXECUTE_ACTION")) {
            mType = "2";
            setTitle("执行动作");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectTypeList(mType);

    }

    public void selectTypeList(String type) {

        SceneModule.getInstance().getAllDeviceType(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetDeviceType>() {
                    @Override
                    public void call(NetDeviceType resultObject) {
                        if (resultObject.getResult().isResult()) {
                            mTypeListBean = resultObject.getTypeList();

                            recyclerView.setLayoutManager(new GridLayoutManager(SelectDeviceActivity.this, 2));
                            recyclerView.setAdapter(new HomeAdapter());
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


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            HomeAdapter.MyViewHolder holder = new HomeAdapter.MyViewHolder(LayoutInflater.from(
                    getApplicationContext()).inflate(R.layout.item_select_device_recyclerview, parent,
                    false));

            return holder;
        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, final int position) {

            Picasso.with(getApplicationContext()).load(mTypeListBean.get(position).getTypeIcon()).placeholder(R.drawable.ic_launcher).into(holder.ivDeviceIcon);

            holder.tvDeviceName.setText(mTypeListBean.get(position).getTypeName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mAction.equals("SELECT_DEVICE")) {
                        Intent intent = new Intent(getApplicationContext(), TriggerActivity.class);
                        intent.putExtra("TypeListBean",mTypeListBean.get(position));
                        startActivity(intent);
                    } else if (mAction.equals("EXECUTE_ACTION")) {

                        Intent intent = new Intent(getApplicationContext(), ExecuteActionActivity.class);
                        intent.putExtra("TypeListBean",mTypeListBean.get(position));
                        startActivity(intent);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {

            return mTypeListBean.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.iv_device_icon)
            ImageView ivDeviceIcon;
            @Bind(R.id.tv_device_name)
            TextView tvDeviceName;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
