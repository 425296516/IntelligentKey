package com.aigo.router.ui.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aigo.router.R;
import com.aigo.router.bussiness.SceneModule;
import com.aigo.router.bussiness.bean.NetLinkman;
import com.aigo.router.bussiness.bean.ResultObject;
import com.aigo.router.ui.utils.ToastUtil;
import com.aigo.usermodule.business.UserModule;

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

    @Bind(R.id.btn_add_contact)
    Button btnAddContact;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private HomeAdapter mHomeAdapter;
    private List<NetLinkman.LinkmanListBean> contactList;
    private boolean isSelectContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isSelectContact = getIntent().getBooleanExtra("SELECT_CONTACT",false);

       // getPermissionToReadUserContacts();

        SceneModule.getInstance().getUserLinkman(UserModule.getInstance().getUser().getUsername())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<NetLinkman>() {
                    @Override
                    public void call(NetLinkman netLinkman) {
                        if (netLinkman.getResult().isResult()) {
                            contactList = netLinkman.getLinkmanList();
                            recyclerView.setLayoutManager(new LinearLayoutManager(ContactActivity.this));
                            mHomeAdapter = new HomeAdapter();
                            recyclerView.setAdapter(mHomeAdapter);
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

    @OnClick(R.id.btn_add_contact)
    public void addContact(View view) {

        Uri uri = Uri.parse("content://contacts/people");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(intent, 0);

    }


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HomeAdapter.MyViewHolder holder = new HomeAdapter.MyViewHolder(LayoutInflater.from(
                    getApplicationContext()).inflate(R.layout.item_contact_recyclerview, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final HomeAdapter.MyViewHolder holder, final int position) {

            holder.tvContactName.setText(contactList.get(position).getLink_name());
            holder.tvContactPhone.setText(contactList.get(position).getLink_no() + "");

            if(isSelectContact) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent mIntent = new Intent();
                        mIntent.putExtra("CONTACT_ID",contactList.get(position).getLink_id());
                        mIntent.putExtra("CONTACT_PHONE", contactList.get(position).getLink_no());
                        mIntent.putExtra("CONTACT_NAME", contactList.get(position).getLink_name());
                        // 设置结果，并进行传送
                        setResult(2, mIntent);
                        finish();

                    }
                });
            }

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    final AlertDialog exitDialog = new AlertDialog.Builder(ContactActivity.this).create();
                    exitDialog.setCancelable(true);
                    exitDialog.show();
                    Window window = exitDialog.getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    window.setContentView(R.layout.dialog_delete_device);

                    TextView llDeleteDevice = (TextView) window.findViewById(R.id.tv_delete);
                    TextView llUpdateDevice = (TextView) window.findViewById(R.id.tv_update_remark);

                    llDeleteDevice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            exitDialog.dismiss();

                            SceneModule.getInstance().deleteUserLinkman(contactList.get(position).getLink_id())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.newThread())
                                    .subscribe(new Action1<ResultObject>() {
                                        @Override
                                        public void call(ResultObject resultObject) {
                                            if (resultObject.getResult().isResult()) {
                                                contactList.remove(position);
                                                recyclerView.getAdapter().notifyDataSetChanged();
                                                ToastUtil.showToast(getApplicationContext(),"删除成功");
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

            return contactList.size();
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


    public void setRemarkName(final int position) {
        // 隐藏输入法
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        final AlertDialog exitDialog = new AlertDialog.Builder(this).create();
        exitDialog.setCancelable(true);
        exitDialog.show();
        Window window = exitDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        window.setContentView(R.layout.dialog_update_device);
        final EditText mRemark = (EditText) window.findViewById(R.id.et_input_remark);
        Button okBtn = (Button) window.findViewById(R.id.btn_ok);
        Button cancelBtn = (Button) window.findViewById(R.id.btn_cancel);

        mRemark.setText(contactList.get(position).getLink_name());

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

                SceneModule.getInstance().modifyUserLinkman(contactList.get(position).getLink_id(),
                        contactList.get(position).getLink_no(),mRemark.getText().toString())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Action1<ResultObject>() {
                            @Override
                            public void call(ResultObject resultObject) {
                                if (resultObject.getResult().isResult()) {
                                    contactList.get(position).setLink_name(mRemark.getText().toString());
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                    ToastUtil.showToast(getApplicationContext(),"修改成功");
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

                NetLinkman.LinkmanListBean contact = new NetLinkman.LinkmanListBean();
                contact.setLink_name(contacts[0]);
                contact.setLink_no(contacts[1]);
                contactList.add(contact);

                mHomeAdapter.notifyDataSetChanged();

                addUserLinkMan(contacts[1], contacts[0]);

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addUserLinkMan(String linkNo,String linkName){

        SceneModule.getInstance().addUserLinkman(linkNo, linkName,
                UserModule.getInstance().getUser().getUsername())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<ResultObject>() {
                    @Override
                    public void call(ResultObject resultObject) {
                        if (resultObject.getResult().isResult()) {
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

        String name  = intent.getStringExtra("CONTACT_NAME");
        String phone = intent.getStringExtra("CONTACT_PHONE");
        NetLinkman.LinkmanListBean contact = new NetLinkman.LinkmanListBean();
        contact.setLink_name(name);
        contact.setLink_no(phone);
        contactList.add(contact);

        mHomeAdapter.notifyDataSetChanged();

        addUserLinkMan(phone,name);

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
        } else if (id == R.id.add_linkman_menu) {

            startActivity(new Intent(this, AddContactActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
