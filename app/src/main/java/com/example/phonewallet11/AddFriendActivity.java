package com.example.phonewallet11;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



public class AddFriendActivity extends AppCompatActivity {
    private Intent intent ;
    private EditText etNam,etPhon,etBeizh;
    private  String sfz;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriend);
        etNam= (EditText) findViewById(R.id.etName);
        etPhon= (EditText) findViewById(R.id.etPhone);
        etBeizh= (EditText) findViewById(R.id.etBeizhu);
        //获取组件对象

    }




    public void save(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("确认保存记录吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

                final DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
                Cursor c = dbHelper.queryf(etNam.getText().toString(),etPhon.getText().toString());
                if (c.getCount() !=0) {
                    c.moveToFirst();
                    if (c.getString(0).equals(sfz)){
                        etNam.setText("");
                        etPhon.setText("");
                        etBeizh.setText("");
                        Toast.makeText(AddFriendActivity.this, "不能添加自已为好友！", Toast.LENGTH_SHORT).show();
                    }else {
                        final DatebaseHelper dbHelper1 = new DatebaseHelper(getApplicationContext());
                        Cursor c1 = dbHelper1.queryConcat(sfz,c.getString(0));
                        c1.moveToFirst();
                        if (c1.getCount()!=0) {
                            etNam.setText("");
                            etPhon.setText("");
                            etBeizh.setText("");
                            Toast.makeText(AddFriendActivity.this, "该好友已存在！", Toast.LENGTH_SHORT).show();
                        }else {

                            //向数据库中添加联系人并实现界面的跳转
                            final ContentValues values = new ContentValues();
                            values.put("_id", c.getString(0));
                            values.put("f_id", sfz);
                            values.put("f_name", c.getString(2));
                            values.put("f_phone", c.getString(4));
                            values.put("f_group", etBeizh.getText().toString());

                            final DatebaseHelper dbHelper2 = new DatebaseHelper(getApplicationContext());
                            int i = dbHelper2.insertFriend(values);
                            Toast.makeText(AddFriendActivity.this, "好友添加成功！", Toast.LENGTH_SHORT).show();
                            setResult(1, intent);
                            finish();

                        }
                    }

                }else {
                    Toast.makeText(AddFriendActivity.this, "不存在此用户！", Toast.LENGTH_SHORT).show();
                    etNam.setText("");
                    etPhon.setText("");
                    etBeizh.setText("");

                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        //将对话框显示出来
        builder.create().show();

    }
}
