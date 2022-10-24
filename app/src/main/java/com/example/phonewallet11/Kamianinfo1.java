package com.example.phonewallet11;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Kamianinfo1 extends AppCompatActivity {
    private TextView tvKm;
    private TextView tvTuan;
    private TextView tvDise;
    private TextView tvFaxingliang;
    private TextView tvQingkuang;
    private Button btKm1, btKm2;
    private Map<String, String> map;
    private List<Map<String, String>> list;
    private String name, tuan, dise, faxingliang, qingkuang;
    String urlStr = "http://192.168.43.222:8080/TestServer/kamian.json";
    final Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //获取子线程传递过来的json数据并保存到变量中
            String json= (String) msg.obj;
                List<Kamianinfo> kamianinfos = null;

            //调用getInfosFromJson()方法，将天气信息集合保存到weatherInfos中
            try {
                kamianinfos=getInfosFromJson(json);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //循环读取weatherInfos中的每一条数据
            list = new ArrayList<Map<String, String>>();
            for (Kamianinfo info : kamianinfos) {
                map = new HashMap<String, String>();
                map.put("tuan", info.getTuan());
                map.put("dise", info.getDise());
                map.put("faxingliang", info.getFaxingliang());
                map.put("qingkuang", info.getQingkuang());
                list.add(map);
            }


        }
    };
    //解析json数据返回天气信息的集合
    public static List<Kamianinfo> getInfosFromJson(String json)
            throws IOException {        ;
        //使用gson库解析JSON数据
        Gson gson=new Gson();
        Type listType=new TypeToken<List<Kamianinfo>>(){}.getType();
        List<Kamianinfo> list=gson.fromJson(json,listType);

        return list;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamian);

        // 初始化控件
        tvKm = (TextView) findViewById(R.id.tv_Km);
        tvTuan = (TextView) findViewById(R.id.tv_tuan);
        tvDise= (TextView) findViewById(R.id.tv_dise);
        tvFaxingliang = (TextView) findViewById(R.id.tv_faxingliang);
        tvQingkuang = (TextView) findViewById(R.id.tv_qingkuang);
        btKm1 = (Button) findViewById(R.id.bt_Km1);
        btKm2 = (Button) findViewById(R.id.bt_Km2);
        //通过OkHttp的get同步方式访问服务器，并将获取到的json数据封装到消息中发送给UI线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder().url(urlStr).build();
                Call call=client.newCall(request);
                try {
                    Response response=call.execute();
                    String result=response.body().string();
                    Message message= Message.obtain();
                    message.obj=result;
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
                    case R.id.bt_Km1:
                        getMap(list, 0, R.drawable.cloud_sun);
                        break;
                    case R.id.bt_Km2:
                        getMap(list, 1, R.drawable.sun);
                        break;
                }
            }
        };
        btKm1.setOnClickListener(listener);
        btKm2.setOnClickListener(listener);

    }

    //自定义getMap()方法，显示天气信息到文本控件中
    //将城市天气信息分条展示到界面上
    private void getMap(List list, int number, int iconNumber) {
        Map<String, String> hdMap = (Map<String, String>) list.get(number);
        tuan = hdMap.get("tuan");
        dise = hdMap.get("dise");
        faxingliang = hdMap.get("faxingliang");
        qingkuang= hdMap.get("qingkuang");
        tvKm.setText(name);
        tvTuan.setText("所属生肖：" + tuan);
        tvDise.setText("展示颜色：" +dise );
        tvFaxingliang.setText("总发行量：" +faxingliang);
        tvQingkuang.setText("具体情况：" + qingkuang);
    }

}
