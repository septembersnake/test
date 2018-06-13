package com.darker.myapplication.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.darker.myapplication.R;
import com.darker.myapplication.Structure.FragmentTag;

import static android.content.Context.MODE_PRIVATE;

public class OilFragment extends Fragment implements View.OnClickListener {
    private Button button1,button2,button3,button4;
    private  SharedPreferences sharedPreferences;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oil, null);
        sharedPreferences = getActivity().getSharedPreferences("GTCLOUD_Content", MODE_PRIVATE);

        button1 = view.findViewById(R.id.button1_oil);
        button2 = view.findViewById(R.id.button2_oil);
        button3 = view.findViewById(R.id.button3_oil);
        button4 = view.findViewById(R.id.button4_oil);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1_oil :
                sharedPreferences.edit().putString("score" , button1.getText().toString()).commit();
                break;
            case R.id.button2_oil :
                sharedPreferences.edit().putString("score" , button2.getText().toString()).commit();
                break;
            case R.id.button3_oil :
                sharedPreferences.edit().putString("score" , button3.getText().toString()).commit();
                break;
            case R.id.button4_oil :
                sharedPreferences.edit().putString("score" , button4.getText().toString()).commit();
                break;
        }

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content,new InformationFragment(), FragmentTag.INFORMATION_TAG)
                .commit();
    }
}
