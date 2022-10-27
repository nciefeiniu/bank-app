package com.example.phonewallet11;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.phonewallet11.myInfo.Data;
import com.example.phonewallet11.myInfo.MyInfo;
import com.example.phonewallet11.okhttpClientManager.clientManager;
import com.example.phonewallet11.transRecord.Datum;
import com.example.phonewallet11.transRecord.TransRecord;
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

public class OneselfActivity extends Activity {
    private TextView name, account, phone, sex, id;

    private Button xiugai;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oneself);
        xiugai  = (Button) findViewById(R.id.xiugai);
        intent  = getIntent();

        xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(OneselfActivity.this, UpuserActivity.class);
                startActivity(intent);
            }
        });

        this.getMyInfo();


    }

    public void getMyInfo() {
        final Gson gson = new Gson();

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("get_my_info/");
        Request request = new Request.Builder().url(url).build(); // 创建一个请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final MyInfo myinfoResult = gson.fromJson(response.body().charStream(), MyInfo.class);
                Integer code = myinfoResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(OneselfActivity.this, myinfoResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (myinfoResult.getSuccess().equals(true)) {
                    final Data resultData = myinfoResult.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            name = (TextView) findViewById(R.id.name);
                            account = (TextView) findViewById(R.id.account);
                            phone = (TextView) findViewById(R.id.phone);
                            sex = (TextView) findViewById(R.id.sex);
                            id = (TextView) findViewById(R.id.id);
                            name.setText(resultData.getName());
                            account.setText(resultData.getAccount());
                            phone.setText(resultData.getPhone());
                            sex.setText(resultData.getSex());
                            id.setText(resultData.getIdNumber());
                        }
                    });

                } else {
                    Toast.makeText(OneselfActivity.this, "查看个人信息数据出现异常",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(OneselfActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
