package com.darker.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.darker.myapplication.Activity.MainActivity;

import java.util.Calendar;


public class AlarmInitReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context , MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
        //執行一個Activity

        long time=System.currentTimeMillis();
        final Calendar mCalendar=Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int mYear=mCalendar.get(Calendar.YEAR);
        int mMonth=mCalendar.get(Calendar.MONTH);
        int mDate=mCalendar.get(Calendar.DATE);
        String reboot=mYear+"/ "+mMonth+"/ "+mDate;
        Log.e("reboot",reboot+"");
        SharedPreferences dateSharedPreferences = context.getSharedPreferences("date_", Activity.MODE_PRIVATE);
        String date=dateSharedPreferences.getString("date","");
//        int i = Integer.parseInt(date);
//        int j = Integer.parseInt(reboot);
        Calendar c = Calendar.getInstance();

//        取得系統日期：
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour=c.get(Calendar.HOUR);
        int minute=c.get(Calendar.MINUTE);

        int j = year*365*24*60+month*30*24*60+day*24*60+hour*60+minute;

        //定義時間格式
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//
//        //取的兩個時間
//        Date dt1 = null ,dt2 = null;
//        try {
//            dt1 = sdf.parse(date);
//            dt2 =sdf.parse(reboot);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        //取得兩個時間的Unix時間
//        Long ut1=dt1.getTime();
//        Long ut2=dt2.getTime();
//        Long timeP=ut2-ut1;//毫秒差
//        Long sec=timeP/1000;//秒差
//        Log.e("date",time+"");
        SharedPreferences mySharedPreferences= context.getSharedPreferences("data_",Activity.MODE_PRIVATE);
        String i = mySharedPreferences.getString("name","");
        int ii = Integer.parseInt(i);
        dateSharedPreferences = context.getSharedPreferences("date_", Activity.MODE_PRIVATE);
        String h = dateSharedPreferences.getString("date","");
        int hh = Integer.parseInt(h);
        long jj=j-hh;
        if (jj*60 >=ii ){
            System.out.println("Start polling service...");
            PollingUtils.startPollingService(context,1,PollingService.class, PollingService.ACTION);
        }
        String strAction = intent.getAction();
        Log.d("tag", "action:" + strAction);
        Bundle bundle0311 = intent.getExtras();
        if(bundle0311!= null){
            String aaa = bundle0311.getString("HELLO");
            Log.d("tag","aaa : "+aaa);

        }
    }





}
