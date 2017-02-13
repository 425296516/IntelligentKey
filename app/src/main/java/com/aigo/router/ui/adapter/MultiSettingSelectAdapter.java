package com.aigo.router.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.NetBindDeviceList;
import com.aigo.router.bussiness.bean.ResultObject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Author:    ZhuWenWu
 * Version    V1.0
 * Date:      2015/8/15  17:33.
 * Description: 多选适配器
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2015/8/15        ZhuWenWu            1.0                    1.0
 * Why & What is modified:
 */
public class MultiSettingSelectAdapter<T> extends BaseMultiSelectAdapter<T> {
    private static final String TAG = MultiSettingSelectAdapter.class.getSimpleName();
    private OnActionModeCallBack onActionModeCallBack;
    private boolean isActionModeShow = false;
    public Context mContext;

    public void setOnActionModeCallBack(OnActionModeCallBack onActionModeCallBack) {
        this.onActionModeCallBack = onActionModeCallBack;
    }

    public void setIsActionModeShow(boolean isActionModeShow) {
        this.isActionModeShow = isActionModeShow;
        if (!isActionModeShow) {
            clearAllSelect();
        }
    }

    public void setAllSelectRadio(boolean status) {
        if (status) {
            selectAllPositions();
        } else {
            clearAllSelect();
        }
    }

    public MultiSettingSelectAdapter(Context context) {

        super(context);
        mContext = context;
    }


    public NetBindDeviceList.DeviceListBean getItem(int position) {
        return (NetBindDeviceList.DeviceListBean) getItemData(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MultiSettingSelectViewHolder(mLayoutInflater.inflate(R.layout.item_contact_recyclerview, parent, false), this, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder=" + position);

        if (holder instanceof MultiSettingSelectViewHolder) {

            MultiSettingSelectViewHolder multiSettingSelectViewHolder = ((MultiSettingSelectViewHolder) holder);

            multiSettingSelectViewHolder.bindViewData(getItem(position), position);
        }

    }

    static class MultiSettingSelectViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        MultiSettingSelectAdapter mAdapter;
        RadioButton radioButton;
        TextView contactName;
        private Context mContext;

        MultiSettingSelectViewHolder(View view, MultiSettingSelectAdapter adapter, Context context) {
            super(view);

            mAdapter = adapter;
            mContext = context;

            relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout);
            radioButton = (RadioButton) view.findViewById(R.id.checkBox);

            contactName = (TextView) view.findViewById(R.id.tv_contact_name);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelected();
                }
            });

            relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    onLongSelected();

                    return false;
                }
            });

        }

        private NetBindDeviceList.DeviceListBean mDeviceListBean;
        private int mCurrentPosition;

        public void bindViewData(final NetBindDeviceList.DeviceListBean deviceListBean, int position) {
            mCurrentPosition = position;
            mDeviceListBean = deviceListBean;
            if (mAdapter.isSelectedEnable && mAdapter.isActionModeShow) {
                radioButton.setVisibility(View.VISIBLE);
            } else {
                radioButton.setVisibility(View.GONE);
            }

            radioButton.setChecked(mAdapter.isSelected(position));
            contactName.setText(deviceListBean.getNotes_name());
        }


        public void onSelected() {
            if (mAdapter.isSelectedEnable && mAdapter.isActionModeShow) {
                if (mAdapter.isSelected(getPosition())) {//已选中

                    mAdapter.removeSelectPosition(getPosition());
                } else {//未选中

                    mAdapter.addSelectPosition(getPosition());
                }
                mAdapter.notifyItemChanged(getPosition());
            } else {

                showPopupWindow();
            }
        }

        private void showPopupWindow() {

            // 一个自定义的布局，作为显示的内容
            View contentView = LayoutInflater.from(mContext).inflate(
                    R.layout.dialog_edit_delete_select, null);
            // 设置按钮的点击事件
            LinearLayout button = (LinearLayout) contentView.findViewById(R.id.ll_content_1);

            final PopupWindow popupWindow = new PopupWindow(contentView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

            contentView.findViewById(R.id.ll_content_1).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    setRemarkName();

                    popupWindow.dismiss();
                }
            });

            contentView.findViewById(R.id.ll_content_2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onLongSelected();

                    popupWindow.dismiss();
                }
            });

            // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
            // 我觉得这里是API的一个bug
            //popupWindow.setAnimationStyle(R.style.dialog_shop_map_style);
            popupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
                    R.drawable.drawable_11000000));

            popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.BOTTOM, 0, 0);

        }

        public void setRemarkName() {

            // 隐藏输入法
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            // 显示或者隐藏输入法
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            final AlertDialog exitDialog = new AlertDialog.Builder(mContext, R.style.Theme_Light_Dialog).create();
            exitDialog.setCancelable(true);
            exitDialog.show();
            Window window = exitDialog.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            window.setContentView(R.layout.dialog_update_device);
            final EditText mRemark = (EditText) window.findViewById(R.id.et_input_remark);
            Button okBtn = (Button) window.findViewById(R.id.btn_ok);
            Button cancelBtn = (Button) window.findViewById(R.id.btn_cancel);

            mRemark.setText(mDeviceListBean.getNotes_name());

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

                    SceneModule.getInstance().modifyRemark(mDeviceListBean.getDeviceSN(), mRemark.getText().toString())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.newThread())
                            .subscribe(new Action1<ResultObject>() {
                                @Override
                                public void call(ResultObject resultObject) {
                                    if (resultObject.getResult().isResult()) {
                                        ((NetBindDeviceList.DeviceListBean)mAdapter.getDataList().get(mCurrentPosition)).setNotes_name(mRemark.getText().toString());
                                        mAdapter.notifyDataSetChanged();
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


        public boolean onLongSelected() {
            if (mAdapter.isActionModeShow) {//已显示选择模式
                onSelected();
            } else {
                if (mAdapter.onActionModeCallBack != null) {

                    mAdapter.onActionModeCallBack.showActionMode();
                }
            }
            return true;
        }
    }

    public interface OnActionModeCallBack {
        void showActionMode();
    }
}
