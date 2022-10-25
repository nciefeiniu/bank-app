package com.example.phonewallet11;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phonewallet11.api_response.login.Data;
import com.example.phonewallet11.api_response.login.Login;
import com.example.phonewallet11.cards.Datum;
import com.example.phonewallet11.cards.GetAllCards;
import com.example.phonewallet11.okhttpClientManager.clientManager;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CardActivity extends AppCompatActivity {
    private TextView tv1;
    private ListView list;
    private Intent intent ;
    private  String sfz;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        intent=getIntent();
        sfz=intent.getStringExtra("id");
        tv1 = (TextView) findViewById(R.id.textView2);
        tv1.setText(intent.getStringExtra("name"));

        list = (ListView) findViewById(R.id.listView1);
        getRelationFromDB();
    }

    private void getRelationFromDB() {
        // TODO Auto-generated method stub
        final DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
        Cursor c = dbHelper.querykb(sfz);
        //将查询到的联系人信息按照relationlist.xml布局方式显示到ListView中
        String[] from = new String[]{"_id","k_phone"};  // _id 就是银行账号
        int[] to = new int[]{R.id.tvNum, R.id.tvPhone};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.kblist, c, from, to);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {



                final long temp = id;
                //利用对话框实现删除记录的提示操作
                AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                builder.setMessage("确认要删除吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        dbHelper.delkb((int)temp);
                        Cursor c = dbHelper.querykb(sfz);
                        String[] from = new String[]{"_id", "k_phone"};
                        int[] to = new int[]{R.id.tvNum, R.id.tvPhone};
                        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.kblist, c, from, to);
                        list.setAdapter(adapter);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });
        dbHelper.close();


    }
    public void add(View view){
        //通过Intent对象实现带返回结果的界面跳转
        intent.setClass(getApplicationContext(), AddCardActivity.class);
        startActivityForResult(intent,1);
    }

    public void getAllCards() {
        final Gson gson = new Gson();

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("get_all_card/");
        Request request = new Request.Builder().url(url).build(); // 创建一个请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final GetAllCards cardsResult = gson.fromJson(response.body().charStream(), GetAllCards.class);
                Integer code = cardsResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CardActivity.this, cardsResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (cardsResult.getSuccess().equals(true)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < cardsResult.getData().size(); i++) {

                            }
                        }
                    });

                } else {
                    Toast.makeText(CardActivity.this, "注册失败，",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(CardActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //调用getRelationFromDB()方法
        if (requestCode == resultCode) {
            getRelationFromDB();
        }
    }

}
