package com.example.phonewallet11;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.phonewallet11.api_response.login.Login;
import com.example.phonewallet11.balance.Balance;
import com.example.phonewallet11.balance.Data;
import com.example.phonewallet11.okhttpClientManager.clientManager;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        this.getBalance();
//        String id = intent.getStringExtra("id");
//        DatebaseHelper dbHelper=new DatebaseHelper(getApplicationContext());
//        Cursor c=dbHelper.query(id);
//        if(c.getCount()!=0) {
//            c.moveToFirst();
//            intent.putExtra("id", c.getString(0));
//            intent.putExtra("account", c.getString(1));
//            intent.putExtra("name", c.getString(2));
//            intent.putExtra("sex", c.getString(3));
//            intent.putExtra("phone", c.getString(4));
//            intent.putExtra("qx", c.getString(5));
//            intent.putExtra("login_password", c.getString(6));
//            intent.putExtra("pay_password", c.getString(7));
//            // String moneys=String.valueOf(c.getFloat(8));
//            intent.putExtra("moneys", c.getFloat(8));
//
//        }else {
//            Log.v("数据错误！","");
//        }
        /*String id = intent.getStringExtra("id");
        XiaoxiActivity xiaoxin = new XiaoxiActivity();
        intent = xiaoxin.getxinxi(id);*/


//        tv2.setText(intent.getStringExtra("name"));
//        Float moneys= intent.getFloatExtra("moneys",0);
//        tv4.setText(moneys+"");


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

    public void getBalance() {
        final Gson gson = new Gson();
        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("get_balance/");

        Request request = new Request.Builder().url(url).build(); // 创建一个请求

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final Balance balanceResult = gson.fromJson(response.body().charStream(), Balance.class);
                Integer code = balanceResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MoneyActivity.this, balanceResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    final Data data = balanceResult.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv2.setText(intent.getStringExtra("name"));
                            Double moneys= data.getBalance();
                            tv4.setText(moneys+"");
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(MoneyActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //调用getRelationFromDB()方法
        if (requestCode == resultCode) {
            getBalance();
        }
    }
}
