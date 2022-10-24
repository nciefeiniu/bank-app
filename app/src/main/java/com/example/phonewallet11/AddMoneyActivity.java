package com.example.phonewallet11;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import java.io.IOException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

public class AddMoneyActivity extends Activity {
    private EditText etmoney,etbank,etpay_pwd;
    private Button btok,return1;
    private  String name,sfz;
    private  float moneys;
    private Intent intent;
//Asynctask异步处理
    private TextView tvs1;
    private ProgressBar pbs1;
    private Button btns1;
    private int pvalues1;

    //音乐播放组件
    private boolean isPause = false;   //是否暂停
    private Button play3;
    private Button pause3;
    private Button stop3;
    private MediaPlayer player3;   //定义音乐播放对象

    //图片获取组件
    private TextView tv;
    private Button bt;
    private ImageView ig;
    private ProgressBar pb2;
    final String urlStr="http://192.168.1.3:8080/TestServer/pic/4.jpg";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmoney);


        //获取组件对象,Asynctask异步处理
        tvs1 = findViewById(R.id.tvs1);
        pbs1= findViewById(R.id.pbs1);
        btns1 = findViewById(R.id.btns1);

        //创建播放器对象并设置音频/视频数据源
           player3 = MediaPlayer.create(this, R.raw.m03);//播放本地音乐

        //图片获取组件
        tv=findViewById(R.id.ttv2);
        bt=findViewById(R.id.bbt2);
        pb2=findViewById(R.id.ppb2);
        ig=findViewById(R.id.iig2);

        intent=getIntent();
        name=intent.getStringExtra("name");
        sfz=intent.getStringExtra("id");

        moneys= intent.getFloatExtra("moneys",0);


        //音乐播放组件
        play3 = (Button) findViewById(R.id.btnPlay3);
        pause3 = (Button) findViewById(R.id.btnPause3);
        stop3= (Button) findViewById(R.id.btnStop3);


        etmoney=(EditText) findViewById(R.id.money);
        etbank=(EditText) findViewById(R.id.bank);
        etpay_pwd=(EditText) findViewById(R.id.pay_pwd);
        btok=(Button) findViewById(R.id.btOk);
        return1=(Button) findViewById(R.id.return1);
        return1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1, intent);
                finish();
            }
        });

        //Button按钮事件监听，新建AsyncTask类的实例对象并调用其execute()方法
        btns1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新建MyAsyncTask类对象，并调用其execute()方法
                new MyTask().execute();

            }
        });
