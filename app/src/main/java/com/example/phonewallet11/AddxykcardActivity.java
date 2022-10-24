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
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;


class AddxykCardActivity extends Activity {

    private EditText etAddCard9,etTel,etId;

    private Button btok;
    private  String  i,id,AddCard;
    private Intent intent;

    private boolean isPause = false;   //是否暂停

    private Button play;
    private Button pause;
    private Button stop;
    private MediaPlayer player;   //定义音乐播放对象
    private SurfaceView surfaceView;        //声明SurfaceView对象
    private SurfaceHolder holder;      //声明SurfaceHolder对象

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addxykcard);
        intent=getIntent();
        id=intent.getStringExtra("id");
        etId=(EditText) findViewById(R.id.etId);
        etAddCard9=(EditText) findViewById(R.id.etAddCard9);
        //etTel=(EditText) findViewById(R.id.etTel);
        btok=(Button) findViewById(R.id.btOk);


        //获取布局中的所有按钮控件
        play = (Button) findViewById(R.id.btnPlay);
        pause = (Button) findViewById(R.id.btnPause);
        stop = (Button) findViewById(R.id.btnStop);
        //获取SufraceView控件对象
        surfaceView = findViewById(R.id.surfaceView);




        //创建播放器对象并设置音频/视频数据源

        player=MediaPlayer.create(this,R.raw.wwbjk);


        // 为MediaPlayer对象添加完成事件监听器
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener()

        {
            @Override
            public void onCompletion (MediaPlayer mp){
                //重新开始播放
                player.start();

            }
        });
        //设置按钮的单击事件监听器
        play.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
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

        pause.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
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


        stop.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
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
    }












    public void register(View v){
        AddCard=etAddCard9.getText().toString();
        if(!AddCard.equals("")){



            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("确认增加吗？");
            AlertDialog.Builder builder1 = builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub

                    if (etId.getText().toString().equals(id)){
                        final DatebaseHelper dbHelper1 = new DatebaseHelper(getApplicationContext());
                        Cursor c = dbHelper1.querykbb(AddCard);
                        c.moveToFirst();

                        if (c.getCount()!=0){
                            etAddCard9.setText("");
                            Toast.makeText(AddxykCardActivity.this, "该卡已存在！", Toast.LENGTH_SHORT).show();
                        }else {


                            final DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());

                            final ContentValues values = new ContentValues();
                            values.put("_id", etAddCard9.getText().toString());
                            values.put("k_sfz", id);
                            //values.put("k_phone", etTel.getText().toString());

                            i = Integer.toString(dbHelper.insertCard(values));


                            Log.v("增加bankscard", i);

                            Toast.makeText(AddxykCardActivity.this, "信用卡添加成功！！", Toast.LENGTH_SHORT).show();
                            setResult(1, intent);
                            finish();
                        }
                    } else {

                        Toast.makeText(AddxykCardActivity.this, "身份证号错误，请重新输入！", Toast.LENGTH_SHORT).show();
                    }


                }

            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){

                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    etAddCard9.setText("");
                    etTel.setText("");
                }

            });
            builder.create().show();
        }else{
            Toast.makeText(AddxykCardActivity.this, "请输入信用卡号！", Toast.LENGTH_SHORT).show();
        }
    }


}
