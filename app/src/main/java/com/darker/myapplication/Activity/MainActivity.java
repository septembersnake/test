package com.darker.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.darker.myapplication.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView textView_92, textView_95, textView_98, textView_super;
    private Button btn_user,btn1;
    private long clickTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_92 = findViewById(R.id.textView_92);
        textView_95 = findViewById(R.id.textView_95);
        textView_98 = findViewById(R.id.textView_98);
        textView_super = findViewById(R.id.textView_super);
        btn_user = findViewById(R.id.btn_user);
        btn_user.setOnClickListener(listener);
        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(listener1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://new.cpc.com.tw/Home/").get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements elements = null;
                if (doc != null) {
                    if (doc.getElementsByClass("clearfix") != null) {
                        elements = doc.getElementsByClass("clearfix").select("dl").select("dd").select("strong");
                    }
                }
                final Elements finalElements = elements;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalElements != null) {
                            textView_92.setText("92無鉛 : " + finalElements.get(2).text());
                            textView_95.setText("95無鉛 : " + finalElements.get(3).text());
                            textView_98.setText("98無鉛 : " + finalElements.get(4).text());
                            textView_super.setText("柴油 : " + finalElements.get(6).text());
                        }
                    }
                });
            }
        }).start();
    }

    private Button.OnClickListener listener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };

    private Button.OnClickListener listener1 = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ItemActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        exit();
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
