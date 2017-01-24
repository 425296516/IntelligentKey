package com.aigo.router.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.ui.fragment.PersonalCenterFragment;
import com.aigo.router.ui.fragment.SceneFragment;
import com.aigo.router.ui.utils.ToastUtil;
import com.aigo.usermodule.business.UserModule;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SceneFragment mCarInsuranceFragment;
    private PersonalCenterFragment mPersonalCenterFragment;

    @Bind(R.id.tab_layout_bottom)
    TabLayout tabLayoutBottom;
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
