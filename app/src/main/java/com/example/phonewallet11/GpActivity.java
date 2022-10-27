package com.example.phonewallet11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phonewallet11.BuyStock.BuyStock;
import com.example.phonewallet11.okhttpClientManager.clientManager;
import com.example.phonewallet11.rechargePhone.RechargePhone;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GpActivity extends AppCompatActivity {
    private TextView tvzyj1;
    private TextView tvzyj2;
    private TextView tvzyj3;
    private TextView tvzyj4;
    private TextView tvzyj5;
    private TextView tvzyj6;
    private TextView tvzyj7;
    private ImageView ivzyj;
    private Button btnzyj1, btnzyj2, btnzyj3, btnzyj4, btnzyj5;
    private Map<String, String> map;
    private List<Map<String, String>> list;
    private String bianhao, price, name, time, area, gongsi, mujiqi;
    String urlStr = "http://192.168.43.222:8080/TestServer/num.json";

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //获取子线程传递过来的json数据并保存到变量中
            String json = (String) msg.obj;
            List<NumInfo> numInfos = null;

            //调用getInfosFromJson()方法，将天气信息集合保存到weatherInfos中
            try {
                numInfos = getInfosFromJson(json);
            } catch (IOException e) {
                e.printStackTrace();
            }


            //循环读取weatherInfos中的每一条数据
            list = new ArrayList<Map<String, String>>();
            for (NumInfo info : numInfos) {
                map = new HashMap<String, String>();
                map.put("time", info.getTime());
                map.put("bianhao", info.getBianhao());
                map.put("area", info.getArea());
                map.put("name", info.getName());
                map.put("gongsi", info.getGongsi());
                map.put("mujiqi", info.getMujiqi());
                map.put("price", info.getPrice());
                list.add(map);
            }


        }
    };

    //解析json数据返回天气信息的集合
    public static List getInfosFromJson(String json)
            throws IOException {
        ;
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        Type listType = new TypeToken<List<NumInfo>>() {
        }.getType();
        List<NumInfo> list = gson.fromJson(json, listType);

        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gupiao);

        // 初始化控件
        tvzyj1 = (TextView) findViewById(R.id.tvzyj1);
        tvzyj2 = (TextView) findViewById(R.id.tvzyj2);
        tvzyj3 = (TextView) findViewById(R.id.tvzyj3);
        tvzyj4 = (TextView) findViewById(R.id.tvzyj4);
        tvzyj5 = (TextView) findViewById(R.id.tvzyj5);
        tvzyj6 = (TextView) findViewById(R.id.tvzyj6);
        tvzyj7 = (TextView) findViewById(R.id.tvzyj7);
        ivzyj = (ImageView) findViewById(R.id.ivzyj);
        btnzyj1 = (Button) findViewById(R.id.btnzyj1);
        btnzyj2 = (Button) findViewById(R.id.btnzyj2);
        btnzyj3 = (Button) findViewById(R.id.btnzyj3);
        btnzyj4 = (Button) findViewById(R.id.btnzyj4);
        btnzyj5 = (Button) findViewById(R.id.btnzyj5);

        btnzyj5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(GpActivity.this);
                dialog.setTitle("提示框");
                dialog.setMessage("确定购买？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GpActivity.this.buyStock(10, "600519", "afawf");
//                        Toast.makeText(GpActivity.this, "购买成功！", Toast.LENGTH_SHORT).show();

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(GpActivity.this, "购买失败！", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

            }
        });


        btnzyj4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GpActivity.this, GmActivity.class);
                startActivity(intent);
            }
        });
        //通过OkHttp的get同步方式访问服务器，并将获取到的json数据封装到消息中发送给UI线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(urlStr).build();
                Call call = client.newCall(request);
                try {
                    Response response = call.execute();
                    String result = response.body().string();
                    Message message = Message.obtain();
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnzyj1:
                        getMap(list, 0, R.drawable.zyj1);
                        break;
                    case R.id.btnzyj2:
                        getMap(list, 1, R.drawable.zyj2);
                        break;
                    case R.id.btnzyj3:
                        getMap(list, 2, R.drawable.zyj3);
                        break;
                }
            }
        };
        btnzyj1.setOnClickListener(listener);
        btnzyj2.setOnClickListener(listener);
        btnzyj3.setOnClickListener(listener);
    }

    //自定义getMap()方法，显示天气信息到文本控件中
    //将城市天气信息分条展示到界面上
    private void getMap(List list, int number, int iconNumber) {
        Map<String, String> cityMap = (Map<String, String>) list.get(number);
        time = cityMap.get("time");
        price = cityMap.get("price");
        name = cityMap.get("name");
        mujiqi = cityMap.get("mujiqi");
        area = cityMap.get("area");
        gongsi = cityMap.get("gongsi");
        bianhao = cityMap.get("bianhao");
        tvzyj1.setText(bianhao);
        tvzyj2.setText(name);
        tvzyj3.setText(price);
        tvzyj4.setText(area);
        tvzyj5.setText(time);
        tvzyj6.setText(gongsi);
        tvzyj7.setText(mujiqi);
        ivzyj.setImageResource(iconNumber);
    }

    public void buyStock(double money, String stock, String password) {
        // 购买股票
        final Gson gson = new Gson();

        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("money", money);
            json.put("pay_password", password);
            json.put("stock_number", stock);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient client = clientManager.getInstance().mOkHttpClient; // 创建OkHttpClient对象
        String url = new ApiBaseUrl().assemblyUrl("buy_stock/");
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

        Request request = new Request.Builder().url(url).post(requestBody).build(); // 创建一个请求

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                final BuyStock buyStockResult = gson.fromJson(response.body().charStream(), BuyStock.class);
                Integer code = buyStockResult.getCode();
                if (!code.equals(200)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GpActivity.this, buyStockResult.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GpActivity.this, "购买股票成功！ 购买：" + buyStockResult.getData().getPurchaseAmount() + '元', Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(GpActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}