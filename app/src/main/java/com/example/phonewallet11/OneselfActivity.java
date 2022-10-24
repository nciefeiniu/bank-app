package com.example.phonewallet11;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OneselfActivity extends Activity {
    private TextView name,account,phone,sex,id;

    private Button xiugai;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneself);
        intent  = getIntent();
        name    = (TextView) findViewById(R.id.name);
        account = (TextView) findViewById(R.id.account);
        phone   = (TextView) findViewById(R.id.phone);
        sex     = (TextView) findViewById(R.id.sex);
        id      = (TextView) findViewById(R.id.id);
        xiugai  = (Button) findViewById(R.id.xiugai);


        String idS = intent.getStringExtra("id");
        DatebaseHelper dbHelper=new DatebaseHelper(getApplicationContext());
        Cursor c=dbHelper.query(idS);
        if(c.getCount()!=0) {
            c.moveToFirst();
            intent.putExtra("id", c.getString(0));
            intent.putExtra("account", c.getString(1));
            intent.putExtra("name", c.getString(2));
            intent.putExtra("sex", c.getString(3));
            intent.putExtra("phone", c.getString(4));
            intent.putExtra("qx", c.getString(5));
            intent.putExtra("login_password", c.getString(6));
            intent.putExtra("pay_password", c.getString(7));
            // String moneys=String.valueOf(c.getFloat(8));
            intent.putExtra("moneys", c.getFloat(8));

        }else {
            Log.v("数据错误！","");
        }
        name.setText(intent.getStringExtra("name"));
        account.setText(intent.getStringExtra("account"));
        phone.setText(intent.getStringExtra("phone"));
        sex.setText(intent.getStringExtra("sex"));
        id.setText(intent.getStringExtra("id"));

        xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(OneselfActivity.this,UpuserActivity.class);
                startActivity(intent);
            }
        });

    }
}
