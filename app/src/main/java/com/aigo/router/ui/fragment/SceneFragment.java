package com.aigo.router.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.NetDeviceList;
import com.aigo.router.bussiness.bean.NetScene;
import com.aigo.router.bussiness.bean.ResultObject;
import com.aigo.router.ui.activity.AddSceneActivity;
import com.aigo.router.ui.activity.BindDeviceActivity;
import com.aigo.router.ui.activity.MainActivity;
import com.aigo.router.ui.adapter.EditSceneMultipleAdapter;
import com.aigo.router.ui.view.DividerItemDecoration;
import com.aigo.usermodule.business.UserModule;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SceneFragment extends Fragment {

    private static final String TAG = SceneFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.linear_no_scene)
    LinearLayout linearNoScene;

    private String mParam1;
    private String mParam2;

    public SceneFragment() {

    }

    public static SceneFragment newInstance(String param1, String param2) {
        SceneFragment fragment = new SceneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scene, container, false);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        SceneModule.getInstance().getAllUserDeviceList(UserModule.getInstance().getUser().getUsername())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetDeviceList>() {
                    @Override
                    public void call(NetDeviceList resultObject) {
                        if (resultObject.getResult().isResult() && resultObject.getDeviceList().size() == 0) {

                            isBindDialog();
                        } else {

                            SceneModule.getInstance().checkDeviceOnline(resultObject.getDeviceList().get(0).getDevice().get(0).getDeviceSN(), UserModule.getInstance().getTokenSync())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(new Action1<ResultObject>() {
                                        @Override
                                        public void call(ResultObject resultObject) {
                                            if (resultObject.getResult().isResult()) {

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


        SceneModule.getInstance().getUserScene(UserModule.getInstance().getUser().getUsername())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetScene>() {
                    @Override
                    public void call(NetScene resultObject) {
                        if (resultObject.getResult().isResult()) {
                            if(resultObject.getSceneList().size()==0){
                                linearNoScene.setVisibility(View.VISIBLE);
                           }else{
                                linearNoScene.setVisibility(View.GONE);
                            }

                            mSceneBeanList = resultObject.getSceneList();

                            mAdapter = new EditSceneMultipleAdapter(getActivity());
                            mAdapter.addItems(mSceneBeanList);

                            recyclerView.setAdapter(mAdapter);

                            mAdapter.setOnActionModeCallBack(new EditSceneMultipleAdapter.OnActionModeCallBack() {

                                @Override
                                public void showActionMode() {

                                    mAdapter.setIsActionModeShow(true);
                                    mAdapter.notifyDataSetChanged();

                                    ((MainActivity)getActivity()).startSupportActionMode();

                                }
                            });

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

    public EditSceneMultipleAdapter mAdapter;

    private List<NetScene.SceneListBean> mSceneBeanList;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //offLineDialog();
        isBindDialog();
    }

    @OnClick(R.id.btn_add_scene)
    public void OnClickAddScene() {
        startActivity(new Intent(getActivity(), AddSceneActivity.class));
    }

    private void isBindDialog() {

        final AlertDialog exitDialog = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_TRADITIONAL).create();
        exitDialog.setCanceledOnTouchOutside(true);
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.setContentView(R.layout.dlg_bind_device);


        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

        TextView bindDevice = (TextView) window.findViewById(R.id.tv_bind_device);

        bindDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BindDeviceActivity.class));
                exitDialog.dismiss();
            }
        });



    }

    public void offLineDialog(){

        final AlertDialog exitDialog = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_TRADITIONAL).create();
        exitDialog.setCanceledOnTouchOutside(true);
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.setContentView(R.layout.dialog_offline_device);


        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

        TextView tVcontent = (TextView) window.findViewById(R.id.tv_content);

        tVcontent.setText("xxxx设备离线\n请靠近蓝牙网关或WIFI使用");

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
