package com.example.phonewallet11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MyBroadcastreceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //获取intent传递过来的收款人信息，并用Toast进行提示
        Toast.makeText(context, intent.getStringExtra("trans"), Toast.LENGTH_LONG).show();

        //实现Notification的推送
        NotificationManager manager=(NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        builder=new NotificationCompat.Builder(context,"channell");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher));
        builder.setContentTitle("转账通知");
        builder.setContentText(intent.getStringExtra("trans"));
        Intent intent1=new Intent(Settings.ACTION_SETTINGS);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,1,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setNumber(1);
        // builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher));
        //Intent intent1=new Intent(Settings.ACTION_SETTINGS);
        //PendingIntent pendingIntent=PendingIntent.getActivity(context,1,intent1,PendingIntent.FLAG_CANCEL_CURRENT)
        //builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        Notification notification=builder.build();
        manager.notify(1,notification);

    }
}
