package com.darker.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darker.myapplication.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HomeFragment extends Fragment {
    private TextView textView_92,textView_95,textView_98,textView_super;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, null);
        textView_92 = view.findViewById(R.id.textView_92);
        textView_95 = view.findViewById(R.id.textView_95);
        textView_98 = view.findViewById(R.id.textView_98);
        textView_super = view.findViewById(R.id.textView_super);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable(){
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://new.cpc.com.tw/Home/").get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements elements = null;
                if(doc != null) {
                    if (doc.getElementsByClass("clearfix") != null) {
                        elements = doc.getElementsByClass("clearfix").select("dl").select("dd").select("strong");
                    }
                }
                final Elements finalElements = elements;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(finalElements !=null){
                            textView_92.setText("92無鉛 : "+ finalElements.get(2).text());
                            textView_95.setText("95無鉛 : "+ finalElements.get(3).text());
                            textView_98.setText("98無鉛 : "+ finalElements.get(4).text());
                            textView_super.setText("柴油 : "+ finalElements.get(6).text());
                        }
                    }
                });
            }
        }).start();
    }
}
