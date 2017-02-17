package com.aigo.router.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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


    @OnClick(R.id.tv_select_key)
    public void OnClickSelectKey() {

        showPopupWindow();
    }

    private NetBindDeviceList.DeviceListBean mDeviceListBean;

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HomeAdapter.MyViewHolder holder = new HomeAdapter.MyViewHolder(LayoutInflater.from(
                    getApplicationContext()).inflate(R.layout.item_bind_device_trigger, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, final int position) {

            holder.tvBindDevice.setText(mDeviceListBeanList.get(position).getNotes_name());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDeviceListBean = mDeviceListBeanList.get(position);

                    btnSelectKey.setText(mDeviceListBeanList.get(position).getNotes_name());
                    popupWindow.dismiss();
                }
            });

        }

        @Override
        public int getItemCount() {

            return mDeviceListBeanList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.tv_bind_device)
            TextView tvBindDevice;
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

    PopupWindow popupWindow;
    private void showPopupWindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.dlg_trigger_bind_device, null);
        // 设置按钮的点击事件
        TextView button = (TextView) contentView.findViewById(R.id.tv_cancel);
        TextView baiduNavi = (TextView)contentView.findViewById(R.id.tv_confirm);

        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HomeAdapter mHomeAdapter = new HomeAdapter();
        recyclerView.setAdapter(mHomeAdapter);

        float height =  getWindow().getDecorView().getHeight();

        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, (int)(height*3/10), true);

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

}
