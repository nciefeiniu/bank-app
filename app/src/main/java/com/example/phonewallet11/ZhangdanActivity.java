package com.example.phonewallet11;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.appcompat.app.AppCompatActivity;

public class ZhangdanActivity extends AppCompatActivity {
    private Intent intent;
    private ListView list;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhangdan);

        intent = getIntent();
        id = intent.getStringExtra("id");

        list = (ListView) findViewById(R.id.listView);
        getRelationFromDB();


    }

    private void getRelationFromDB() {
        // TODO Auto-generated method stub
        final DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
        Cursor c = dbHelper.queryrecord(id);
        //将查询到的联系人信息按照list.xml布局方式显示到ListView中
        String[] from = new String[]{"r_name","r_mode","r_money","r_time"};
        int[] to = new int[]{R.id.name, R.id.mode,R.id.money,R.id.r_time};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_list, c, from, to);
        list.setAdapter(adapter);


        dbHelper.close();


    }
    public void add(View view){
        //通过Intent对象实现带返回结果的界面跳转
        intent.setClass(getApplicationContext(), ZhangdanActivity.class);
        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //调用getRelationFromDB()方法
        if (requestCode == resultCode) {
            getRelationFromDB();
        }
    }
}
