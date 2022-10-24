package com.example.phonewallet11;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class XiaoxiActivity extends Activity {
    private Intent intent;

    public Intent getxinxi(String id) {

        DatebaseHelper dbHelper=new DatebaseHelper(getApplicationContext());
        Cursor c=dbHelper.query(id);
        //数据库回传判断
        if(c.getCount()!=0) {
            c.moveToFirst();
            intent.putExtra("id", c.getString(0));
            intent.putExtra("account", c.getString(1));
            intent.putExtra("name", c.getString(2));
            intent.putExtra("sex", c.getString(3));
            intent.putExtra("phone", c.getString(4));
            intent.putExtra("qx", c.getString(5));
            intent.putExtra("login_password", c.getString(6));
            intent.putExtra("pay_password", c.getString(7));
            // String moneys=String.valueOf(c.getFloat(8));
            intent.putExtra("moneys", c.getFloat(8));

        }else {
            Log.v("数据错误！","");
        }
        return intent;
    }

}
