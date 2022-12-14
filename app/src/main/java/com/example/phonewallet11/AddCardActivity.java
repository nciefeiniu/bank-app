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

import com.example.phonewallet11.api_response.addCard.Data;
import com.example.phonewallet11.api_response.addCard.AddCard;
import com.example.phonewallet11.okhttpClientManager.clientManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AddCardActivity extends Activity {

    private EditText etAddCard, etTel, etId;

    private Button btok;
    private String i, id, AddCard;
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
        setContentView(R.layout.activity_addcard);
        intent = getIntent();
        id = intent.getStringExtra("id");
        etId = (EditText) findViewById(R.id.etId);
        etAddCard = (EditText) findViewById(R.id.etAddCard);
        //etTel=(EditText) findViewById(R.id.etTel);
        btok = (Button) findViewById(R.id.btOk);


        //获取布局中的所有按钮控件
        play = (Button) findViewById(R.id.btnPlay);
        pause = (Button) findViewById(R.id.btnPause);
        stop = (Button) findViewById(R.id.btnStop);
        //获取SufraceView控件对象
        surfaceView = findViewById(R.id.surfaceView);


        //创建播放器对象并设置音频/视频数据源

        player = MediaPlayer.create(this, R.raw.wwbjk);


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


    public void register(View v) {
        AddCard = etAddCard.getText().toString();
        if (!AddCard.equals("")) {


            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("确认增加吗？");
            AlertDialog.Builder builder1 = builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub

                    if (etId.getText().toString().equals(id)) {
                        AddCardActivity.this.addCard(AddCard, id);
//                        final DatebaseHelper dbHelper1 = new DatebaseHelper(getApplicationContext());
//                        Cursor c = dbHelper1.querykbb(AddCard);
//                        c.moveToFirst();
//
//                        if (c.getCount() != 0) {
//                            etAddCard.setText("");
//                            Toast.makeText(AddCardActivity.this, "该银行卡已存在！", Toast.LENGTH_SHORT).show();
//                        } else {
//
//
//                            final DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
//
//                            final ContentValues values = new ContentValues();
//                            values.put("_id", etAddCard.getText().toString());
//                            values.put("k_sfz", id);
//                            //values.put("k_phone", etTel.getText().toString());
//
//                            i = Integer.toString(dbHelper.insertCard(values));
//
//
//                            Log.v("增加bankscard", i);
//
//                            Toast.makeText(AddCardActivity.this, "银行卡添加成功！！", Toast.LENGTH_SHORT).show();
//                            setResult(1, intent);
//                            finish();
//                        }
                    } else {

                        Toast.makeText(AddCardActivity.this, "身份证号错误，请重新输入！", Toast.LENGTH_SHORT).show();
                    }


                }

            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    etAddCard.setText("");
                    etTel.setText("");
                }

            });
            builder.create().show();
        } else {
            Toast.makeText(AddCardActivity.this, "请输入银行卡号！", Toast.LENGTH_SHORT).show();
        }
    }

    public void addCard(String cardNo, String IDNumber) {
        final Gson gson = new Gson();
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("card_no", cardNo);
            json.put("id_number", IDNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("add_card/");

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
                            Toast.makeText(AddCardActivity.this, addCardResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddCardActivity.this, "银行卡添加成功！！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(AddCardActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
