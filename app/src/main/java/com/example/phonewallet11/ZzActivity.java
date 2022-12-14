package com.example.phonewallet11;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.phonewallet11.okhttpClientManager.clientManager;
import com.example.phonewallet11.rechargePhone.RechargePhone;
import com.example.phonewallet11.transferAccounts.TransferAccounts;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ZzActivity extends AppCompatActivity {
    private Intent intent;
    private EditText name,phone,money,pwd;
    private Float moneys;
    private Button btnzyj6,btnzyj7;
    private  String ppwd,id;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zz);
        intent = getIntent();
        moneys= intent.getFloatExtra("moneys",0);
        ppwd=intent.getStringExtra("pay_password");
        id=intent.getStringExtra("id");

        name = (EditText) findViewById(R.id.etName);
        phone = (EditText) findViewById(R.id.etPhone);
        money = (EditText) findViewById(R.id.etMoney);
        pwd = (EditText) findViewById(R.id.etPwd);
        btnzyj6 = (Button) findViewById(R.id.btnzyj6);
        btnzyj7 = (Button) findViewById(R.id.btnzyj7);

        //??????????????????

        btnzyj6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ZzActivity.this, ZzjiluActivity.class);
                startActivity(intent);
            }
        });
        btnzyj7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZzActivity.this.save(view);
//                Toast.makeText(ZzActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void save(View v){
        //??????
        if((name.getText().toString()).equals("")){
            Toast.makeText(ZzActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
        }else if((phone.getText().toString()).equals("")){
            Toast.makeText(ZzActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
        }else if((money.getText().toString()).equals("")){
            Toast.makeText(ZzActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
        }else{

            double money1=Double.parseDouble(money.getText().toString());



            this.transferAccounts(money1, name.getText().toString(), phone.getText().toString(), pwd.getText().toString());
//            if(money1<=moneys) {
//                //???????????????
//                DatebaseHelper dbHelper = new DatebaseHelper(getApplicationContext());
//
//                Cursor c = dbHelper.queryzz(phone.getText().toString(),name.getText().toString() );
//                String r_name=name.getText().toString();
//                if (c.getCount() !=0) {
//                    c.moveToFirst();
//                    if (ppwd.equals(pwd.getText().toString())){
//                        DatebaseHelper dbHelper1 = new DatebaseHelper(getApplicationContext());
//                        ContentValues values = new ContentValues();
//                        values.put("money",moneys-money1 );
//                        dbHelper1.updateuser(values,id);
//                        finish();
//                        //????????????
//                        DatebaseHelper dbHelperRecord = new DatebaseHelper(getApplicationContext());
//                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String time=sdf.format(new java.util.Date());
//                        final ContentValues value=new ContentValues();//?????????
//                        value.put("_id",intent.getStringExtra("id"));
//                        value.put("r_name",r_name);
//                        value.put("r_mode","??????");
//                        value.put("r_money",money1);
//                        value.put("r_time",time);
//                        dbHelperRecord.insertRecord(value);
//
//                        Toast.makeText(ZzActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
//                        ContentValues values1 = new ContentValues();
//                        values1.put("money",c.getFloat(8)+money1 );
//                        dbHelper1.updateuser(values1,c.getString(0));
//
//
//                        //????????????
//                        DatebaseHelper dbHelperRecord2 = new DatebaseHelper(getApplicationContext());
//                        final ContentValues value2=new ContentValues();//?????????
//                        value2.put("_id",c.getString(0));
//                        value2.put("r_name",intent.getStringExtra("name"));
//                        value2.put("r_mode","??????");
//                        value2.put("r_money", money1);
//                        value2.put("r_time",time);
//                        dbHelperRecord2.insertRecord(value2);
//
//
//                        intent.putExtra("moneys", moneys-money1);
//                        intent.setClass(ZzActivity.this,FriendActivity.class);
//                        startActivity(intent);
//
//                    }else {
//                        pwd.setText("");
//                        Toast.makeText(ZzActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
//                    }
//
//                }else {
//                    Toast.makeText(ZzActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
//                }
//
//            }else{
//                money.setText("");
//                System.out.println(money1);
//                System.out.println(moneys);
//                Toast.makeText(ZzActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
//            }
        }

    }

    public void transferAccounts(final double money, String name, String phone, String password) {
        final Gson gson = new Gson();

        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("money", money);
            json.put("pay_password", password);
            json.put("name", name);
            json.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // ??????OkHttpClient??????
        String url = new ApiBaseUrl().assemblyUrl("transfer_accounts/");
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

        Request request = new Request.Builder().url(url).post(requestBody).build(); // ??????????????????

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final TransferAccounts transferAccountsResult = gson.fromJson(response.body().charStream(), TransferAccounts.class);
                Integer code = transferAccountsResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ZzActivity.this, transferAccountsResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ZzActivity.this, "??????????????????????????????" + money + "???", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(ZzActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}