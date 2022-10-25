package com.example.phonewallet11;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.example.phonewallet11.api_response.checkAccountExists.CheckAccountExists;
import com.example.phonewallet11.api_response.register.Register;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivity extends Activity {
    private EditText account, name, sex, id, phone, login_password, password, pay_password;
    private RadioGroup rg;
    private RadioButton rb1, rb2;
    private Button reg;
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
        setContentView(R.layout.activity_register);
        account = (EditText) findViewById(R.id.account);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        id = (EditText) findViewById(R.id.id);
        login_password = (EditText) findViewById(R.id.login_password);
        password = (EditText) findViewById(R.id.password);
        pay_password = (EditText) findViewById(R.id.pay_password);
        reg = (Button) findViewById(R.id.reg);
        rg = (RadioGroup) findViewById(R.id.sex);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        //获取布局中的所有按钮控件
        play = (Button) findViewById(R.id.btnPlay);
        pause = (Button) findViewById(R.id.btnPause);
        stop = (Button) findViewById(R.id.btnStop);
        //获取SufraceView控件对象
        surfaceView = findViewById(R.id.surfaceView);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, /*@IdRes*/ int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                Toast.makeText(RegisterActivity.this, "选择了：" + rb.getText().toString(), Toast.LENGTH_LONG).show();

            }
        });


        //创建播放器对象并设置音频/视频数据源

        player = MediaPlayer.create(this, R.raw.ysyzc);


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
                    pause.setText("继续");

                } else {
                    player.start();
                    isPause = false;
                    pause.setText("暂停");
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
    }

    @Override
    protected void onDestroy() {
        if (player.isPlaying()) {
            player.stop();   //停止音频的播放
        }
        player.release();     //释放资源
        super.onDestroy();
    }

    public void checkUserExists() {
        final Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient(); // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("check_account_exists");
        Request request = new Request.Builder().url(url).build(); // 创建一个请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                CheckAccountExists accountExists = gson.fromJson(response.body().charStream(), CheckAccountExists.class);

                if (!accountExists.getSuccess().equals(true)) {
                    Toast.makeText(RegisterActivity.this, accountExists.getMessage(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (accountExists.getData().getExists().equals(true)) {
                        Toast.makeText(RegisterActivity.this, "账号已存在",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendRegisterHandle(ContentValues values) {
        final Gson gson = new Gson();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject json = new JSONObject();

        for (Map.Entry<String, Object> item : values.valueSet()) {
            try {
                json.put(item.getKey(), item.getValue().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        OkHttpClient client = new OkHttpClient(); // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("register/");

        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        Request request = new Request.Builder().url(url).post(requestBody).build(); // 创建一个请求

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Register registerResult = gson.fromJson(response.body().charStream(), Register.class);
                Integer code = registerResult.getCode();
                if (code.equals(501)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "数据提交不全",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (code.equals(601)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "账号已存在",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (code.equals(602)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "该身份证号码已注册过账号！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if (registerResult.getSuccess().equals(true)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("注册成功，请登录！");
                            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //返回登录界面
                                    Intent intent = getIntent();
                                    setResult(1, intent);
                                    finish();
                                }
                            });
                            builder.create().show();


                        }
                    });

                } else {
                    Toast.makeText(RegisterActivity.this, "注册失败，",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void register(View v) {

        if (name.getText().toString().equals("") || (account.getText().toString()).equals("") || login_password.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "请先填写资料！", Toast.LENGTH_SHORT).show();

        } else {
            String ac = account.getText().toString();
//            DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
//            Cursor c = dbHelper.loginquery(ac);

            if (-1 > 0) {
                Toast.makeText(RegisterActivity.this, "账号已存在！", Toast.LENGTH_SHORT).show();
                account.setText("");
            } else {
                int len = id.getText().toString().length();
                if (account.getText().length() < 6) {
                    Toast.makeText(RegisterActivity.this, "账号至少为6位字符", Toast.LENGTH_SHORT).show();
                } else if (account.getText().length() > 10) {
                    Toast.makeText(RegisterActivity.this, "账号长度过长", Toast.LENGTH_SHORT).show();
                } else if (len < 18) {//正常 !=18  测试 >=18
                    Toast.makeText(RegisterActivity.this, "请输入18位身份证号码", Toast.LENGTH_SHORT).show();
                } else if (login_password.getText().toString().length() < 6) {
                    Toast.makeText(RegisterActivity.this, "密码位数不足！", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    login_password.setText("");
                } else {
                    if (!password.getText().toString().equals(login_password.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
                        password.setText("");
                        login_password.setText("");
                    } else {
                        RadioButton rb = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
                        String xbei = rb.getText().toString();

                        final ContentValues values = new ContentValues();
                        values.put("_id", id.getText().toString());
                        values.put("account", account.getText().toString());
                        values.put("name", name.getText().toString());
                        values.put("sex", xbei);
                        values.put("phone", phone.getText().toString());
                        values.put("login_password", login_password.getText().toString());
                        values.put("pay_password", pay_password.getText().toString());
                        values.put("qx", "qwe");
                        values.put("money", "0");

                        this.sendRegisterHandle(values);

//                        final DatebaseHelper dbHelper1 = new DatebaseHelper(getApplicationContext());
//                        String g = Integer.toString(dbHelper1.insert(values));
//                        Log.v("debug", g);
                        //如果数据添加失败，返回值是-1
//                        if (!g.equals("-1")) {
//                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                            builder.setMessage("注册成功，请登录！");
//                            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //返回登录界面
//                                    Intent intent = getIntent();
//                                    setResult(1, intent);
//                                    finish();
//                                }
//                            });
//                            builder.create().show();
//
//                        } else {
//                            Toast.makeText(RegisterActivity.this, "您已申请过账号！", Toast.LENGTH_SHORT).show();
//                            account.setText("");
//                            id.setText("");
//                        }
                    }

                }

            }
        }
    }


}
