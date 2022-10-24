package com.example.phonewallet11;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UserActivity extends Activity {
    private TextView tv1;
    private Button bt1,bt2,bt3;
    private Intent intent;
    //获取天气相应组件
    private TextView tvCity;
    private TextView tvTemp;
    private TextView tvWind;
    private TextView tvAir;
    private ImageView ivIcon2;
    private Button btnzj, btnsc, btnhb;
    private Map<String, String> map;
    private  List<Map<String, String>> list;
    private String temp, name, air, wind;
    String urlStr = "http://192.168.43.222:8080/TestServer/weather.json";

    //获取图片相应组件
    int pbValue;
    private TextView textViewsy3;
    private Button buttonsy3;
    private ImageView imageView;
    private ProgressBar progressBarsy3;
    final String urlStr1="http://192.168.43.222:8080/TestServer/pic/1.jpg";
    final String urlStr2="http://192.168.43.222:8080/TestServer/pic/2.jpg";


    final Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {  //接收消息
            super.handleMessage(msg);
            //获取子线程传递过来的json数据并保存到变量中
            String json= (String) msg.obj;//obj是任意类型
            List<WeatherInfo> weatherInfos = null;

            //调用getInfosFromJson()方法，将天气信息集合保存到weatherInfos中
            try {
                weatherInfos=getInfosFromJson(json);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //循环读取weatherInfos中的每一条数据
            list = new ArrayList<Map<String, String>>();
            for (WeatherInfo info : weatherInfos) {
                map = new HashMap<String, String>();
                map.put("temp", info.getTemp());
                map.put("name", info.getName());
                map.put("air", info.getAir());
                map.put("wind", info.getWind());
                list.add(map);
            }


        }
    };
    //解析json数据返回天气信息的集合
    public static List<WeatherInfo> getInfosFromJson(String json)
            throws IOException {        ;
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        Type listType = new  TypeToken<List<WeatherInfo>>(){}.getType();
        List<WeatherInfo> list=gson.fromJson(json,listType);

        return list;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        // 初始化控件
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvTemp = (TextView) findViewById(R.id.tv_temp);
        tvWind = (TextView) findViewById(R.id.tv_wind);
        tvAir = (TextView) findViewById(R.id.tv_air);
        ivIcon2 = (ImageView) findViewById(R.id.iv_icon2);
        btnzj = (Button) findViewById(R.id.btn_zj);
        btnsc = (Button) findViewById(R.id.btn_sc);
        btnhb = (Button) findViewById(R.id.btn_hb);
        tv1= (TextView) findViewById(R.id.textView2);

        intent = getIntent();
        tv1.setText(intent.getStringExtra("name"));

        //通过OkHttp的get同步方式访问服务器，并将获取到的json数据封装到消息中发送给UI线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();   //创建OkHttpClient对象
                Request request = new Request.Builder().url(urlStr).build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    //通过handler更新UI
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
                    case R.id.btn_zj:
                        getMap(list, 0, R.drawable.z);
                        break;
                    case R.id.btn_sc:
                        getMap(list, 1, R.drawable.s);
                        break;
                    case R.id.btn_hb:
                        getMap(list, 2, R.drawable.h);
                        break;
                }
            }
        };
        btnzj.setOnClickListener(listener);
        btnsc.setOnClickListener(listener);
        btnhb.setOnClickListener(listener);


        //  获取组件对象
        textViewsy3 = findViewById(R.id.textviewsy3);
        buttonsy3 = findViewById(R.id.buttonsy3);
        progressBarsy3 = findViewById(R.id.progressBarsy3);
        imageView = findViewById(R.id.image);
        buttonsy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //第1种方法：使用AsyncTask+OkHttp的同步请求方式
                new DownloadTask().execute(urlStr2);

            }
        });

    }
    class DownloadTask extends AsyncTask<String,Integer, Bitmap>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textViewsy3.setVisibility(View.VISIBLE);
            progressBarsy3.setVisibility(View.VISIBLE);
            progressBarsy3.setProgress(0);

        }
        @Override
        protected Bitmap doInBackground(String... strings) {//非ui线程，子线程中，用Get同步方式实现
            Bitmap bitmap=null;
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(strings[0])
                        .build();
                Response response = client.newCall(request).execute();
                int maxSize=(int) response.body().contentLength();
                InputStream inputStream=response.body().byteStream();
                byte[] buffer=new byte[1024*6];
                ByteArrayOutputStream out=new ByteArrayOutputStream();
                int progress=0;
                int len=-1;
                while ((len=inputStream.read(buffer)) !=-1) {
                    progress+= len;
                    out.write(buffer, 0, len);
                    Thread.sleep(100);
                    int pbValue = (int) (((double) progress / maxSize) * 100);
                    publishProgress(pbValue);
                }
                bitmap= BitmapFactory.decodeByteArray(out.toByteArray(),0,maxSize);

            } catch(Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //下载过程中更新TextView和进度条的进度值
            textViewsy3.setText("已获取："+values[0]+"%");
            progressBarsy3.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //下载完成后更新TextView和进度条
            textViewsy3.setText("头像获取成功!");
            progressBarsy3.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);

        }
    }

    //自定义getMap()方法，显示天气信息到文本控件中
    //将城市天气信息分条展示到界面上
    private void getMap( List list,  int number,  int iconNumber) {
        Map<String, String> cityMap = (Map<String, String>) list.get(number);
        temp = cityMap.get("temp");
        name = cityMap.get("name");
        air = cityMap.get("air");
        wind = cityMap.get("wind");
        tvCity.setText(name);
        tvTemp.setText("" + temp);
        tvWind.setText("风力  : " + wind);
        tvAir.setText("空气指数: " + air);
        ivIcon2.setImageResource(iconNumber);
    }




    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}

