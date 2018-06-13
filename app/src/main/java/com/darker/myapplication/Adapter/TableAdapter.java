package com.darker.myapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.darker.myapplication.Util;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.darker.myapplication.fragment.MaintenanceFragment.key_array;


public class TableAdapter extends BaseAdapter {
    private Context context;
    private List<TableRow> table;

    public ArrayList<ImageView> imageViews = new ArrayList<>();
    public TableAdapter(Context context, ArrayList<TableRow> table) {
        this.context = context;
        this.table = table;
    }

    @Override
    public int getCount() {
        return table.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public TableRow getItem(int position) {
        return table.get(position);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        TableRow tableRow = table.get(position);
        return new TableRowView(this.context, tableRow,position);
    }
    /**
     * TableRowView 实现表格行的样式
     * @author hellogv
     */
    class TableRowView extends LinearLayout {
        public TableRowView(Context context, TableRow tableRow, final int position) {
            super(context);

            this.setOrientation(LinearLayout.HORIZONTAL);
            final Firebase FirebaseRef  = new Firebase("https://motorcycle-cc0fe.firebaseio.com/");
            for (int i = 0; i < tableRow.getSize(); i++) {//逐个格单元添加到行
                TableCell tableCell = tableRow.getCellValue(i);

                if (tableCell.type == TableCell.STRING) {//如果格单元是文本内容
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tableCell.width, LayoutParams.WRAP_CONTENT);//按照格单元指定的大小设置空间
                    layoutParams.setMargins(0, 0, 4, 4);//预留空隙制造边框
                    TextView textCell = new TextView(context);
                    textCell.setLines(1);
                    textCell.setGravity(Gravity.CENTER);
                    textCell.setBackgroundColor(0x00000000);
                    textCell.setText(String.valueOf(tableCell.value));
                    textCell.setTextColor(Color.WHITE);
                    textCell.setPadding(0,30,0,30);
                    addView(textCell, layoutParams);
                } else if (tableCell.type == TableCell.IMAGE) {//如果格单元是图像内容
                    ImageView imgCell = new ImageView(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tableCell.width, Util.getDP(getContext(),30));//按照格单元指定的大小设置空间
                    layoutParams.setMargins(0, Util.getDP(getContext(),5), 4, 4);//预留空隙制造边框
                    imgCell.setImageResource((Integer) tableCell.value);
//                    imgCell.setForegroundGravity(Gravity.CENTER);
                    imgCell.setScaleType(ImageView.ScaleType.FIT_CENTER);

                    imgCell.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.e("in","in");
                            Log.e("position",position+"");
                            Log.e("key_array",key_array.size()+"");

                            Query applesQuery = FirebaseRef.child("Warranty").orderByKey().equalTo(key_array.get(position-1));
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                }
                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }
                    });

                    imageViews.add(imgCell);
                    addView(imgCell, layoutParams);


                }
            }
//            this.setBackgroundColor(Color.WHITE);//背景白色，利用空隙来实现边框
        }
    }
    /**
     * TableRow 实现表格的行
     * @author hellogv
     */
    static public class TableRow {
        private TableCell[] cell;
        public TableRow(TableCell[] cell) {
            this.cell = cell;
        }
        public int getSize() {
            return cell.length;
        }
        public TableCell getCellValue(int index) {
            if (index >= cell.length)
                return null;
            return cell[index];
        }
    }
    /**
     * TableCell 实现表格的格单元
     * @author hellogv
     */
    static public class TableCell {
        static public final int STRING = 0;
        static public final int IMAGE = 1;
        public Object value;
        public int width;
        public int height;
        private int type;
        public TableCell(Object value, int width, int height, int type) {
            this.value = value;
            this.width = width;
            this.height = height;
            this.type = type;
        }
    }
}
