package com.example.phonewallet11;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.Toast;

public class Caidan extends TabActivity {
    private TabHost tabhost;
    private Intent intent;
    String sc,sccc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caidan);

        intent = getIntent();
        String idss = intent.getStringExtra("id");
//        DatebaseHelper dbHelper1=new DatebaseHelper(getApplicationContext());
//        Cursor c1=dbHelper1.query(idss);
//        if(c1.getCount()!=0) {
//            c1.moveToFirst();
//            intent.putExtra("id", c1.getString(0));
//            intent.putExtra("account", c1.getString(1));
//            intent.putExtra("name", c1.getString(2));
//            intent.putExtra("sex", c1.getString(3));
//            intent.putExtra("phone", c1.getString(4));
//            intent.putExtra("qx", c1.getString(5));
//            intent.putExtra("login_password", c1.getString(6));
//            intent.putExtra("pay_password", c1.getString(7));
//            // String moneys=String.valueOf(c.getFloat(8));
//            intent.putExtra("moneys", c1.getFloat(8));
//
//        }else {
//            Log.v("数据错误！","");
//        }

        //选项卡

        tabhost=getTabHost();
        Intent intent = getIntent();
        String id=intent.getStringExtra("id");
        String name=intent.getStringExtra("name");
        String pay_password=intent.getStringExtra("pay_password");

        Intent shouyeIntent=new Intent(this,UserActivity.class);
        shouyeIntent.putExtra("id", id);
        shouyeIntent.putExtra("name", name);
        shouyeIntent.putExtra("pay_password", pay_password);
        tabhost.addTab(tabhost.newTabSpec("tabShouye").setIndicator("首页").setContent(shouyeIntent));

        Intent zixunIntent=new Intent(this,ShoujiActivity.class);
        zixunIntent.putExtra("id", id);
        zixunIntent.putExtra("name", name);
        zixunIntent.putExtra("pay_password", pay_password);
        tabhost.addTab(tabhost.newTabSpec("tabShenghuo").setIndicator("生活").setContent(zixunIntent));


        Intent shoucangIntent=new Intent(this,FriendActivity.class);
        shoucangIntent.putExtra("id", id);
        shoucangIntent.putExtra("name", name);
        shoucangIntent.putExtra("pay_password", pay_password);
        tabhost.addTab(tabhost.newTabSpec("tabFriend").setIndicator("好友").setContent(shoucangIntent));





//        DatebaseHelper dbHelper=new DatebaseHelper(getApplicationContext());
        //Log.v("取到的值为",id);
//        Cursor c=dbHelper.query(id);
        Intent loginIntent=new Intent(this,MyActivity.class);
        loginIntent.putExtra("id", id);
        loginIntent.putExtra("name", name);
        loginIntent.putExtra("pay_password", pay_password);
        tabhost.addTab(tabhost.newTabSpec("tabLogin").setIndicator("我的").setContent(loginIntent));

//        if(c.getCount()!=0){
//            c.moveToFirst();
//
//            if(!"".equals(pay_password)){
//                //intent.setClass(UserActivity.this,MyActivity.class);
//                Intent loginIntent=new Intent(this,MyActivity.class);
//                loginIntent.putExtra("id", id);
//                loginIntent.putExtra("name", name);
//                loginIntent.putExtra("pay_password", pay_password);
//                tabhost.addTab(tabhost.newTabSpec("tabLogin").setIndicator("我的").setContent(loginIntent));
//            }else {
//                // intent.setClass(UserActivity.this, paypwdActivity.class);
//                Intent loginIntent=new Intent(this,paypwdActivity.class);
//                loginIntent.putExtra("id", id);
//                loginIntent.putExtra("name", name);
//                loginIntent.putExtra("pay_password", pay_password);
//                tabhost.addTab(tabhost.newTabSpec("tabLogin").setIndicator("我的").setContent(loginIntent));
//
//            }
//            //  startActivity(intent);
//
//        }else {
//            Toast.makeText(Caidan.this, "数据错误！", Toast.LENGTH_SHORT).show();
//
//        }





        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabid) {
                // TODO Auto-generated method stub
                //Toast.makeText(MainActivity.this, "当前选中："+tabid+"标签", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
