package com.example.phonewallet11;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phonewallet11.allRecord.AllRecord;
import com.example.phonewallet11.allRecord.Datum;
import com.example.phonewallet11.okhttpClientManager.clientManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        this.getAllRecord();
//        // TODO Auto-generated method stub
//        final DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
//        Cursor c = dbHelper.queryrecord(id);
//        //将查询到的联系人信息按照list.xml布局方式显示到ListView中
//        String[] from = new String[]{"r_name","r_mode","r_money","r_time"};
//        int[] to = new int[]{R.id.name, R.id.mode,R.id.money,R.id.r_time};
//
//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_list, c, from, to);
//        list.setAdapter(adapter);
//
//
//        dbHelper.close();


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

    public void getAllRecord() {
        final Gson gson = new Gson();

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("get_all_record/");
        Request request = new Request.Builder().url(url).build(); // 创建一个请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final AllRecord recordResult = gson.fromJson(response.body().charStream(), AllRecord.class);
                Integer code = recordResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ZhangdanActivity.this, recordResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (recordResult.getSuccess().equals(true)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<Datum> resultData = recordResult.getData();
                            String[] from = {"name","type", "money", "time", "card_no"};
                            int[] to={R.id.name,R.id.mode, R.id.money, R.id.r_time, R.id.card_no}; //填入要更改的控件内容ID号 ,用来对应from中的两个key值,

                            List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
                            for (int i=0; i<resultData.size(); i++) {
                                HashMap<String, String>  map1 = new HashMap<String, String>();
                                map1.put("name", resultData.get(i).getName());
                                map1.put("type", resultData.get(i).getType());
                                map1.put("money", resultData.get(i).getMoney());
                                map1.put("time", resultData.get(i).getTime());
                                map1.put("card_no", resultData.get(i).getCardNo());
                                data.add(map1);
                            }

                            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.activity_list, from, to);
                            list.setAdapter(adapter);
//                            String[] array2 = new String[arrayLength];  / / 卡号
//                            String[] array1 = new String[arrayLength];  // 名
//                            String[] array3 = new String[arrayLength];  // 操作
//                            String[] array4 = new String[arrayLength];  // 金额
//                            String[] array5 = new String[arrayLength];  // 时间
//                            for (int i=0; i<resultData.size(); i++) {
//                                array2[i] = resultData.get(i).getCardNo();
//                                array1[i] = resultData.get(i).getName();
//                                array3[i] = resultData.get(i).getType();
//
//                                array4[i] = resultData.get(i).getMoney();
//
//                                array5[i] = resultData.get(i).getTime();
//
//                            }
//                            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(ZhangdanActivity.this, R.layout.activity_list, R.id.card_no, array2);
//                            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(ZhangdanActivity.this, R.layout.activity_list, R.id.name, array1);
//                            ArrayAdapter<String> adapter3=new ArrayAdapter<String>(ZhangdanActivity.this, R.layout.activity_list, R.id.mode, array3);
//                            ArrayAdapter<String> adapter4=new ArrayAdapter<String>(ZhangdanActivity.this, R.layout.activity_list, R.id.money, array4);
//                            ArrayAdapter<String> adapter5=new ArrayAdapter<String>(ZhangdanActivity.this, R.layout.activity_list, R.id.r_time, array5);
//                            list.setAdapter(adapter1);
//                            list.setAdapter(adapter2);
//                            list.setAdapter(adapter3);
//                            list.setAdapter(adapter4);
//                            list.setAdapter(adapter5);

                        }
                    });

                } else {
                    Toast.makeText(ZhangdanActivity.this, "获取账单失败！",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(ZhangdanActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
