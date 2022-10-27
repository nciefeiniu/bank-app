package com.example.phonewallet11;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phonewallet11.cards.Datum;
import com.example.phonewallet11.cards.GetAllCards;
import com.example.phonewallet11.delCard.DelCard;
import com.example.phonewallet11.okhttpClientManager.clientManager;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

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
        this.getAllCards();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, final long id) {
                final long temp = id;
                //利用对话框实现删除记录的提示操作
                AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this);
                builder.setMessage("确认要删除吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        System.out.println(temp);
                        TextView tv_name = (TextView) view.findViewById(R.id.tvNum);
                        String cardNo = tv_name.getText().toString();
                        CardActivity.this.delCard(cardNo);
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
//        dbHelper.close();


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
                            List<Datum> resultData = cardsResult.getData();
                            String[] array2 = new String[resultData.size()];
                            for (int i=0; i<resultData.size(); i++) {
                                array2[i] = resultData.get(i).getCardNo();
                            }
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(CardActivity.this, R.layout.kblist, R.id.tvNum, array2);
                            list.setAdapter(adapter);
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

    public void delCard(String cardNo){
        final Gson gson = new Gson();

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("del_card/") + "?card_no=" + cardNo;
        Request request = new Request.Builder().url(url).build(); // 创建一个请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final DelCard delResult = gson.fromJson(response.body().charStream(), DelCard.class);
                Integer code = delResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CardActivity.this, delResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (delResult.getSuccess().equals(true)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CardActivity.this, delResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            CardActivity.this.invalidateOptionsMenu();  // 刷新界面
                            CardActivity.this.getAllCards();
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
