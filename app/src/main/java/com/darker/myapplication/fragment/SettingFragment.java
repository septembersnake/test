package com.darker.myapplication.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.darker.myapplication.Maps.Connecter;
import com.darker.myapplication.PollingService;
import com.darker.myapplication.PollingUtils;
import com.darker.myapplication.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Calendar;

public class SettingFragment extends Fragment {
    Button notice;
    Button stole;
    final Connecter connecter  = Connecter.getInstance();
    private KProgressHUD hud;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_setting, null);

        stole=(Button)view.findViewById(R.id.stole);
        notice=(Button)view.findViewById(R.id.notice);
        long time=System.currentTimeMillis();
        final Calendar mCalendar=Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int mYear=mCalendar.get(Calendar.YEAR);
        int mMonth=mCalendar.get(Calendar.MONTH);
        int mDate=mCalendar.get(Calendar.DATE);
        int date=mYear*365+mMonth*30+mDate;
        Log.e("date",date+"");
        SharedPreferences dateSharedPreferences = getActivity().getSharedPreferences("date_", Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor = dateSharedPreferences.edit();
        editor.putString("date",String.valueOf(date));
        editor.commit();
        SharedPreferences iSharedPreferences = getActivity().getSharedPreferences("test",Activity.MODE_PRIVATE);
        String ii = iSharedPreferences.getString("test","");
        Log.e("test",ii+"");

        notice.setOnClickListener(new Button.OnClickListener(){
            public void onClick (View v){
                new AlertDialog.Builder(getActivity()).setTitle("請選擇提醒時間").setSingleChoiceItems(
                        new String[]{"1週","2週","3週","1個月"},0,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.e("i",i+"");
                                SharedPreferences mySharedPreferences= getActivity().getSharedPreferences("data_",Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mySharedPreferences.edit();
                                editor.putString("name",String.valueOf(i));
                                editor.commit();
                                if(i==0) {
                                    //                                    //Start polling service
                                    System.out.println("Start polling service...");
                                    PollingUtils.startPollingService(getActivity(), 5, PollingService.class, PollingService.ACTION);
                                }
                                else if(i==1){
                                    //Start polling service
                                    System.out.println("Start polling service...");
                                    PollingUtils.startPollingService(getActivity(), 1209600, PollingService.class, PollingService.ACTION);
                                }
                                else if(i==2){
                                    //Start polling service
                                    System.out.println("Start polling service...");
                                    PollingUtils.startPollingService(getActivity(), 1814400, PollingService.class, PollingService.ACTION);

                                }
                                else if(i==3){
                                    //Start polling service
                                    System.out.println("Start polling service...");
                                    PollingUtils.startPollingService(getActivity(), 2419200, PollingService.class, PollingService.ACTION);
                                }
                            }
                            public void Onclick(DialogInterface dialog , int which){
                                dialog.dismiss();
                            }
                        }
                ).setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //定義時間格式
                    }
                }).setNegativeButton("取消",null).show();

            }});
        stole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = (RelativeLayout) inflater.inflate(R.layout.dialog_setting,null);
                final EditText editText = view.findViewById(R.id.dialog_input);
//                editText.setText("RAV-0679");
                final Spinner spinner = (Spinner) view.findViewById(R.id.dialog_spinner);
                builder.setTitle("請輸入查詢的種類與車牌")
                        .setView(view)
                        .setPositiveButton("確定",new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog,int id){
                                        if(editText.getText().toString().length()<5){
                                            Toast.makeText(getActivity(),"長度不能小於5",Toast.LENGTH_SHORT).show();
                                        }else{
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    hud = KProgressHUD.create(getActivity())
                                                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                                            .setDimAmount(0.5f)
                                                            .show();
                                                }
                                            });

                                            final String s = editText.getText().toString();
                                            String p = "A";
                                            int postion = spinner.getSelectedItemPosition();
                                            if(postion == 0)
                                                p = "A";
                                            else if(postion == 1)
                                                p = "B";
                                            else if(postion == 2)
                                                p = "C";
                                            else if(postion == 3)
                                                p = "D";

                                            final String finalP = p;
                                            new Thread(new Runnable(){
                                                @Override
                                                public void run() {
                                                    String result = connecter.getJSONContent("http://od.moi.gov.tw/adm/veh/query_veh?_m=query&vehType="+ finalP +"&vehNumber="+s);
                                                    if(result != null){
                                                        if(result.contains("L"))
                                                            showToast("查無失竊紀錄");
                                                        else
                                                            showToast("此為失竊車輛");
                                                    }else
                                                        showToast("伺服器無回應");
                                                    hud.dismiss();
                                                }
                                            }).start();
                                        }
                                    }
                                }
                        ).setNegativeButton("取消",null);
                ArrayAdapter<CharSequence> kind = ArrayAdapter.createFromResource(getActivity(),
                        R.array.kind,
                        android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(kind);
                AlertDialog dialog =builder.create();
                dialog.show();
            }
        });


        return view;

    }

    private void showToast(final String title){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(),title,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
