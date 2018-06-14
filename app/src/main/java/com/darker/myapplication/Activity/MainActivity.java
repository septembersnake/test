package com.darker.myapplication.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.darker.myapplication.R;
import com.darker.myapplication.Structure.FragmentTag;
import com.darker.myapplication.fragment.HomeFragment;
import com.darker.myapplication.fragment.MaintenanceAddFragment;
import com.darker.myapplication.fragment.MaintenanceFragment;
import com.darker.myapplication.fragment.MapFragment;
import com.darker.myapplication.fragment.SettingFragment;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends FragmentActivity implements View.OnClickListener  {
    private TextView textView_title,textView_email;
    private ImageView imageView_account,imageView_ic_chart,imageView_home, imageView_service,imageView_gps, imageView_help,imageView_setting;
    private long clickTime = 0;

    private HomeFragment homeFragment;
    private MaintenanceFragment maintenanceFragment;
    private MapFragment mapFragment;
    private SettingFragment settingFragment;
    private MaintenanceAddFragment maintenanceAddFragment;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findById();
        sharedPreferences = getSharedPreferences("GTCLOUD_Content", MODE_PRIVATE);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_main_content, homeFragment = new HomeFragment(), FragmentTag.HOME_TAG)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_main_content, maintenanceFragment = new MaintenanceFragment(), FragmentTag.MAINTENANCE_TAG)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_main_content, mapFragment = new MapFragment(), FragmentTag.MAP_TAG)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_main_content, settingFragment = new SettingFragment(),FragmentTag.SETTING_TAG)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_main_content, maintenanceAddFragment = new MaintenanceAddFragment(),FragmentTag.MAINTENANCEADD_TAG)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .show(homeFragment).hide(maintenanceFragment).hide(mapFragment).hide(settingFragment).hide(maintenanceAddFragment)
                .commit();

        checkPermission();
    }
    private void findById() {
        textView_title = findViewById(R.id.textView_title);
        textView_email = findViewById(R.id.textView_email);
        textView_email.setText(getIntent().getStringExtra("Email"));
        imageView_account = findViewById(R.id.imageView_account);
        imageView_account.setOnClickListener(listener);
        imageView_home = this.findViewById(R.id.imageView_home);
        imageView_home.setOnClickListener(this);
        imageView_service = this.findViewById(R.id.imageView_service);
        imageView_service.setOnClickListener(this);
        imageView_gps = this.findViewById(R.id.imageView_gps);
        imageView_gps.setOnClickListener(this);
        imageView_help = this.findViewById(R.id.imageView_help);
        imageView_help.setOnClickListener(this);
        imageView_setting = this.findViewById(R.id.imageView_setting);
        imageView_setting.setOnClickListener(this);
        imageView_ic_chart = findViewById(R.id.imageView_ic_chart);

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated methodstub
        switch (view.getId()) {
            case R.id.imageView_home:
                getSupportFragmentManager().beginTransaction()
                        .show(homeFragment).hide(maintenanceFragment).hide(mapFragment).hide(settingFragment).hide(maintenanceAddFragment)
                        .commit();
                textView_title.setText("首頁");
                imageView_ic_chart.setVisibility(View.VISIBLE);
                break;
            case R.id.imageView_service:
                if(sharedPreferences.getString("userid","").equals("")){
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("請登入")
                            .setContentText("需登入才能使用保養功能")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent = new Intent();
                                    intent.setClass(MainActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                }else{
                    getSupportFragmentManager().beginTransaction()
                            .show(maintenanceFragment).hide(homeFragment).hide(mapFragment).hide(settingFragment).hide(maintenanceAddFragment)
                            .commit();
                    textView_title.setText("保養");
                    imageView_ic_chart.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.imageView_gps:
                getSupportFragmentManager().beginTransaction()
                        .show(mapFragment).hide(homeFragment).hide(maintenanceFragment).hide(settingFragment).hide(maintenanceAddFragment)
                        .commit();
                textView_title.setText("位置服務");
                imageView_ic_chart.setVisibility(View.INVISIBLE);

                break;
            case R.id.imageView_setting:
                getSupportFragmentManager().beginTransaction()
                        .show(settingFragment).hide(homeFragment).hide(mapFragment).hide(maintenanceFragment).hide(maintenanceAddFragment)
                        .commit();
                textView_title.setText("設定");
                imageView_ic_chart.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    private Button.OnClickListener listener = new Button.OnClickListener(){
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //尚未登入
            String id = sharedPreferences.getString("userid","");
            if(id.equals("")){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }else{ //登入過
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("登出提醒")
                        .setContentText("是否要登出?")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sharedPreferences.edit().clear().commit();
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this,LoginActivity.class);
                                startActivity(intent);
                                sDialog.dismissWithAnimation();
                            }
                        }).show();
            }
        }};


    public void chageFragment(String string){
        if(string.equals("首頁")){
            imageView_ic_chart.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .show(homeFragment).hide(maintenanceFragment).hide(mapFragment).hide(settingFragment).hide(maintenanceAddFragment)
                    .commit();
            textView_title.setText("首頁");
        }else if(string.equals("保養")){
            imageView_ic_chart.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .show(maintenanceFragment).hide(homeFragment).hide(mapFragment).hide(settingFragment).hide(maintenanceAddFragment)
                    .commit();
            textView_title.setText("保養");
        }else if(string.equals("位置服務")){
            imageView_ic_chart.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .show(mapFragment).hide(homeFragment).hide(maintenanceFragment).hide(settingFragment).hide(maintenanceAddFragment)
                    .commit();
            textView_title.setText("位置服務");
        }else if(string.equals("設定")) {
            imageView_ic_chart.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .show(settingFragment).hide(homeFragment).hide(mapFragment).hide(maintenanceFragment).hide(maintenanceAddFragment)
                    .commit();
            textView_title.setText("設定");
        }

        else if(string.equals("新增保養")) {
            imageView_ic_chart.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .show(maintenanceAddFragment).hide(homeFragment).hide(mapFragment).hide(maintenanceFragment).hide(settingFragment)
                    .commit();
            textView_title.setText("保養");
        }
    }
    //打電話權限檢查
    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 10);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 是否触发按键为back键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else { // 如果不是back键正常响应
            return super.onKeyDown(keyCode, event);
        }
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(this, "再按一次退出 無限騎機", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
    }
}