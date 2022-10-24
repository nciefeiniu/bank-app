package com.example.phonewallet11;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.app.PictureInPictureParams;
import android.util.Rational;
import android.widget.Toast;
import android.widget.MediaController;
import android.widget.VideoView;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

public class paypwdActivity extends AppCompatActivity {
    private Button submit,btnsy2;
    private EditText etid,etpay_password,etpassword;
    private Intent intent;

    private VideoView videoView;//视频视图（视频播放窗口区域）
    private Button btn_start2;
    private Uri uri;//手机上视频文件的URI
    private MediaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypwd);
        intent = getIntent();
        etid= (EditText) findViewById(R.id.id);
        etpay_password= (EditText) findViewById(R.id.pay_password);
        etpassword= (EditText) findViewById(R.id.password);
        submit= (Button) findViewById(R.id.submit);
        btnsy2=(Button) findViewById(R.id.btnsy2);

        videoView=findViewById(R.id.videoview);
        btn_start2=findViewById(R.id.buplay);

        //将VideoView对象与MediaController媒体控制器绑定显示进度条
        controller=new MediaController(this);//创建MediaController媒体控制器对象
        videoView.setMediaController(controller);//为VideoView对象设置关联的媒体控制器对象
        controller.setMediaPlayer(videoView);//为媒体控制器对象设置与之绑定的VideoView视频视图对象

        btn_start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.v04);
                videoView.setVideoURI(uri);//设置视频URI
                videoView.start();//开始播放
            }
        });

                //事件监听
                btnsy2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(1, intent);
                        finish();
                    }
                });
                //事件监听
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String _id = etid.getText().toString();
                        String pay_password = etpay_password.getText().toString();
                        String password = etpassword.getText().toString();
                        String ids = intent.getStringExtra("id");
                        if (ids.equals(_id)) {
                            if (password.equals(pay_password)) {
                                //判断输入两次的密码
                                final ContentValues values = new ContentValues();

                                values.put("pay_password", pay_password);
                                final DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
                                String g = Integer.toString(dbHelper.updatauesr(values, ids));
                                Log.v("debug", g);
                                //如果数据添加失败，返回值是-1
                                if (!g.equals("-1")) {
                                    Toast.makeText(paypwdActivity.this, "修改支付密码成功！", Toast.LENGTH_SHORT).show();
                                    //界面跳转
                                    intent.setClass(paypwdActivity.this, Caidan.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(paypwdActivity.this, "数据错误！", Toast.LENGTH_SHORT).show();
                                    etpassword.setText("");
                                    etpay_password.setText("");
                                }


                            } else {
                                Toast.makeText(paypwdActivity.this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
                                etpay_password.setText("");
                                etpassword.setText("");
                            }
                        } else {
                            Toast.makeText(paypwdActivity.this, "请输入本人正确身份证号", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }



    }


