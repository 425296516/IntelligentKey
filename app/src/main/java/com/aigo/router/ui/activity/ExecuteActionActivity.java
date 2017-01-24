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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.Message;
import com.aigo.router.bussiness.bean.NetBehaviour;
import com.aigo.router.bussiness.bean.NetDeviceType;
import com.aigo.router.ui.utils.ToastUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ExecuteActionActivity extends AppCompatActivity {

    private static final String TAG = ExecuteActionActivity.class.getSimpleName();
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.tv_contact_name)
    TextView tvContactName;
    @Bind(R.id.linear_contact)
    LinearLayout linearContact;
    @Bind(R.id.tv_message)
    TextView tvMessage;


    private NetDeviceType.TypeListBean mTypeListBean;
    private NetBehaviour mNetBehaviour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_action);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTypeListBean = (NetDeviceType.TypeListBean) getIntent().getSerializableExtra("TypeListBean");

        SceneModule.getInstance().getMessageTemplate()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<Message>() {
                    @Override
                    public void call(Message resultObject) {
                        if (resultObject.getResult().isResult()) {
                            mMessageList = resultObject.getMessageList();
                            ToastUtil.showToast(getApplicationContext(), "成功");
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


        SceneModule.getInstance().getBehaviorList(mTypeListBean.getDeviceTypeId(), "2")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetBehaviour>() {
                    @Override
                    public void call(NetBehaviour resultObject) {
                        if (resultObject.getResult().isResult()) {
                            mNetBehaviour = resultObject;
                            ToastUtil.showToast(getApplicationContext(), "成功");
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

    @OnClick(R.id.linear_contact)
    public void OnClickContact(View view) {
        Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
        intent.putExtra("SELECT_CONTACT", true);
        startActivityForResult(intent, 1);
    }

    private AlertDialog exitDialog;

    @OnClick(R.id.tv_message)
    public void OnClickMessage() {
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

    private List<String> mMessageList;

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HomeAdapter.MyViewHolder holder = new HomeAdapter.MyViewHolder(LayoutInflater.from(
                    getApplicationContext()).inflate(R.layout.item_contact_recyclerview, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, final int position) {

            holder.tvContactName.setText(mMessageList.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    tvMessage.setText(mMessageList.get(position));
                    exitDialog.dismiss();
                }
            });

        }

        @Override
        public int getItemCount() {

            return mMessageList.size();
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

    private String mContactId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == 2) {
            mContactId = data.getStringExtra("CONTACT_ID");
            tvPhone.setText(data.getStringExtra("CONTACT_PHONE"));
            tvContactName.setText(data.getStringExtra("CONTACT_NAME"));
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
            intent.putExtra("NetBehaviour", mNetBehaviour);
            intent.putExtra("TypeListBean", mTypeListBean);
            intent.putExtra("CONTACT_PHONE", mContactId);
            intent.putExtra("MESSAGE", tvMessage.getText().toString());

            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
