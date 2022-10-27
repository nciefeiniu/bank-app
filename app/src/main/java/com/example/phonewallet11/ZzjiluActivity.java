package com.example.phonewallet11;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ZzjiluActivity extends AppCompatActivity {
    private ListView list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhangdan);
        list = (ListView) findViewById(R.id.listView);
        this.getTransRecord();

    }

    private void getTransRecord() {
        final Gson gson = new Gson();

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("get_transfer_accounts_record/");
        Request request = new Request.Builder().url(url).build(); // 创建一个请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final TransRecord transRecordResult = gson.fromJson(response.body().charStream(), TransRecord.class);
                Integer code = transRecordResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ZzjiluActivity.this, transRecordResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (transRecordResult.getSuccess().equals(true)) {
                    final List<Datum> resultData = transRecordResult.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String[] from = {"name","phone","money", "time"};
                            int[] to={R.id.name,R.id.phone,R.id.money,R.id.r_time}; //填入要更改的控件内容ID号 ,用来对应from中的两个key值,

                            List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
                            for (int i=0; i<resultData.size(); i++) {
                                HashMap<String, String>  map1 = new HashMap<String, String>();
                                map1.put("name", resultData.get(i).getPayeeName());
                                map1.put("money", resultData.get(i).getMoney());
                                map1.put("time", resultData.get(i).getCreateTime());
                                map1.put("phone", resultData.get(i).getPayeePhone());
                                data.add(map1);
                            }
                            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.activity_zzjilu, from, to);
                            list.setAdapter(adapter);
                        }
                    });

                } else {
                    Toast.makeText(ZzjiluActivity.this, "查看转账记录数据出现异常",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(ZzjiluActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
