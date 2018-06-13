package com.darker.myapplication.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.darker.myapplication.Activity.MainActivity;
import com.darker.myapplication.R;
import com.darker.myapplication.Structure.MaintainStructure;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class InformationFragment extends Fragment {
    String type ;
    String lable;
    int price = 0;
    SharedPreferences sharedPreferences;
    int specification_temp = 0;
    private DatabaseReference mDatabase;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_information, null);
        RelativeLayout relativeLayout =(RelativeLayout)  view.findViewById(R.id.R1);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getActivity().getSharedPreferences("GTCLOUD_Content", MODE_PRIVATE);
        EditText editText =(EditText)  view.findViewById(R.id.editText);
        editText.setText(sharedPreferences.getString("score",""));

        Spinner spinner2 = view.findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lable = parent.getItemAtPosition(position).toString();
                specification_temp = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2.setSelection(sharedPreferences.getInt("specification_temp",0));

        final TextView dateEditText = view.findViewById(R.id.tvDate);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        Button button = view.findViewById(R.id.button_sure);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String date = dateEditText.getText().toString();
                String labele = ((EditText)view.findViewById(R.id.editText)).getText().toString();
//                String labele = ((TextView)view.findViewById(R.id.TextView1)).getText().toString();
                writeNewPost(date,lable,price,
                        ((EditText)view.findViewById(R.id.editText4)).getText().toString(),
                        ((EditText) view.findViewById(R.id.editText)).getText().toString(),
                        sharedPreferences.getString("userid",""));
            }
        });

        return view;
    }

    public void showDatePickerDialog(View v)
    {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bData = new Bundle();
        bData.putInt("view", v.getId());
        TextView button = (TextView) v;
        bData.putString("date", button.getText().toString());
        newFragment.setArguments(bData);
        newFragment.show(((MainActivity)getContext()).getSupportFragmentManager(), "日期挑選器");
    }

    private void writeNewPost(String day, String lable, int price, String trip,String type,String userId) {
        if(day.equals("")) {
            Toast.makeText(getContext(), "請輸入日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if(lable.equals("請選擇廠牌")) {
            Toast.makeText(getContext(), "請選擇廠牌", Toast.LENGTH_SHORT).show();
            return;
        }
        if(price == 0) {
            Toast.makeText(getContext(), "請選擇價位", Toast.LENGTH_SHORT).show();
            return;
        }
        if(trip.equals("")) {
            Toast.makeText(getContext(), "請數入公里數", Toast.LENGTH_SHORT).show();
            return;
        }


        sharedPreferences.edit().putInt("specification_temp",specification_temp).apply();

        String key = mDatabase.child("Warranty").push().getKey();
        MaintainStructure maintainStructure = new MaintainStructure(day, lable, price, trip,type,userId);
        Map<String, Object> postValues = maintainStructure.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Warranty/" + key, postValues);
        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(getContext(), "送出成功", Toast.LENGTH_SHORT).show();
                ((MainActivity)getContext()).chageFragment("保養");
            }
        });

    }
}
