package com.example.phonewallet11;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.view.View;

import com.example.phonewallet11.api_response.login.Data;
import com.example.phonewallet11.api_response.register.Register;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
//import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.phonewallet11.api_response.login.Login;
import com.example.phonewallet11.okhttpClientManager.clientManager;


public class MainActivity extends AppCompatActivity {
    public static int USERID;
    private Button btsubmit, btreg;
    private EditText etaccount, etlogin_password;
    private Intent intent;

    private TextView tvCity3;
    private TextView tvYear;
    private TextView tvNum2;
    private ImageView ivIcon3;
    private Button btntj, btncq, btnyn;
    private Map<String, String> map;
    private List<Map<String, String>> list;
    private String year, num2, name2;
    String urlStr6 = "http://192.168.43.222:8080/TestServer/year.json";

    final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //获取子线程传递过来的json数据并保存到变量中
            String json = (String) msg.obj;
            List<YearInfo> yearInfos = null;

            //调用getInfosFromJson()方法，将天气信息集合保存到yearInfos中
            try {
                yearInfos = getInfosFromJson(json);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //循环读取weatherInfos中的每一条数据
            list = new ArrayList<Map<String, String>>();
            for (YearInfo info : yearInfos) {
                map = new HashMap<String, String>();
                map.put("year", info.getYear());
                map.put("num", info.getNum());
                map.put("name", info.getName());
                list.add(map);
            }


        }
    };

    //解析json数据返回天气信息的集合
    public static List<YearInfo> getInfosFromJson(String json)
            throws IOException {
        ;
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        Type listType = new TypeToken<List<YearInfo>>() {
        }.getType();
        List<YearInfo> list = gson.fromJson(json, listType);

        return list;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        intent =getIntent();
        btsubmit = (Button) findViewById(R.id.submit);
        btreg = (Button) findViewById(R.id.reg);

        etaccount = (EditText) findViewById(R.id.account);
        etlogin_password = (EditText) findViewById(R.id.login_password);

        // 初始化控件
        tvCity3 = (TextView) findViewById(R.id.tv_city3);
        tvYear = (TextView) findViewById(R.id.tv_year);
        tvNum2 = (TextView) findViewById(R.id.tv_num2);
        ivIcon3 = (ImageView) findViewById(R.id.iv_icon3);
        btntj = (Button) findViewById(R.id.btn_tj);
        btncq = (Button) findViewById(R.id.btn_cq);
        btnyn = (Button) findViewById(R.id.btn_yn);

        //3通过OkHttp的get同步方式访问服务器，并将获取到的json数据封装到消息中发送给UI线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(urlStr6).build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    Message message = Message.obtain();
                    message.obj = result;
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_tj:
                        getMap(list, 0, R.drawable.t);
                        break;
                    case R.id.btn_cq:
                        getMap(list, 1, R.drawable.c);
                        break;
                    case R.id.btn_yn:
                        getMap(list, 2, R.drawable.y);
                        break;
                }
            }
        };
        btntj.setOnClickListener(listener);
        btncq.setOnClickListener(listener);
        btnyn.setOnClickListener(listener);


        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etaccount.getText().toString();
                String password = etlogin_password.getText().toString();

                if (account.equals("")) {
                    Toast.makeText(MainActivity.this, "用户名为空", Toast.LENGTH_SHORT).show();
                } else if (account.equals("admin") && password.equals("admin")) {
                    Intent aa = new Intent(MainActivity.this, Userht.class);
                    startActivity(aa);
                } else {
                    MainActivity.this.sendLoginData(account, password);
//                    DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
//                    Cursor c = dbHelper.loginquery(account);
//
//                    if (c.getCount() != 0) {
//                        c.moveToFirst();
//                        String login_pwd = c.getString(6);
//                        if (password.equals(login_pwd)) {
//
//                            Intent intent = new Intent(MainActivity.this, Caidan.class);
//                            intent.putExtra("id", c.getString(0));
//                            intent.putExtra("account", c.getString(1));
//                            intent.putExtra("name", c.getString(2));
//                            intent.putExtra("sex", c.getString(3));
//                            intent.putExtra("phone", c.getString(4));
//                            intent.putExtra("qx", c.getString(5));
//                            intent.putExtra("login_password", c.getString(6));
//                            intent.putExtra("pay_password", c.getString(7));
//                            // String moneys=String.valueOf(c.getFloat(8));
//                            intent.putExtra("moneys", c.getFloat(8));
//
//                            startActivity(intent);
//                            //0代表MODE_PRIVATE私有模式
//                            SharedPreferences sp = getSharedPreferences("usermemory", 0);
//                            SharedPreferences.Editor ed = sp.edit();
//                            ed.putString("name", c.getString(c.getColumnIndex("name")));
//
//
//                            ed.commit();
//                        } else {
//                            Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
//                        }

//                    } else {
//                        Toast.makeText(MainActivity.this, "没有此用户，请注册！", Toast.LENGTH_SHORT).show();
//                    }
                }
            }
        });
        btreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setClass(MainActivity.this, RegisterActivity.class);
                startActivity(intent1);
            }
        });
    }

    public void sendLoginData(String account, String password) {
        final Gson gson = new Gson();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("account", account);
            json.put("login_password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("login/");

        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        Request request = new Request.Builder().url(url).post(requestBody).build(); // 创建一个请求

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final Login loginResult = gson.fromJson(response.body().charStream(), Login.class);
                Integer code = loginResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, loginResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (loginResult.getSuccess().equals(true)) {
                    final Data data = loginResult.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, Caidan.class);
                            intent.putExtra("id", data.getIdNumber());  // 这是身份证
                            intent.putExtra("account", data.getAccount());
                            intent.putExtra("name", data.getName());
                            intent.putExtra("sex", data.getSex());
                            intent.putExtra("phone", data.getPhone());
                            intent.putExtra("qx", data.getQx());
                            intent.putExtra("login_password", "");
                            intent.putExtra("pay_password", "");
                            intent.putExtra("id_number", data.getId());  // 这是存储在数据库中的ID
//                             String moneys=String.valueOf(c.getFloat(8));
                            intent.putExtra("moneys", data.getMoneys());
                            startActivity(intent);
                        }
                    });

                } else {
                    Toast.makeText(MainActivity.this, "注册失败，",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //自定义getMap()方法，显示天气信息到文本控件中
    //将城市天气信息分条展示到界面上
    private void getMap(List list, int number, int iconNumber) {
        Map<String, String> cityMap = (Map<String, String>) list.get(number);
        year = cityMap.get("year");
        num2 = cityMap.get("num");
        name2 = cityMap.get("name2");
        tvCity3.setText(name2);
        tvYear.setText(year);
        tvNum2.setText("" + num2);
        ivIcon3.setImageResource(iconNumber);
    }

}