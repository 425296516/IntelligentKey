package com.aigo.router.ui.activity;

import android.content.Intent;
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
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.NetDeviceList;
import com.aigo.usermodule.business.UserModule;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DeviceManagerActivity extends AppCompatActivity {

    private static final String TAG = DeviceManagerActivity.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private HomeAdapter mHomeAdapter;
    //private List<NetBindDeviceList.DeviceListBean> deviceListBeen;
    private List<NetDeviceList.DeviceListBean> mDeviceListBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onResume() {
        super.onResume();
        SceneModule.getInstance().getAllUserDeviceList(UserModule.getInstance().getUser().getUsername())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetDeviceList>() {
                    @Override
                    public void call(NetDeviceList resultObject) {
                        if (resultObject.getResult().isResult()) {
                            mDeviceListBeanList = resultObject.getDeviceList();
                            recyclerView.setLayoutManager(new LinearLayoutManager(DeviceManagerActivity.this));
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
                    getApplicationContext()).inflate(R.layout.item_devicemanager_recyclerview, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, final int position) {

            holder.tvContactName.setText(mDeviceListBeanList.get(position).getTypeName());
            holder.tvContactPhone.setText(mDeviceListBeanList.get(position).getCount() + "");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), DeviceListActivity.class);
                    intent.putExtra("TITLE",mDeviceListBeanList.get(position).getTypeName());
                    intent.putExtra("TYPE",mDeviceListBeanList.get(position).getDeviceTypeId());
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {

            return mDeviceListBeanList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.tv_contact_name)
            TextView tvContactName;
            @Bind(R.id.tv_contact_phone)
            TextView tvContactPhone;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_add_scene_save, menu);
        menu.getItem(0).setTitle("添加");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }else if(id == R.id.add_scene_save){
            Intent intent = new Intent(getApplicationContext(),BindDeviceActivity.class);

            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
