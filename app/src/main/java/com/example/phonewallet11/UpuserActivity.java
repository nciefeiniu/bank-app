package com.example.phonewallet11;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phonewallet11.api_response.addCard.AddCard;
import com.example.phonewallet11.myInfo.Data;
import com.example.phonewallet11.myInfo.MyInfo;
import com.example.phonewallet11.okhttpClientManager.clientManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpuserActivity extends AppCompatActivity {
    private EditText name, account, phone, sex, login_password, password;
    ;
    private RadioGroup rg;
    private RadioButton rb1, rb2;
    private Button submit;
    private Intent intent;

    private boolean isPause = false;   //是否暂停
    private Button play;
    private Button pause;
    private Button stop;
    private MediaPlayer player;   //定义音乐播放对象
    private SurfaceView surfaceView;        //声明SurfaceView对象
    private SurfaceHolder holder;      //声明SurfaceHolder对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upuser);
        intent = getIntent();
        name = (EditText) findViewById(R.id.name);
        account = (EditText) findViewById(R.id.account);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        login_password = (EditText) findViewById(R.id.login_password);
        rg = (RadioGroup) findViewById(R.id.sex);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);

//音乐播放组件
        play = (Button) findViewById(R.id.btnPlay1);
        pause = (Button) findViewById(R.id.btnPause1);
        stop = (Button) findViewById(R.id.btnStop1);
        //获取SufraceView控件对象
        surfaceView = findViewById(R.id.surfaceView);
        this.getMyInfo(); // 获取个人信息
//        name.setText(intent.getStringExtra("name"));
//        account.setText(intent.getStringExtra("account"));
//        phone.setText(intent.getStringExtra("phone"));

        submit = (Button) findViewById(R.id.submit);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, /*@IdRes*/ int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                Toast.makeText(UpuserActivity.this, "选择了：" + rb.getText().toString(), Toast.LENGTH_LONG).show();

            }
        });


        //创建播放器对象并设置音频/视频数据源
        player = MediaPlayer.create(this, R.raw.video);

        // 为MediaPlayer对象添加完成事件监听器
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //重新开始播放
                player.start();

            }
        });
        //设置按钮的单击事件监听器
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取SurfaceView控件的管理器SurfaceHolder
                // 设置SurfaceHolder类型
                // 将SurfaceView控件与MediaPlayer类进行关联
                holder = surfaceView.getHolder();
                holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                holder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

                    }

                    @Override
                    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                    }

                    @Override
                    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

                    }
                });
                player.setDisplay(holder);

                /*实现MediaPlayer对象的播放，并判断当前是否处于暂停状态，
             若处于暂停状态则将“暂停”按钮文本内容设置为“暂停”并设置暂停标记isPause为false。
                 */
                player.start();
                if (isPause) {
                    pause.setText("暂停");
                    isPause = false;
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
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


        stop.setOnClickListener(new View.OnClickListener() {
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


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (login_password.getText().toString().length() < 6) {
                    Toast.makeText(UpuserActivity.this, "密码位数不足！", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    login_password.setText("");
                } else {
                    if (!password.getText().toString().equals(login_password.getText().toString())) {
                        Toast.makeText(UpuserActivity.this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
                        password.setText("");
                        login_password.setText("");
                    } else {


                        RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
                        String xbei = rb.getText().toString();

                        final ContentValues values = new ContentValues();

                        values.put("account", account.getText().toString());
                        values.put("name", name.getText().toString());
                        values.put("sex", xbei);
                        values.put("phone", phone.getText().toString());
                        values.put("login_password", login_password.getText().toString());
                        UpuserActivity.this.changeMyInfo(values.get("account").toString(),
                                values.get("name").toString(), values.get("sex").toString(),
                                values.get("phone").toString(),
                                values.get("login_password").toString());

                    }
                }

            }
        });


    }

    @Override
    protected void onDestroy() {
        if (player.isPlaying()) {
            player.stop();   //停止音频的播放
        }
        player.release();     //释放资源
        super.onDestroy();
    }

    public void changeMyInfo(String account, String name, String sex, String phone, String login_password) {
        final Gson gson = new Gson();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("account", account);
            json.put("name", name);
            json.put("sex", sex);
            json.put("phone", phone);
            json.put("login_password", login_password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("change_my_info/");

        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        Request request = new Request.Builder().url(url).post(requestBody).build(); // 创建一个请求

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final AddCard addCardResult = gson.fromJson(response.body().charStream(), AddCard.class);
                Integer code = addCardResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpuserActivity.this, addCardResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpuserActivity.this, "修改个人信息成功！！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(UpuserActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getMyInfo() {
        final Gson gson = new Gson();

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("get_my_info/");
        Request request = new Request.Builder().url(url).build(); // 创建一个请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final MyInfo myinfoResult = gson.fromJson(response.body().charStream(), MyInfo.class);
                Integer code = myinfoResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpuserActivity.this, myinfoResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (myinfoResult.getSuccess().equals(true)) {
                    final Data resultData = myinfoResult.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            name = (EditText) findViewById(R.id.name);
                            account = (EditText) findViewById(R.id.account);
                            phone = (EditText) findViewById(R.id.phone);
                            rg = (RadioGroup) findViewById(R.id.sex);

                            name.setText(resultData.getName());
                            account.setText(resultData.getAccount());
                            phone.setText(resultData.getPhone());
                            if (resultData.getSex().equals("男")) {
                                rg.check(R.id.rb1);
                            } else {
                                rg.check(R.id.rb2);
                            }
//                            sex.setText(resultData.getSex());
//                            id.setText(resultData.getIdNumber());
                        }
                    });

                } else {
                    Toast.makeText(UpuserActivity.this, "查看个人信息数据出现异常",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(UpuserActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

