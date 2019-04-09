package com.everday.foregroundservice.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.everday.foregroundservice.R;

/**
* @author Everday
* @emil wangtaohandsome@gmail.com
* create at 2019/3/15
* description: 保活进程
*/

public class ClientService extends Service {
    private static final int SERVICE_ID = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(Build.VERSION.SDK_INT  < 18){
            //4.3
            startForeground(SERVICE_ID,new Notification());
        }else if(Build.VERSION.SDK_INT < 26){
            //7.0
            startForeground(SERVICE_ID,new Notification());
//            stopForeground(true);
//            stopSelf();
            //删除通知栏消息
            startService(new Intent(this,InnerService.class));
        }else{
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //越小，通知重要性越低
            NotificationChannel channel = new NotificationChannel("channel","channel",NotificationManager.IMPORTANCE_MIN);
            if(null!=manager){
                manager.createNotificationChannel(channel);
                Notification notification = new NotificationCompat.Builder(this,"channel")
                        .setSmallIcon(R.mipmap.ic)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic))
                        .build();
                startForeground(SERVICE_ID,notification);

            }
        }

        return super.onStartCommand(intent, flags, startId);
    }


    public static class InnerService extends Service{
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(SERVICE_ID,new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
    }
}
