package com.aigo.router.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.bean.NetLinkman;
import com.aigo.router.ui.activity.EditContactActivity;

/**
 * Author:    ZhuWenWu
 * Version    V1.0
 * Date:      2015/8/26  13:21.
 * Description: 学校选择适配器
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2015/8/26        ZhuWenWu            1.0                    1.0
 * Why & What is modified:
 */
public class EditContactMultipleAdapter<T> extends BaseMultiSelectAdapter<T> {
    private static final String TAG = EditContactMultipleAdapter.class.getSimpleName();
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

    public EditContactMultipleAdapter(Context context) {

        super(context);
        mContext = context;
    }

    public NetLinkman.LinkmanListBean getItem(int position) {
        return (NetLinkman.LinkmanListBean) getItemData(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MultiSettingSelectViewHolder(mLayoutInflater.inflate(R.layout.item_contact_recyclerview, parent, false), this, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MultiSettingSelectViewHolder) {
            MultiSettingSelectViewHolder multiSettingSelectViewHolder = ((MultiSettingSelectViewHolder) holder);
            multiSettingSelectViewHolder.bindViewData(getItem(position), position);
        }

    }


    static class MultiSettingSelectViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        EditContactMultipleAdapter mAdapter;
        RadioButton radioButton;
        TextView contactName, contactPhone;
        Context mContext;

        MultiSettingSelectViewHolder(View view, EditContactMultipleAdapter adapter, Context context) {
            super(view);

            mAdapter = adapter;
            mContext = context;

            relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout);
            radioButton = (RadioButton) view.findViewById(R.id.checkBox);
            contactName = (TextView) view.findViewById(R.id.tv_contact_name);
            contactPhone = (TextView) view.findViewById(R.id.tv_contact_phone);

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

        private NetLinkman.LinkmanListBean mLinkmanListBean;

        public void bindViewData(final NetLinkman.LinkmanListBean contact, int position) {

            mLinkmanListBean = contact;
            if (mAdapter.isSelectedEnable && mAdapter.isActionModeShow) {
                radioButton.setVisibility(View.VISIBLE);
            } else {
                radioButton.setVisibility(View.GONE);
            }

            radioButton.setChecked(mAdapter.isSelected(position));
            contactName.setText(contact.getLink_name());
            contactPhone.setText(contact.getLink_no() + "");

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
                Intent intent = new Intent(mContext, EditContactActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("CONTACT_ID",mLinkmanListBean.getLink_id());
                intent.putExtra("CONTACT_NAME",mLinkmanListBean.getLink_name());
                intent.putExtra("CONTACT_PHONE",mLinkmanListBean.getLink_no());

                mContext.startActivity(intent);
            }
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
