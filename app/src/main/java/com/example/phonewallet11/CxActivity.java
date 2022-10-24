package com.example.phonewallet11;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CxActivity extends AppCompatActivity{
    private TextView tev1;
    private Button bt1;
    private ImageView im1;
    private ProgressBar pb1;
    final String urlStry1="http://192.168.43.222:8080/TestServer/pic/cx.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.phonewallet11.R.layout.activity_cx);
        //  获取组件对象
        tev1= findViewById(com.example.phonewallet11.R.id.tev1);
        bt1 = findViewById(com.example.phonewallet11.R.id.bt1);
        pb1 = findViewById(com.example.phonewallet11.R.id.pb1);
        im1 = findViewById(com.example.phonewallet11.R.id.im1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //第1种方法：使用AsyncTask+OkHttp的同步请求方式
                //第2种方法：使用OkHttp的异步请求方式+Handler或runOnUiThread（）
                new  CxActivity.DownloadTask().execute(urlStry1);

            }
        });

    }
    class DownloadTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tev1.setVisibility(View.VISIBLE);
            pb1.setVisibility(View.VISIBLE);
            pb1.setProgress(0);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap=null;

            try {
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder()
                        .url(strings[0])
                        .build();
                Response response=client.newCall(request).execute();
                int maxSize=(int) response.body().contentLength();
                InputStream inputStream=response.body().byteStream();
                byte[] buffer=new byte[1024*6];
                ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                int len=-1;
                int process=0;
                while ((len=inputStream.read(buffer))!=-1){
                    process+=len;
                    outputStream.write(buffer,0,len);
                    int pbValue=(int)((((double)process)/maxSize)*pb1.getMax());
                    System.out.println(maxSize);
                    System.out.println(process);
                    System.out.println(pbValue);
                    Thread.sleep(50);
                    publishProgress(pbValue);
                }
                bitmap= BitmapFactory.decodeByteArray(outputStream.toByteArray(),0,maxSize);
                client.newCall(request).execute();

            }catch (IOException e){
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            tev1.setText("正在加载"+values[0]+"%");
            pb1.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            tev1.setText("账单加载成功");
            pb1.setVisibility(View.INVISIBLE);
            im1.setVisibility(View.VISIBLE);
            im1.setImageBitmap(bitmap);
        }

    }

}

