package com.example.phonewallet11;
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

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class XxActivity extends AppCompatActivity {
    private EditText dfName,dfPwd;
    private Button btnl,btnf;
    private Intent intent;
    private URL url;//URL对象
    private HttpURLConnection uc;//HttpURLConnection对象

    // 创建Handler，处理Message
    final Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            //通过msg中传递数据判断是否登录成功，若成功则实现界面跳转到ZzActivity
            int i=msg.arg1;
            if(i==49){
                Intent intent=new Intent(XxActivity.this,CxActivity.class);
                startActivity(intent);
            }else if(i==48){
                Toast.makeText(XxActivity.this,"用户名或密码错误！",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(XxActivity.this,"网络连接失败！",Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.phonewallet11.R.layout.activity_xx);
        intent = getIntent();
        dfName=findViewById(com.example.phonewallet11.R.id.df_name);
        dfPwd=findViewById(com.example.phonewallet11.R.id.df_pass);
        btnl=findViewById(com.example.phonewallet11.R.id.btnl);
        btnf=findViewById(com.example.phonewallet11.R.id.btnf);


        btnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1, intent);
                finish();
            }
        });

        btnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=dfName.getText().toString();
                final String pwd=dfPwd.getText().toString();
                //判断是否联网
                if(checkNetworkState()!=true){
                    Toast.makeText(XxActivity.this,"网络没有打开，请打开网络后再试。", Toast.LENGTH_LONG).show();
                }else {
                    if (name.equals("")) {
                        Toast toast = Toast.makeText(XxActivity.this,
                                "输入用户名", Toast.LENGTH_LONG);
                        toast.show();
                    } else if (pwd.equals("")) {
                        Toast toast = Toast.makeText(XxActivity.this,
                                "输入密码", Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        //新建线程，并通过OkHttp的get同步请求方式把用户名和密码向服务器发送请求并将结果发送给Handler对象
                        //使用try-catch-finally捕获异常
                        //同步请求步骤：
                        //1.新建线程并启动线程,并在线程的run()方法中进行以下步骤
                        //2.创建OkHttpClient对象并添加配置
                        //3.创建Request对象
                        //通过OkHttp的post异步请求方式把用户名和密码向服务器发送请求并将结果发送给Handler对象
                        //Post异步请求步骤：
                        //1.创建OkHttpClient对象并添加配置
                        //2.创建FormBody对象并通过add方法传递用户名和密码的键值对参数
                        //3.创建Request对象
                        //4.创建Call对象并调用enqueue()方法
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String urlStr="http://192.168.43.222:8080/TestServer/servlet/Login";
                                try {
                                    url=new URL(urlStr);
                                    uc= (HttpURLConnection) url.openConnection();
                                    uc.setRequestMethod("POST");
                                    uc.setConnectTimeout(5000);
                                    String data="name="+ URLEncoder.encode(name)+"&pass="+URLEncoder.encode(pwd);
                                    //设置请求头数据提交方式，
                                    uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                                    //设置长度
                                    uc.setRequestProperty("Content-Length",data.length()+"");
                                    uc.setDoOutput(true);
                                    uc.connect();

                                    OutputStream outputStream=uc.getOutputStream();
                                    outputStream.write(data.getBytes());

                                    int code=uc.getResponseCode();
                                    System.out.println(code);
                                    if(code==HttpURLConnection.HTTP_OK){
                                        InputStream inputStream= uc.getInputStream();
                                        int i=inputStream.read();
                                        //Message数据传递handler.sendMessage()
                                        Message message=Message.obtain();
                                        message.arg1=i;
                                        handler.sendMessage(message);
                                    }else {
                                        Message message=Message.obtain();
                                        message.arg1=2;
                                        handler.sendMessage(message);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }finally {
                                    if(uc!=null){
                                        uc.disconnect();
                                    }
                                }
                            }
                        }).start();

                    }
                }
            }
        });
    }

    //使用ConnectivityManager检查网络状态
    private boolean checkNetworkState(){
        //通过系统服务获取ConnectivityManager类的对象，
        // 并调用getActiveNetworkInfo()获取网络信息
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if((networkInfo==null)||(networkInfo.isConnected()==false)){
            return false;
        }
        return true;
    }
}

