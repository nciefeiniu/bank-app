package com.example.phonewallet11;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CardxykActivity extends AppCompatActivity {
    private TextView tv1;
    private ListView list;
    private Intent intent ;
    private  String sfz;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        intent=getIntent();
        sfz=intent.getStringExtra("id");
        tv1 = (TextView) findViewById(R.id.textView2);
        tv1.setText(intent.getStringExtra("name"));

        list = (ListView) findViewById(R.id.listView1);
        getRelationFromDB();


    }

    private void getRelationFromDB() {
        // TODO Auto-generated method stub
        final DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
        Cursor c = dbHelper.querykb(sfz);
        //将查询到的联系人信息按照relationlist.xml布局方式显示到ListView中
        String[] from = new String[]{"_id","k_phone"};
        int[] to = new int[]{R.id.tvNum, R.id.tvPhone};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.kblist, c, from, to);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {



                final long temp = id;
                //利用对话框实现删除记录的提示操作
                AlertDialog.Builder builder = new AlertDialog.Builder(CardxykActivity.this);
                builder.setMessage("确认要删除吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dbHelper.delkb((int)temp);
                        Cursor c = dbHelper.querykb(sfz);
                        String[] from = new String[]{"_id", "k_phone"};
                        int[] to = new int[]{R.id.tvNum, R.id.tvPhone};
                        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.kblist, c, from, to);
                        list.setAdapter(adapter);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });
        dbHelper.close();


    }
    public void add(View view){
        //通过Intent对象实现带返回结果的界面跳转
        intent.setClass(getApplicationContext(), AddCardActivity.class);
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
