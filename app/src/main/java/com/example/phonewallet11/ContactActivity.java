package com.example.phonewallet11;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;



import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {
    protected ListView contactsview;//ContactsView 可从系统电话本中查找、选取联系人的工具
    protected List<String> contactsList = new ArrayList<>();
    protected ArrayAdapter<String> adapter;//ArrayAdapter是一个很简单的适配器，是BaseAdapter的子类。
    //ArrayAdapter绑定的数据是集合或数组，比较单一。视图是列表形式，ListView 或 Spinner.
    protected String number;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initview();//initViews 初始视图（界面控件等）
        contactsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                String string = contactsList.get(position);
                intent.putExtra("phone", string.substring(string.length() - number.length(), string.length()));
                setResult(2, intent);
                finish();
            }
        });
    }

    private void initview() {
        contactsview = findViewById(R.id.contactsview);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
        contactsview.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions//申请权限
                    (
                    ContactActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            readContacts();//使用READ_CONTACTS读取手机联系人
        }
    }

    //调用并获取联系人信息
    @SuppressLint("Range")
    private void readContacts() {
        Cursor cursor = null;
        try {

            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayName + "\n" + number);
                    this.number = number;
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


    }

    //回调方法，无论哪种结果，最终都会回调该方法，之后在判断用户是否授权，
    // 用户同意则调用readContacts（）方法，失败则会弹窗提示失败
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                // Permission Granted 授予权限
                //处理授权之后逻辑
                {
                    readContacts();
                } else {
                    Toast.makeText(this, "获取联系人权限失败", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}