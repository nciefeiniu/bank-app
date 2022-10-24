package com.example.phonewallet11;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MyActivity extends Activity {
    private Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, gaimi, bthuanying, btkamian, btnxx, btgp,xyk,bt66;
    private Intent intent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
       /* bt1=(Button) findViewById(R.id.button1);
        bt2=(Button) findViewById(R.id.button2);
        bt3=(Button) findViewById(R.id.button3);*/

        bt4 = (Button) findViewById(R.id.button4);
         xyk= (Button) findViewById(R.id.xyk);
        bt5 = (Button) findViewById(R.id.button5);
        bt6 = (Button) findViewById(R.id.button6);
        bt66 = (Button) findViewById(R.id.button66);
        btgp = (Button) findViewById(R.id.buttongp);
        bt7 = (Button) findViewById(R.id.button7);
        // bt8=(Button) findViewById(R.id.button8);
        gaimi = (Button) findViewById(R.id.gaima);
        bthuanying = (Button) findViewById(R.id.huanying);
        btkamian = (Button) findViewById(R.id.kamian);

        btnxx = (Button) findViewById(R.id.btnxx);



        intent = getIntent();
        String id = intent.getStringExtra("id");
        DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
        Cursor c = dbHelper.query(id);
        if (c.getCount() != 0) {
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

        } else {
            Log.v("数据错误！", "");
        }


    /*    String id = intent.getStringExtra("id");
        XiaoxiActivity xiaoxin = new XiaoxiActivity();
        intent = xiaoxin.getxinxi(id);
*/
       /* bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               intent.setClass(MyActivity.this,UserActivity.class);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MyActivity.this,NewActivity.class);
                startActivity(intent);
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MyActivity.this,MyActivity.class);
                startActivity(intent);
            }
        });*/




        //个人信息
        btnxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentqt = new Intent();
                intentqt.setClass(MyActivity.this, XxActivity.class);
                startActivity(intentqt);
            }
        });

        xyk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentqt = new Intent();
                intentqt.setClass(MyActivity.this, XykActivity.class);
                startActivity(intentqt);
            }
        });


        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MyActivity.this, OneselfActivity.class);
                startActivity(intent);
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MyActivity.this, MoneyActivity.class);
                startActivity(intent);
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MyActivity.this, CardActivity.class);
                startActivity(intent);
            }
        });

        bt66.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MyActivity.this, CardxykActivity.class);
                startActivity(intent);
            }
        });
        btkamian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MyActivity.this, Kamianinfo1.class);
                startActivity(intent);
            }
        });
        btgp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(MyActivity.this, GpActivity.class);
                startActivity(intent);
            }
        });
        //账单
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MyActivity.this, ZhangdanActivity.class);
                startActivity(intent);
            }
        });
        bthuanying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MyActivity.this, Bbb2Activity.class);
                startActivity(intent);
            }
        });

        /*bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(MyActivity.this,FriendActivity.class);
                startActivity(intent);
            }
        });*/
        gaimi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                intent.setClass(MyActivity.this, paypwdActivity.class);
                startActivity(intent);
            }
        });
    }
}