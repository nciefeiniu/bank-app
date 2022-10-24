package com.example.phonewallet11;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class FriendActivity extends AppCompatActivity {
    private TextView tv1;
    private ListView list;
    private Intent intent ;
    private  String sfz;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        intent=getIntent();
        sfz=intent.getStringExtra("id");
        tv1 = (TextView) findViewById(R.id.textView2);
        tv1.setText(intent.getStringExtra("name"));
        list = (ListView) findViewById(R.id.listView1);

        //获取布局中的所有按钮控件
        DatebaseHelper dbHelper=new DatebaseHelper(getApplicationContext());
        Cursor c=dbHelper.query(sfz);





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
        getRelationFromDB();


    }


    private void getRelationFromDB() {
        // TODO Auto-generated method stub
        final DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
        Cursor c = dbHelper.queryConcat(sfz);
        c.moveToFirst();


        //将查询到的联系人信息按照relationlist.xml布局方式显示到ListView中

        String[] from1 = new String[]{"f_name","f_phone","f_group"};
        int[] to1 = new int[]{R.id.tvName, R.id.tvPhone, R.id.tvgroup};
        SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(this, R.layout.friendlist, c, from1, to1);
        list.setAdapter(adapter1);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final long temp = l;
                //利用对话框实现删除记录的提示操作
                AlertDialog.Builder builder = new AlertDialog.Builder(FriendActivity.this);
                builder.setMessage("确认要删除好友吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dbHelper.delf((int)temp,sfz);

                        Cursor c = dbHelper.queryConcat(sfz);
                        c.moveToFirst();
                        String[] from1 = new String[]{"f_name","f_phone","f_group"};
                        int[] to1 = new int[]{R.id.tvName, R.id.tvPhone, R.id.tvgroup};
                        SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(getApplicationContext(), R.layout.friendlist, c, from1, to1);
                        list.setAdapter(adapter1);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                return true;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {

                ListView listView = (ListView) parent;
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String strName = cursor.getString(2);
                Intent intent = new Intent(FriendActivity.this, ChatActivitiy.class);
                intent.putExtra("name", strName);
                startActivity(intent);


            }
        });
        dbHelper.close();


    }
    public void add(View view){
        //通过Intent对象实现带返回结果的界面跳转
        intent.setClass(getApplicationContext(),  AddFriendActivity.class);
        startActivityForResult(intent,1);
    }
    public void zz(View view){
        //通过Intent对象实现带返回结果的界面跳转
        intent.setClass(FriendActivity.this, com.example.phonewallet11.ZzActivity.class);
        //startActivityForResult(intent,1);
        startActivity(intent);
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
