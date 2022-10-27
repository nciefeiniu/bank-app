package com.example.phonewallet11;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import androidx.annotation.NonNull;

import com.example.phonewallet11.okhttpClientManager.clientManager;
import com.example.phonewallet11.stockRecord.Datum;
import com.example.phonewallet11.stockRecord.StockRecord;
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

public class GmActivity extends AppCompatActivity {
    private TextView tvzyj8;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhangdan);
        list = (ListView) findViewById(R.id.listView);
        this.getBuyStockRecord();
    }

    public void getBuyStockRecord() {
        final Gson gson = new Gson();

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("get_all_stock_record/");
        Request request = new Request.Builder().url(url).build(); // 创建一个请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final StockRecord stockRecordResult = gson.fromJson(response.body().charStream(), StockRecord.class);
                Integer code = stockRecordResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GmActivity.this, stockRecordResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (stockRecordResult.getSuccess().equals(true)) {
                    final List<Datum> resultData = stockRecordResult.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String[] from = {"stock_number","money", "time"};
                            int[] to={R.id.stockNumber,R.id.money,R.id.r_time}; //填入要更改的控件内容ID号 ,用来对应from中的两个key值,

                            List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
                            for (int i=0; i<resultData.size(); i++) {
                                HashMap<String, String>  map1 = new HashMap<String, String>();
                                map1.put("stock_number", resultData.get(i).getStockNumber());
                                map1.put("money", resultData.get(i).getMoney());
                                map1.put("time", resultData.get(i).getCreateTime());
                                data.add(map1);
                            }
                            SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), data, R.layout.activity_gm, from, to);
                            list.setAdapter(adapter);
                        }
                    });

                } else {
                    Toast.makeText(GmActivity.this, "查看股票购买数据出现异常",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(GmActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
