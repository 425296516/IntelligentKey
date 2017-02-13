package com.aigo.router.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.aigo.router.ui.utils.ToastUtil;
import com.aigo.router.ui.view.DividerItemDecoration;
import com.aigo.router.ui.view.UISwitchButton;
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
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));

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

        offLineDialog();
    }

    @OnClick(R.id.btn_add_scene)
    public void OnClickAddScene() {
        startActivity(new Intent(getActivity(), AddSceneActivity.class));
    }

    private void isBindDialog() {
        final AlertDialog exitDialog = new AlertDialog.Builder(getActivity()).create();
        exitDialog.show();
        final Window window = exitDialog.getWindow();

        final View view = getActivity().getLayoutInflater().inflate(R.layout.dlg_bind_device, null);

        //获取view的宽度;注意view的宽度不能直接get获取需要通过不同的方式获取,http://blog.csdn.net/eclipsexys/article/details/39641115
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int width = view.getMeasuredWidth();

        window.setContentView(view);

        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为包裹内容
        lp.width = width * 3;
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

    private List<String> sceneIdList = new ArrayList<>();

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    getActivity()).inflate(R.layout.item_scene_recyclerview, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.tvSceneDescription.setText(mSceneBeanList.get(position).getTrigger_describe() + "");

            holder.btnSceneStatus.setChecked(mSceneBeanList.get(position).getIsActivate().equals("1") ? true : false);

            holder.btnSceneStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    holder.btnSceneStatus.setChecked(b);

                    SceneModule.getInstance().updateSceneStatus(mSceneBeanList.get(position).getSceneId(), b == true ? 1 : 0)
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
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AddSceneActivity.class);
                    intent.putExtra("SceneBean", mSceneBeanList.get(position));
                    startActivity(intent);
                }
            });


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final AlertDialog exitDialog = new AlertDialog.Builder(getActivity()).create();
                    exitDialog.setCancelable(true);
                    exitDialog.show();
                    Window window = exitDialog.getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    window.setContentView(R.layout.dialog_delete_device);

                    TextView llDeleteDevice = (TextView) window.findViewById(R.id.tv_delete);
                    TextView llUpdateDevice = (TextView) window.findViewById(R.id.tv_update_remark);

                    sceneIdList.add(mSceneBeanList.get(position).getSceneId());

                    llDeleteDevice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exitDialog.dismiss();
                            SceneModule.getInstance().deleteScene(sceneIdList)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(new Action1<ResultObject>() {
                                        @Override
                                        public void call(ResultObject resultObject) {
                                            if (resultObject.getResult().isResult()) {
                                                mSceneBeanList.remove(position);
                                                recyclerView.getAdapter().notifyDataSetChanged();
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
                    });

                    llUpdateDevice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exitDialog.dismiss();
                            setRemarkName(position);

                        }
                    });

                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {

            return mSceneBeanList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.iv_scene_icon)
            ImageView ivSceneIcon;
            @Bind(R.id.tv_scene_description)
            TextView tvSceneDescription;
            @Bind(R.id.btn_scene_status)
            UISwitchButton btnSceneStatus;

            public MyViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }

    public void setRemarkName(final int position) {

        // 隐藏输入法
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        final AlertDialog exitDialog = new AlertDialog.Builder(getActivity()).create();
        exitDialog.setCancelable(true);
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        window.setContentView(R.layout.dialog_update_device);
        final EditText mRemark = (EditText) window.findViewById(R.id.et_input_remark);
        Button okBtn = (Button) window.findViewById(R.id.btn_ok);
        Button cancelBtn = (Button) window.findViewById(R.id.btn_cancel);

        mRemark.setText(mSceneBeanList.get(position).getTrigger_describe());

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();

                SceneModule.getInstance().modifyScene(mSceneBeanList.get(position).getSceneId(),
                        mSceneBeanList.get(position).getTrigger_deviceSN(), mSceneBeanList.get(position).getExecute_deviceSN(),
                        mSceneBeanList.get(position).getTriggerId(), mSceneBeanList.get(position).getExecuteId(),
                        mRemark.getText().toString(), mSceneBeanList.get(position).getExecute_describe(),
                        mSceneBeanList.get(position).getLink_id(), mSceneBeanList.get(position).getMessage(),
                        mSceneBeanList.get(position).getIsActivate(), mSceneBeanList.get(position).getIsPush())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Action1<ResultObject>() {
                            @Override
                            public void call(ResultObject resultObject) {
                                if (resultObject.getResult().isResult()) {
                                    mSceneBeanList.get(position).setTrigger_describe(mRemark.getText().toString());
                                    recyclerView.getAdapter().notifyItemChanged(position);
                                    ToastUtil.showToast(getActivity(), "修改成功");
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
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
