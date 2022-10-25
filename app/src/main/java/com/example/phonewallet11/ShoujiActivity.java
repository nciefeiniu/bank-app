package com.example.phonewallet11;

import android.app.Activity;

import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.phonewallet11.okhttpClientManager.clientManager;
import com.example.phonewallet11.rechargePhone.RechargePhone;
import com.example.phonewallet11.withDrawal.WithDrawal;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShoujiActivity extends Activity {
    private EditText phone,pay_money,pay_password;
    private Button submit;
    private Intent intent;
    private URL url;//URL对象
    private HttpURLConnection uc;//HttpURLConnection对象

    // 创建Handler，处理Message
    final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            //通过msg中传递数据判断是否登录成功，若成功则实现界面跳转到ZzActivity
            int i=msg.arg1;
            if(i==49){
                Intent intent=new Intent(ShoujiActivity.this,ZzActivity.class);
                startActivity(intent);
            }else if(i==48){
                Toast.makeText(ShoujiActivity.this,"用户名或密码错误！",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(ShoujiActivity.this,"网络连接失败！",Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shouji);

        intent = getIntent();
        phone  = (EditText) findViewById(R.id.phone);
        pay_money    = (EditText) findViewById(R.id.pay_money);
        pay_password = (EditText) findViewById(R.id.pay_password);
        submit  = (Button) findViewById(R.id.submit);
        //建立事件监听


        //二、POST方式步骤：
        // 1.通过URL字符串创建URL对象
        //2.使用url的openConnection()方法获得HttpURLConnection对象
        //3.设置HttpURLConnection对象的请求方式、超时、请求头数据提交方式等，
        // 以POST方式向服务器发送请求，并调用connect()建立连接
        //4.通过getOutputStream()获得输出流往服务器写数据（109）
        //5.得到响应码并判断连接正常后，通过getInputStream()方法获得携带服务器返回信息的输入流（115），
        //  从流中读取出服务器返回值，并向handler发送消息
        //6.断开连接
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=phone.getText().toString();
                final String pwd=pay_password.getText().toString();
//                final double money = Double.parseDouble(pay_money.getText().toString());
                final String money = pay_money.getText().toString();

                if (name.equals("")) {
                    Toast toast = Toast.makeText(ShoujiActivity.this,
                            "输入用户名", Toast.LENGTH_LONG);
                    toast.show();
                } else if (pwd.equals("")) {
                    Toast toast = Toast.makeText(ShoujiActivity.this,
                            "输入密码", Toast.LENGTH_LONG);
                    toast.show();
                } else if (money.equals("")) {
                    Toast toast = Toast.makeText(ShoujiActivity.this,
                            "输入密码", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    double _money = Double.parseDouble(money);
                    ShoujiActivity.this.rechargePhoneBill(_money, name, pwd);
                }
                //判断是否联网
//                if(checkNetworkState()!=true){
//                    Toast.makeText(ShoujiActivity.this,"网络没有打开，请打开网络后再试。",Toast.LENGTH_LONG).show();
//                }else {
//                    if (name.equals("")) {
//                        Toast toast = Toast.makeText(ShoujiActivity.this,
//                                "输入用户名", Toast.LENGTH_LONG);
//                        toast.show();
//                    } else if (pwd.equals("")) {
//                        Toast toast = Toast.makeText(ShoujiActivity.this,
//                                "输入密码", Toast.LENGTH_LONG);
//                        toast.show();
//                    } else {
//                        //新建线程，并通过get或post方式把用户名和密码向服务器发送请求并将结果发送给Handler对象
//                        //使用try-catch-finally捕获异常
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                String urlStr="http://192.168.43.222:8080/TestServer/servlet/Login";
//                                try {
//                                    url=new URL(urlStr);
//                                    uc= (HttpURLConnection) url.openConnection();
//                                    uc.setRequestMethod("POST");
//                                  // POST方式向服务器发出请求时需要在请求后加上实体，
//                                    // 向服务器提交的参数在请求后的实体中
//                                    //POST的重点是向服务器发送数据
//                                    uc.setConnectTimeout(5000);
//                                    String data="name="+ URLEncoder.encode(name)+"&pass="+URLEncoder.encode(pwd);
//                                    //设置请求头数据提交方式，
//                                    uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//                                    //设置长度
//                                    uc.setRequestProperty("Content-Length",data.length()+"");
//                                    uc.setDoOutput(true);
//                                    uc.connect();
//
//                                    OutputStream outputStream=uc.getOutputStream();
//                                    outputStream.write(data.getBytes());
//
//                                    int code=uc.getResponseCode();
//                                    System.out.println(code);
//                                    if(code==HttpURLConnection.HTTP_OK){
//                                        InputStream inputStream= uc.getInputStream();
//                                        int i=inputStream.read();
//                                        //Message数据传递handler.sendMessage()
//                                        Message message=Message.obtain();
//                                        message.arg1=i;
//                                        handler.sendMessage(message);
//                                    }else {
//                                        Message message=Message.obtain();
//                                        message.arg1=2;
//                                        handler.sendMessage(message);
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }finally {
//                                    if(uc!=null){
//                                        uc.disconnect();
//                                    }
//                                }
//                            }
//                        }).start();
//
//                    }
//                }
            }
        });
    }

    public void rechargePhoneBill(double money, String name, String password) {
        final Gson gson = new Gson();

        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("money", money);
            json.put("pay_password", password);
            json.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("recharge_phone_bill/");
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

        Request request = new Request.Builder().url(url).post(requestBody).build(); // 创建一个请求

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final RechargePhone rechargePhoneResult = gson.fromJson(response.body().charStream(), RechargePhone.class);
                Integer code = rechargePhoneResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ShoujiActivity.this, rechargePhoneResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ShoujiActivity.this, "充值成功！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(ShoujiActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //使用ConnectivityManager检查网络状态
    private boolean checkNetworkState(){
        //通过系统服务获取ConnectivityManager类的对象，
        // 并调用getActiveNetworkInfo()获取网络信息
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null||networkInfo.isConnected()==false)
        {
            return false;
        }
        return true;
    }
}
