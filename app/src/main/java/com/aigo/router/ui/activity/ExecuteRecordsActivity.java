package com.aigo.router.ui.activity;

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
import com.aigo.router.bussiness.bean.ExecuteRecords;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ExecuteRecordsActivity extends AppCompatActivity {

    private static final String TAG = ExecuteRecordsActivity.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private HomeAdapter mHomeAdapter;
    private List<ExecuteRecords.LogListBean> mLogListBeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execute_reocrds);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SceneModule.getInstance().getSceneLog("ceshi1",2017,1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<ExecuteRecords>() {
                    @Override
                    public void call(ExecuteRecords resultObject) {

                        mLogListBeen = resultObject.getLogList();

                        recyclerView.setLayoutManager(new LinearLayoutManager(ExecuteRecordsActivity.this));
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


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HomeAdapter.MyViewHolder holder = new HomeAdapter.MyViewHolder(LayoutInflater.from(
                    getApplicationContext()).inflate(R.layout.item_contact_recyclerview, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(HomeAdapter.MyViewHolder holder, final int position) {

            holder.tvContactName.setText(mLogListBeen.get(position).getTrigger_device_name()+"\n"+mLogListBeen.get(position).getExecuteInfo());
            holder.tvContactPhone.setText(mLogListBeen.get(position).getExecuteTime()+"");

        }

        @Override
        public int getItemCount() {

            return mLogListBeen.size();
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
