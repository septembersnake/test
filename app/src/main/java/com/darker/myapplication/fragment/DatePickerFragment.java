package com.darker.myapplication.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    int vid;
    TextView editText;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);
        //欲轉換的日期字串
        Bundle bData = getArguments();
        // 記錄下傳進來的是哪個 button 的 id
        vid = bData.getInt("view");
        String str = bData.getString("date");
        final Calendar c = Calendar.getInstance();
//        Date date;
//        if(!str.equals(""))
//        {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            //進行轉換
//            ParsePosition pos = new ParsePosition(0);
//            date = sdf.parse(str, pos);
//            c.setTime(date);
//        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        editText = (TextView) getActivity().findViewById(vid);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    // Do Stuff
//                    editText.clearFocus();
//                    editText.setFocusableInTouchMode(false);

                }
            }
        });
        // 建立 DatePickerDialog instance 並回傳
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        // 注意 月的起始值是 0，所以要加 1
        editText.setText(""+day+"/"+(month+1)+"/"+year);
    }
}
