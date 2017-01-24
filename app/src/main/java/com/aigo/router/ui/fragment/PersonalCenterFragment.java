package com.aigo.router.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aigo.router.R;
import com.aigo.router.ui.activity.ContactActivity;
import com.aigo.router.ui.activity.DeviceManagerActivity;
import com.aigo.router.ui.activity.ExecuteRecordsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PersonalCenterFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.rl_1_contact)
    RelativeLayout rl1Contact;
    @Bind(R.id.rl_1_execute_records)
    RelativeLayout rl1ExecuteRecords;
    @Bind(R.id.rl_1_device_manage)
    RelativeLayout rl1DeviceManage;

    private String mParam1;
    private String mParam2;

    public static PersonalCenterFragment newInstance(String param1, String param2) {
        PersonalCenterFragment fragment = new PersonalCenterFragment();
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

        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.rl_1_contact)
    public void onClickContact(View view) {
        startActivity(new Intent(getActivity(), ContactActivity.class));

       /* Intent intent001 = new Intent();
        intent001.setClassName("com.android.contacts","com.android.contacts.activities.PeopleActivity");
        startActivity(intent001);*/

       /* Uri uri = Uri.parse("content://contacts/people");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(intent, 0);*/

    }

    @OnClick(R.id.rl_1_execute_records)
    public void onClickExecuteRecord(View view) {
        startActivity(new Intent(getActivity(), ExecuteRecordsActivity.class));
    }


    @OnClick(R.id.rl_1_device_manage)
    public void onClickDeviceManage(View view) {
        startActivity(new Intent(getActivity(), DeviceManagerActivity.class));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
