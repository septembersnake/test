package com.darker.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import com.darker.myapplication.Activity.MainActivity;


public class PollingService extends Service {
    public static final String ACTION = "com.ryantang.service.PollingService";
    private Notification mNotification;
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override

    public void onCreate() {
        Log.e("PollingService","PollingService");
        initNotifiManager();

    }

    @Override

    public void onStart(Intent intent, int startId) {
        new PollingThread().start();

    }

//初始化通知栏配置

    private void initNotifiManager() {

//
//        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
////        int icon = R.drawable.ic_launcher;
//
//        mNotification = new Notification();
////        mNotification.icon = icon;
//
//        mNotification.ticke        mNotification.defaults |= Notification.DEFAULT_SOUND;
//        rText = "New Message";
//        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
//        mNotification.flags = Notification.FLAG_AUTO_CANCEL;

    }

    ////弹出Notification
//
    private void showNotification() {


        Notification.Builder builder = new Notification.Builder(this);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notifyPIntent = PendingIntent.getActivity(getApplicationContext(), 0,notificationIntent, 0);
        builder.setContentIntent(notifyPIntent);

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentTitle("無限騎機");
        builder.setContentText("設置的提醒時間到了，該換機油囉!");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);

        Notification notification = builder.getNotification();
        // 建立震動效果，陣列中元素依序為停止、震動的時間，單位是毫秒
        long[] vibrate_effect =
                {1000, 500, 1000, 400, 1000, 300};
        // 設定震動效果
        builder.setVibrate(vibrate_effect);
        // 設定閃燈效果，參數依序為顏色、打開與關閉時間，單位是毫秒
        builder.setLights(Color.GREEN, 1000, 1000);
        notification.defaults = Notification.DEFAULT_ALL; // 使用默認設置，比如鈴聲、震動、閃燈
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;


        NotificationManager notiManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notiManager.notify(1, builder.build());

    }

    class PollingThread extends Thread {
        @Override
        public void run() {
            showNotification();
        }
    }

    @Override

    public void onDestroy() {
        super.onDestroy();
        System.out.println("Service:onDestroy");
    }

}