//Button按钮事件监听，新建AsyncTask类的实例对象并调用其execute()方法
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建DownloadTask实例对象，并调用其execute()方法
                new DownloadTask().execute(urlStr);
            }
        });

        player3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //重新开始播放
                player3.start();

            }
        });
        //设置按钮的单击事件监听器
        play3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*实现MediaPlayer对象的播放，并判断当前是否处于暂停状态，
                若处于暂停状态则将“暂停”按钮文本内容设置为“暂停”并设置暂停标记isPause为false。
                 */
                player3.start();
                if (isPause) {
                    pause3.setText("暂停");
                    isPause = false;
                }
            }
        });
        pause3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*判断当前是否正在播放并且没有处于暂停状态，
                若是则暂停播放、设置暂停标记isPause为true并将“暂停”按钮文本内容设置为“继续”；
                否则继续播放、设置暂停标记isPause为false并将“暂停”按钮文本内容设置为“暂停”
                 */
                if (player3.isPlaying() && !isPause) {
                    player3.pause();
                    isPause = true;
                    ((Button) v).setText("继续");
                } else {
                    player3.start();
                    ((Button) v).setText("暂停");
                    isPause = false;
                }
            }
        });
        stop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 判断播放器对象是否为null，若是则停止播放、
                并在调用stop后设置再次通过start进行播放前调用prepare函数。
                 */
                if (player3 != null) {
                    player3.stop();
                    try {
                        player3.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        if(player3.isPlaying()){
            player3.stop();   //停止音频的播放
        }
        player3.release();     //释放资源
        super.onDestroy();
    };

     /* AsyncTask中各方法执行说明：
    1.当Task启动，首先执行onPreExecute()方法（UI Thread，进行UI组件初始状态设置），
      该方法执行完毕立即调用doInBackground()方法。
    2.doInBackground()方法在onPreExecute()执行之后执行---非UI Thread。
    3.publishProgress()方法在后台执行，主要用于发布进度，
      该方法调用，则onProgressUpdate()方法被回调，否则不会回调onProgressUpdate()方法。
    4.onProgressUpdate()方法只有在publishProgress()方法被调用后，才会被系统回调---UI Thread，
      更新UI组件。
    5.onPostExecute()方法在doInBackground()方法执行完毕后被调用----UI Thread，
      进行UI组件更新的后续操作*/

    //    新建AsyncTask的子类并重写相关的方法：
//    1.在onPreExecute()中设置各组件的可见性及进度条的初始值
//    2.在doInBackground()方法中，每隔100ms通过publishProgress()方法发布进度条进度值
//    3.在onProgressUpdate()方法中，设置UI组件的进度更新值，值取自values[0]
//    4.在onPostExecute()方法中，设置下载完成状态时UI组件的属性
    class MyTask extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvs1.setVisibility(View.VISIBLE);
            pbs1.setVisibility(View.VISIBLE);
            pbs1.setProgress(0);
        }

        @Override
        protected String doInBackground(String... strings) {
            pvalues1 = 0;
            //每100ms将随机产生的进度值通过Message传递给UI线程的Handler对象
            while (pvalues1 < pbs1.getMax()) {
                pvalues1 += (int) (Math.random() * 10);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(pvalues1);

            }
            return "获取完成！";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            tvs1.setText("已获取"+values[0]+"%");
            pbs1.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvs1.setText(s);
            pbs1.setVisibility(View.GONE);
        }
    }

    //下载图片
        class DownloadTask extends AsyncTask<String,Integer, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //UI组件初始化设置
            tv.setVisibility(View.VISIBLE);
            pb2.setVisibility(View.VISIBLE);
            pb2.setProgress(0);

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            Bitmap bitmap = null;
            URL url;
            try {
                //连接网络并获取图片，通过urlConnection的getContentLength()获取下载图片的总大小
                //通过inputStream.read(buffer)和while循环读取数据并通过ByteArrayOutputStream将数据写入，
                // 计算数据下载的完成百分百，并每隔50ms通过publishProgress()发布进度值;
                //数据读取完成后通过BitmapFactory.decodeByteArray()将ByteArrayOutputStream中数据
                // 转换成Bitmap对象并返回
                url=new URL(strings[0]);
                urlConnection= (HttpURLConnection) url.openConnection();//创建HttpURLConnection对象
                urlConnection.setRequestMethod("GET");//设置URL请求的方法
                urlConnection.setConnectTimeout(5000);//设置超时时间
                urlConnection.connect();//连接,请求行、请求头的设置必须放在网络连接前。


                if(urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    //getResponseCode获得状态码
                    InputStream inputStream=urlConnection.getInputStream();
                    //connection.getInputStream()  只是得到一个流对象，并不是数据。
                    // 不过我们可以从流中读出数据，从流中读取数据的操作必须放在子线程。
                    //从这个流对象中只能读取一次数据，第二次读取时将会得到空数据。

                    //连接网络并获取图片，通过urlConnection的getContentLength()获取下载图片的总大小
                    // 通过inputStream.read(buffer)和while循环读取数据并通过Byt eArrayOutputStream将数据写入，
                    // 计算数据下载的完成百分百，并每隔50ms通过publishProgress()发布进度值;
                    // 数据读取完成后通过BitmapFactory. decodeByt eArray()将ByteArrayOutputStream中数据;
                    int maxSize=urlConnection.getContentLength();//文件大小
                    byte[] buffer=new byte[1024*8];
                    int len=-1;
                    int process=0;
                    ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                    while ((len=inputStream.read(buffer)) !=-1){
                        process+=len;
                        outputStream.write(buffer,0,len);
                        int pbValue=(int)(((double)process/maxSize)*pb2.getMax());
                        System.out.println(maxSize);
                        System.out.println(process);
                        System.out.println(pbValue);
                        publishProgress(pbValue);
                        Thread.sleep(50);
                        publishProgress(pbValue);
                    }
                    bitmap= BitmapFactory.decodeByteArray(outputStream.toByteArray(),0,maxSize);
                    return bitmap;

                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                return bitmap;
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //下载过程中更新TextView和进度条的进度值
            tv.setText("已获取："+values[0]+"%");
            pb2.setProgress(values[0]);

        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //下载完成后更新TextView和进度条
            tv.setText("获取充值注意事项完成!");
            pb2.setVisibility(View.INVISIBLE);
            ig.setVisibility(View.VISIBLE);
            ig.setImageBitmap(bitmap);

        }
    }
    public void btOk(View v){
        //判空
        if((etmoney.getText().toString()).equals("")){
            Toast.makeText(AddMoneyActivity.this, "请输入充值金额！", Toast.LENGTH_SHORT).show();
        }else if((etbank.getText().toString()).equals("")){
            Toast.makeText(AddMoneyActivity.this, "请输入充值银行卡号！", Toast.LENGTH_SHORT).show();
        }else if((etpay_pwd.getText().toString()).equals("")){
            Toast.makeText(AddMoneyActivity.this, "请输入支付密码！", Toast.LENGTH_SHORT).show();
        }else{
            float money=Float.parseFloat(etmoney.getText().toString());
            String card=etbank.getText().toString();
            if(money>0) {
                //连接数据库
                DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
                Cursor c = dbHelper.querykb2(sfz,card );
                if (c.getCount() !=0) {
                    c.moveToFirst();
                    //Log.v("chengg", "没问题");
                    //Log.v("chengg", c.getString(1));

                    DatebaseHelper dbHelper1 = new DatebaseHelper(getApplicationContext());
                    Cursor c1 = dbHelper1.querypwd(sfz);
                    if (c1.getCount() !=0) {
                        c1.moveToFirst();
                        String  paypwd=c1.getString(0);

                        if(etpay_pwd.getText().toString().equals(paypwd)){

                            //更新余额
                            float money2 = money + moneys;
                            ContentValues values = new ContentValues();
                            values.put("money", money2);
                            DatebaseHelper dbHelper2 = new DatebaseHelper(getApplicationContext());
                            dbHelper2.updateuser(values, sfz);
                            //插入记录
                            DatebaseHelper dbHelperRecord = new DatebaseHelper(getApplicationContext());
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String time=sdf.format(new java.util.Date());
                            final ContentValues valueRecord=new ContentValues();//存储值
                            valueRecord.put("_id",intent.getStringExtra("id"));
                            valueRecord.put("r_name","自己");
                            valueRecord.put("r_mode","充值");
                            valueRecord.put("r_money",money2);
                            valueRecord.put("r_time",time);
                            dbHelperRecord.insertRecord(valueRecord);

                            Toast.makeText(AddMoneyActivity.this, "充值成功！", Toast.LENGTH_SHORT).show();
                            finish();
                            intent.putExtra("moneys", money2);
                            intent.setClass(AddMoneyActivity.this,MoneyActivity.class);
                            startActivity(intent);


                        }else {
                            etpay_pwd.setText("");
                            Toast.makeText(AddMoneyActivity.this, "支付密码有误！", Toast.LENGTH_SHORT).show();
                        } }
                } else {
                    etbank.setText("");
                    Toast.makeText(AddMoneyActivity.this, "银行卡号有误或不存在！", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                etmoney.setText("");
                Toast.makeText(AddMoneyActivity.this, "输入金额有误！", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
