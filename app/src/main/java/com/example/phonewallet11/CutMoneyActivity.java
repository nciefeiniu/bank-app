package com.example.phonewallet11;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.phonewallet11.okhttpClientManager.clientManager;
import com.example.phonewallet11.recharge.Data;
import com.example.phonewallet11.recharge.Recharge;
import com.example.phonewallet11.withDrawal.WithDrawal;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CutMoneyActivity extends Activity {
    private EditText etmoney,etbank,etpay_pwd;
    private Button btok,fanhui;
    private  String name,sfz;
    private  float moneys;
    private Intent intent;
    //提现注意事项有关组件
    TextView tvsy1;
    Button btnsy1;
    ProgressBar progressBarsy1;
    int pbValuesy1;
    //异步处理
    private HandlerThread handlerThread;
    private Handler threadHandler;
    private Handler uiHandler;
    //音乐播放组件
    private boolean isPause = false;   //是否暂停
    private Button playsy1;
    private Button pausesy1;
    private Button stopsy1;
    private MediaPlayer player;   //定义音乐播放对象

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutmoney);
        intent=getIntent();
        name=intent.getStringExtra("name");
        sfz=intent.getStringExtra("id");

        playsy1 = (Button) findViewById(R.id.btnPlaysy1);
        pausesy1 = (Button) findViewById(R.id.btnPausesy1);
        stopsy1 = (Button) findViewById(R.id.btnStopsy1);
        moneys= intent.getFloatExtra("moneys",0);
        //提现注意事项有关组件
        tvsy1 = findViewById(R.id.tvsy1);
        btnsy1 = findViewById(R.id.btnsy1);
        progressBarsy1 = findViewById(R.id.progressBarsy1);
        handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();

        etmoney=(EditText) findViewById(R.id.money);
        etbank=(EditText) findViewById(R.id.bank);
        etpay_pwd=(EditText) findViewById(R.id.pay_pwd);
        btok=(Button) findViewById(R.id.btOk);
        fanhui=(Button) findViewById(R.id.fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1, intent);
                finish();
            }
        });
        handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        threadHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                //进行耗时操作
                if (msg.what == 0) {    //what,arg1,arg2都是int类型
                    pbValuesy1 = 0;
                    //每100ms将随机产生的进度值通过Message传递给UI线程的Handler对象
                    while (pbValuesy1 < progressBarsy1.getMax()) {
                        pbValuesy1 += (int) (Math.random() * 10);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message = uiHandler.obtainMessage();//获取消息池中的消息对象
                        message.arg1 = pbValuesy1;
                        uiHandler.sendMessage(message);//发送消息
                    }
                }
            }
        };
        uiHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                //进行ui处理操作
                if (msg.arg1 < progressBarsy1.getMax()){
                    tvsy1.setText("正在获取中" + msg.arg1 + "%");
                    progressBarsy1.setProgress(msg.arg1);
                }else{
                    tvsy1.setText("获取提现注意事项完成！");
                    progressBarsy1.setVisibility(View.GONE);
                }

            }
        };
        btnsy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvsy1.setVisibility(View.VISIBLE);
                progressBarsy1.setVisibility(View.VISIBLE);
                progressBarsy1.setProgress(0);

                Message message = threadHandler.obtainMessage();
                threadHandler.sendEmptyMessage(0);
            }
        });

        //创建播放器对象并设置音频/视频数据源
        player = MediaPlayer.create(this, R.raw.m01);
        // 为MediaPlayer对象添加完成事件监听器
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //重新开始播放
                player.start();

            }
        });


        //设置按钮的单击事件监听器
        playsy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*实现MediaPlayer对象的播放，并判断当前是否处于暂停状态，
                若处于暂停状态则将“暂停”按钮文本内容设置为“暂停”并设置暂停标记isPause为false。
                 */
                player.start();
                if (isPause) {
                    pausesy1.setText("暂停");
                    isPause = false;
                }
            }
        });
        pausesy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*判断当前是否正在播放并且没有处于暂停状态，
                若是则暂停播放、设置暂停标记isPause为true并将“暂停”按钮文本内容设置为“继续”；
                否则继续播放、设置暂停标记isPause为false并将“暂停”按钮文本内容设置为“暂停”
                 */
                if (player.isPlaying() && !isPause) {
                    player.pause();
                    isPause = true;
                    ((Button) v).setText("继续");
                } else {
                    player.start();
                    ((Button) v).setText("暂停");
                    isPause = false;
                }
            }
        });
        stopsy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 判断播放器对象是否为null，若是则停止播放、
                并在调用stop后设置再次通过start进行播放前调用prepare函数。
                 */
                if (player != null) {
                    player.stop();
                    try {
                        player.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    @Override
    protected void onDestroy() {
        if(player.isPlaying()){
            player.stop();   //停止音频的播放
        }
        player.release();     //释放资源
        super.onDestroy();
    };
    public void btOk(View v){
        //判空
        if((etmoney.getText().toString()).equals("")){
            Toast.makeText(CutMoneyActivity.this, "请输入提现金额！", Toast.LENGTH_SHORT).show();
        }else if((etbank.getText().toString()).equals("")){
            Toast.makeText(CutMoneyActivity.this, "请输入银行卡号！", Toast.LENGTH_SHORT).show();
        }else if((etpay_pwd.getText().toString()).equals("")){
            Toast.makeText(CutMoneyActivity.this, "请输入支付密码！", Toast.LENGTH_SHORT).show();
        }else{
            double money=Double.parseDouble(etmoney.getText().toString());
            String card=etbank.getText().toString();
            String password = etpay_pwd.getText().toString();
            this.withDrawal(money, password, card);
//            if(money<=moneys) {
//                //连接数据库
//                DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
//                Cursor c = dbHelper.querykb2(sfz,card );
//                if (c.getCount() !=0) {
//                    c.moveToFirst();
//                    //Log.v("chengg", "没问题");
//                    //Log.v("chengg", c.getString(1));
//
//                    DatebaseHelper dbHelper1 = new DatebaseHelper(getApplicationContext());
//                    Cursor c1 = dbHelper1.querypwd(sfz);
//                    if (c1.getCount() !=0) {
//                        c1.moveToFirst();
//                        String  paypwd=c1.getString(0);
//
//                        if(etpay_pwd.getText().toString().equals(paypwd)){
//
//                            //更新余额
//                            float money2 = moneys-money;
//                            ContentValues values = new ContentValues();
//                            values.put("money", money2);
//                            DatebaseHelper dbHelper2 = new DatebaseHelper(getApplicationContext());
//                            dbHelper2.updateuser(values, sfz);
//                            //插入记录
//                            DatebaseHelper dbHelperRecord = new DatebaseHelper(getApplicationContext());
//                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            String time=sdf.format(new java.util.Date());
//                            final ContentValues valueRecord=new ContentValues();//存储值
//                            valueRecord.put("_id",intent.getStringExtra("id"));
//                            valueRecord.put("r_name","自己");
//                            valueRecord.put("r_mode","提现");
//                            valueRecord.put("r_money",money2);
//                            valueRecord.put("r_time",time);
//                            dbHelperRecord.insertRecord(valueRecord);
//
//                            Toast.makeText(CutMoneyActivity.this, "提现成功！", Toast.LENGTH_SHORT).show();
//                            finish();
//                            intent.putExtra("moneys", money2);
//                            intent.setClass(CutMoneyActivity.this,MoneyActivity.class);
//                            startActivity(intent);
//
//
//                        } }else {
//                        etpay_pwd.setText("");
//                        Toast.makeText(CutMoneyActivity.this, "支付密码有误！", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    etbank.setText("");
//                    Toast.makeText(CutMoneyActivity.this, "银行卡号有误或不存在！", Toast.LENGTH_SHORT).show();
//                }
//            }
//            else{
//                etmoney.setText("");
//                Toast.makeText(CutMoneyActivity.this, "余额不足！", Toast.LENGTH_SHORT).show();
//            }
        }

    }

    public void withDrawal(double money, String password, String cardNo) {
        // 提现
        final Gson gson = new Gson();

        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("money", money);
            json.put("pay_password", password);
            json.put("card_no", cardNo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("with_drawal/");
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

        Request request = new Request.Builder().url(url).post(requestBody).build(); // 创建一个请求

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final WithDrawal withDrawalResult = gson.fromJson(response.body().charStream(), WithDrawal.class);
                Integer code = withDrawalResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CutMoneyActivity.this, withDrawalResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CutMoneyActivity.this, "提现成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(CutMoneyActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
