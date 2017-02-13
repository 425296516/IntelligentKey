package com.aigo.router.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.NetScene;
import com.aigo.router.bussiness.bean.ResultObject;
import com.aigo.router.ui.fragment.PersonalCenterFragment;
import com.aigo.router.ui.fragment.SceneFragment;
import com.aigo.router.ui.utils.ToastUtil;
import com.aigo.usermodule.business.UserModule;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SceneFragment mCarInsuranceFragment;
    private PersonalCenterFragment mPersonalCenterFragment;

    @Bind(R.id.tab_layout_bottom)
    TabLayout tabLayoutBottom;
    @Bind(R.id.linear_select_bottom)
    LinearLayout mLinearSelect;
    @Bind(R.id.rl_all_select)
    RelativeLayout rlAllSelect;
    @Bind(R.id.radio_button)
    RadioButton mRadioButton;
    private boolean mSelectStatus;

    private boolean isPageChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mCarInsuranceFragment == null) {
            mCarInsuranceFragment = SceneFragment.newInstance("", "");
        }

        if (mPersonalCenterFragment == null) {
            mPersonalCenterFragment = PersonalCenterFragment.newInstance("", "");
        }

        initTabView();
        onItemSelected(mCarInsuranceFragment);

    }

    public void startSupportActionMode(){
        mLinearSelect.setVisibility(View.VISIBLE);
        startSupportActionMode(mDeleteMode);
    }

    @OnClick(R.id.rl_all_select)
    public void onClickAllSelect(){
        if (!mSelectStatus) {
            mSelectStatus = true;
        } else {
            mSelectStatus = false;
        }
        mRadioButton.setChecked(mSelectStatus);
        mCarInsuranceFragment.mAdapter.setAllSelectRadio(mSelectStatus);
    }


    @OnClick(R.id.button_delete)
    public void onClickDelete() {

        deleteKeyDialog();
    }

    private List<NetScene.SceneListBean> mSceneBeanList;
    private List<String> sceneIdList = new ArrayList<>();


    public void deleteKeyDialog(){

        final AlertDialog exitDialog = new AlertDialog.Builder(this, R.style.Theme_Light_Dialog).create();
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.setContentView(R.layout.dialog_delete_linkman_tip);

        //获得window窗口的属性
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);

        TextView tVcontent = (TextView) window.findViewById(R.id.tv_content);
        window.findViewById(R.id.tv_title).setVisibility(View.INVISIBLE);
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);

        tVcontent.setText("删除场景?\n删除后将不再执行条件");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Integer> selectList = mCarInsuranceFragment.mAdapter.getMultiSelectPositions();

                mSceneBeanList = mCarInsuranceFragment.mAdapter.getDataList();

                for(int i=0;i<selectList.size();i++){
                    sceneIdList.add(mSceneBeanList.get(selectList.get(i)).getSceneId());
                }

                SceneModule.getInstance().deleteScene(sceneIdList)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Action1<ResultObject>() {
                            @Override
                            public void call(ResultObject resultObject) {
                                if (resultObject.getResult().isResult()) {

                                    mCarInsuranceFragment.mAdapter.notifyDataSetChanged();
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


    private ActionMode.Callback mDeleteMode = new ActionMode.Callback() {


        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mCarInsuranceFragment.mAdapter.setIsActionModeShow(false);

            //ToastUtil.showToast(getApplicationContext(),"退出");
           mLinearSelect.setVisibility(View.GONE);
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_trigger_next, menu);
            actionMode.setTitle("场景");
            actionMode.getMenu().getItem(0).setTitle("完成");
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int i = menuItem.getItemId();
            if (i == R.id.action_next) {
                actionMode.finish();
                return true;
            }
            return false;
        }
    };


    private void initTabView() {

        for (int i = 0; i < 2; i++) {
            View tab1 = getLayoutInflater().inflate(R.layout.item_tab_intelligent_bottom, null);
            TextView textView = ((TextView) tab1.findViewById(R.id.text_tab_view));
            ImageView icon = (ImageView) tab1.findViewById(R.id.icon);
            icon.setImageResource(i == 0 ? R.drawable.drw_1_tab_scene_icon_select : R.drawable.drw_1_tab_personal_icon_select);
            textView.setText(i == 0 ? "场景" : "我的");
            tabLayoutBottom.addTab(tabLayoutBottom.newTab().setCustomView(tab1));
        }

        tabLayoutBottom.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(final TabLayout.Tab tab) {

                Log.d(TAG, "onTabSelected=" + tab.getPosition());

                if (tab.getPosition() == 0) {

                    mMenu.getItem(0).setVisible(true);
                    mMenu.getItem(1).setVisible(true);
                    mMenu.getItem(2).setVisible(false);

                    switchFragment(mPersonalCenterFragment, mCarInsuranceFragment);


                    isPageChange = false;
                } else {

                    mMenu.getItem(0).setVisible(false);
                    mMenu.getItem(1).setVisible(false);
                    mMenu.getItem(2).setVisible(true);

                    isPageChange = true;

                    switchFragment(mCarInsuranceFragment, mPersonalCenterFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabUnselected=" + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabReselected=" + tab.getPosition());
                if (tab.getPosition() == 1) {
                    isPageChange = true;

                    mMenu.getItem(0).setVisible(false);
                    mMenu.getItem(1).setVisible(false);
                    mMenu.getItem(2).setVisible(true);

                    tabLayoutBottom.setScrollPosition(1, 0, true);
                    switchFragment(mCarInsuranceFragment, mPersonalCenterFragment);
                }
            }
        });
    }

    public void onItemSelected(Fragment detailFragment) {

        //开始Fragment的事务Transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //替换容器(container)原来的Fragment
        fragmentTransaction.add(R.id.fl_1_home_fragment, detailFragment);

        fragmentTransaction.commit();
    }


    public void switchFragment(Fragment from, Fragment to) {
        if (from == null || to == null)
            return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!to.isAdded()) {
            // 隐藏当前的fragment，add下一个到Activity中
            transaction.hide(from).add(R.id.fl_1_home_fragment, to).commit();
        } else {
            // 隐藏当前的fragment，显示下一个
            transaction.hide(from).show(to).commit();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }

    private Menu mMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scene, menu);

        menu.getItem(2).setVisible(false);
        mMenu = menu;

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.edit_scene_menu) {

            ToastUtil.showToast(getApplicationContext(),"编辑");
            return true;
        }else if (id == R.id.add_scene_menu) {

            startActivity(new Intent(this,AddSceneActivity.class));
            return true;
        }else if (id == R.id.action_unlogin) {

            UserModule.getInstance().logout();
            startActivity(new Intent(this,SplashActivity.class));
            finish();

            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    private int mBackKeyClickedNum;

    @Override
    public void onBackPressed() {
        if (isPageChange) {
            isPageChange = false;
            mMenu.getItem(0).setVisible(true);
            mMenu.getItem(1).setVisible(true);
            mMenu.getItem(2).setVisible(false);
            switchFragment(mPersonalCenterFragment, mCarInsuranceFragment);
            tabLayoutBottom.setScrollPosition(0, 0, true);

        } else {
            mBackKeyClickedNum++;
            if (mBackKeyClickedNum > 1) {
                super.onBackPressed();
                return;
            }

            ToastUtil.showToast(getApplicationContext(), "再点一次退出");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBackKeyClickedNum = 0;
                }
            }, 1000 * 2);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
