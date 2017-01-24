package com.aigo.router.ui.activity;

import android.app.AlertDialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.NetBindDeviceList;
import com.aigo.router.bussiness.bean.NetDeviceType;
import com.aigo.router.ui.utils.ToastUtil;
import com.aigo.usermodule.business.UserModule;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TriggerActivity extends AppCompatActivity {

    private static final String TAG = TriggerActivity.class.getSimpleName();

    @Bind(R.id.tv_select_key)
    TextView btnSelectKey;

    private NetDeviceType.TypeListBean mTypeListBean;
    //private NetBehaviour mNetBehaviour;
    private List<NetBindDeviceList.DeviceListBean> mDeviceListBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTypeListBean = (NetDeviceType.TypeListBean) getIntent().getSerializableExtra("TypeListBean");

        SceneModule.getInstance().getUserBindDeviceList(
                UserModule.getInstance().getUser().getUsername(), mTypeListBean.getDeviceTypeId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetBindDeviceList>() {
                    @Override
                    public void call(NetBindDeviceList resultObject) {
                        if (resultObject.getResult().isResult()) {
                            mDeviceListBeanList = resultObject.getDeviceList();
                            ToastUtil.showToast(getApplicationContext(), "成功" + resultObject.getDeviceList().size());
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

    private AlertDialog exitDialog;

    @OnClick(R.id.tv_select_key)
    public void OnClickSelectKey() {

        exitDialog = new AlertDialog.Builder(this).create();
        exitDialog.setCancelable(true);
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        window.setContentView(R.layout.dlg_trigger_bind_device);

        final RecyclerView recyclerView = (RecyclerView) window.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HomeAdapter mHomeAdapter = new HomeAdapter();
        recyclerView.setAdapter(mHomeAdapter);

    }

    private NetBindDeviceList.DeviceListBean mDeviceListBean;

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HomeAdapter.MyViewHolder holder = new HomeAdapter.MyViewHolder(LayoutInflater.from(
                    getApplicationContext()).inflate(R.layout.item_contact_recyclerview, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, final int position) {

            holder.tvContactName.setText(mDeviceListBeanList.get(position).getNotes_name());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDeviceListBean = mDeviceListBeanList.get(position);

                    btnSelectKey.setText(mDeviceListBeanList.get(position).getNotes_name());
                    exitDialog.dismiss();
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
        getMenuInflater().inflate(R.menu.menu_trigger_next, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.action_next) {
            Intent intent = new Intent(this, AddSceneActivity.class);
            //intent.putExtra("NetBehaviour", mNetBehaviour);
            intent.putExtra("TypeListBean", mTypeListBean);
            intent.putExtra("DeviceListBean", mDeviceListBean);

            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
