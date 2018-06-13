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

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SuppliesFragment extends Fragment implements View.OnClickListener {
    private  SharedPreferences sharedPreferences;
    private ArrayList<Button> arrayList = new ArrayList<>();
    int[] ids = {R.id.button1,R.id.button2,R.id.button3,R.id.button4,R.id.button5
            ,R.id.button6,R.id.button7,R.id.button8,R.id.button9,R.id.button10};

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supplies, null);
        sharedPreferences = getActivity().getSharedPreferences("GTCLOUD_Content", MODE_PRIVATE);
        for (int i = 0 ; i <ids.length;i++){
            arrayList.add((Button) view.findViewById(ids[i]));
            arrayList.get(i).setOnClickListener(this);
        }
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        for (int i = 0 ; i<arrayList.size();i++){
            if(v.getId() == ids[i])
                sharedPreferences.edit().putString("score" , arrayList.get(i).getText().toString()).commit();
        }

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content,new InformationFragment())
                .commit();
    }
}
