package com.aigo.router.ui.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.NetLinkman;
import com.aigo.router.ui.adapter.EditContactMultipleAdapter;
import com.aigo.router.ui.utils.ToastUtil;
import com.aigo.router.ui.view.DividerItemDecoration;
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

public class ContactActivity extends AppCompatActivity {

    private static final String TAG = ContactActivity.class.getSimpleName();

    @Bind(R.id.rl_add_contact)
    RelativeLayout RlAddContact;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.linear_select_bottom)
    LinearLayout mLinearSelect;
    @Bind(R.id.radio_button)
    RadioButton mRadioButton;
    @Bind(R.id.rl_all_select)
    RelativeLayout rlAllSelect;

    //private HomeAdapter mHomeAdapter;
    private List<NetLinkman.LinkmanListBean> contactList;
    private boolean isSelectContact;
    private EditContactMultipleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isSelectContact = getIntent().getBooleanExtra("SELECT_CONTACT", false);

        SceneModule.getInstance().getUserLinkman(UserModule.getInstance().getUser().getUsername())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetLinkman>() {
                    @Override
                    public void call(NetLinkman netLinkman) {
                        if (netLinkman.getResult().isResult()) {
                            contactList = netLinkman.getLinkmanList();
                            recyclerView.setLayoutManager(new LinearLayoutManager(ContactActivity.this));
                            recyclerView.addItemDecoration(new DividerItemDecoration(ContactActivity.this, DividerItemDecoration.VERTICAL_LIST));

                            mAdapter = new EditContactMultipleAdapter(ContactActivity.this);
                            mAdapter.addItems(contactList);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.setOnActionModeCallBack(new EditContactMultipleAdapter.OnActionModeCallBack() {
                                @Override
                                public void showActionMode() {

                                    mAdapter.setIsActionModeShow(true);
                                    mAdapter.notifyDataSetChanged();

                                    RlAddContact.setVisibility(View.GONE);
                                    mLinearSelect.setVisibility(View.VISIBLE);
                                    startSupportActionMode(mDeleteMode);
                                }
                            });
                        }
                        Log.d(TAG, "MainActivity:test:getNetState:integer:" + netLinkman);
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

    @OnClick(R.id.rl_all_select)
    public void onClickAllSelect(){
        if (!mSelectStatus) {
            mSelectStatus = true;
        } else {
            mSelectStatus = false;
        }
        mRadioButton.setChecked(mSelectStatus);
        mAdapter.setAllSelectRadio(mSelectStatus);
    }


    @OnClick(R.id.button_delete)
    public void onClickDelete() {

        deleteLinkManDialog();
    }

    private void deleteLinkManDialog() {
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
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();

                final List<Integer> selectList = mAdapter.getMultiSelectPositions();
                List<String> linkIdList = new ArrayList<String>();
                for (int i = 0; i < selectList.size(); i++) {
                    linkIdList.add(contactList.get(selectList.get(i)).getLink_id());
                }

                SceneModule.getInstance().deleteUserLinkman(linkIdList)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Action1<NetLinkman>() {
                            @Override
                            public void call(NetLinkman resultObject) {
                                if (resultObject.getResult().isResult()) {
                                    contactList = resultObject.getLinkmanList();
                                    mAdapter.clearItems();
                                    mAdapter.addItems(resultObject.getLinkmanList());

                                    ToastUtil.showToast(getApplicationContext(), "删除成功");
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
                                recyclerView.getAdapter().notifyDataSetChanged();
                                Log.d(TAG, "MainActivity:test:getNetState:ok");
                            }
                        });

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });
    }


    private boolean mSelectStatus;

    private ActionMode.Callback mDeleteMode = new ActionMode.Callback() {


        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            mAdapter.setIsActionModeShow(false);

            RlAddContact.setVisibility(View.VISIBLE);
            mLinearSelect.setVisibility(View.GONE);
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getMenuInflater().inflate(R.menu.menu_trigger_next, menu);
            actionMode.setTitle("联系人");
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

    @OnClick(R.id.rl_add_contact)
    public void addContact(View view) {

        Uri uri = Uri.parse("content://contacts/people");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(intent, 0);

    }


 /*   private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    public void getPermissionToReadUserContacts() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
            }

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data == null) {
                    return;
                }
                //处理返回的data,获取选择的联系人信息
                Uri uri = data.getData();
                String[] contacts = getPhoneContacts(uri);

                addUserLinkMan(contacts[1], contacts[0]);

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addUserLinkMan(String linkNo, String linkName) {

        SceneModule.getInstance().addUserLinkman(linkNo, linkName,
                UserModule.getInstance().getUser().getUsername())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetLinkman>() {
                    @Override
                    public void call(NetLinkman resultObject) {
                        if (resultObject.getResult().isResult()) {
                            contactList = resultObject.getLinkmanList();
                            mAdapter.clearItems();

                            mAdapter.addItems(resultObject.getLinkmanList());

                            mAdapter.notifyDataSetChanged();
                            ToastUtil.showToast(getApplicationContext(), "添加成功");
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String contactId = intent.getStringExtra("CONTACT_ID");
        if(TextUtils.isEmpty(contactId)){
            String name = intent.getStringExtra("CONTACT_NAME");
            String phone = intent.getStringExtra("CONTACT_PHONE");
            NetLinkman.LinkmanListBean contact = new NetLinkman.LinkmanListBean();
            contact.setLink_name(name);
            contact.setLink_no(phone);
            contactList.add(contact);

            mAdapter.addItem(contact);
            mAdapter.notifyDataSetChanged();

            addUserLinkMan(phone, name);
        }else{
            String name = intent.getStringExtra("CONTACT_NAME");
            String phone = intent.getStringExtra("CONTACT_PHONE");

            for(int i=0;i<contactList.size();i++){
                if(contactList.get(i).getLink_id().equals(contactId)){
                    contactList.get(i).setLink_name(name);
                    contactList.get(i).setLink_no(phone);
                    mAdapter.notifyItemChanged(i);
                }
            }
        }
    }

    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_linkman, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.edit_linkman_menu) {

            mAdapter.setIsActionModeShow(true);
            mAdapter.notifyDataSetChanged();

            RlAddContact.setVisibility(View.GONE);
            mLinearSelect.setVisibility(View.VISIBLE);
            startSupportActionMode(mDeleteMode);

        } else if (id == R.id.add_linkman_menu) {

            startActivity(new Intent(this, AddContactActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
