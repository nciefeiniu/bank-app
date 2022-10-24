package com.example.phonewallet11;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MoneyActivity extends Activity {
    private Button bt1,bt2,bt3;
    private TextView tv2,tv4;
    private Intent intent;
    private  String id;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        bt1=(Button) findViewById(R.id.button1);
        bt2=(Button) findViewById(R.id.button2);
        bt3=(Button) findViewById(R.id.button3);
        tv2=(TextView) findViewById(R.id.textView2);
        tv4=(TextView) findViewById(R.id.textView4);


        intent =getIntent();

        String id = intent.getStringExtra("id");
        DatebaseHelper dbHelper=new DatebaseHelper(getApplicationContext());
        Cursor c=dbHelper.query(id);
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
        /*String id = intent.getStringExtra("id");
        XiaoxiActivity xiaoxin = new XiaoxiActivity();
        intent = xiaoxin.getxinxi(id);*/


        tv2.setText(intent.getStringExtra("name"));
        Float moneys= intent.getFloatExtra("moneys",0);
        tv4.setText(moneys+"");


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.setClass(MoneyActivity.this,AddMoneyActivity.class);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.setClass(MoneyActivity.this,CutMoneyActivity.class);
                startActivity(intent);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.setClass(MoneyActivity.this,Caidan.class);
                startActivity(intent);
            }
        });
    }
}
