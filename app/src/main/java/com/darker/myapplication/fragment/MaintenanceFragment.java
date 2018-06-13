package com.darker.myapplication.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.darker.myapplication.Adapter.TableAdapter;
import com.darker.myapplication.Activity.MainActivity;
import com.darker.myapplication.R;
import com.darker.myapplication.Structure.MaintainStructure;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;

public class MaintenanceFragment extends Fragment {
    TableAdapter.TableCell[] titles;
    ListView listView;
    TableAdapter tableAdapter;
    ArrayList<TableAdapter.TableRow> table;

    public static ArrayList<String> key_array = new ArrayList<>();
    public static ArrayList<MaintainStructure> main_arrayList = new ArrayList<>();
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maintenance, container, false);

        Button nextPageBtn = rootView.findViewById(R.id.button);
        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getContext()).chageFragment("新增保養");
            }
        });

        listView = rootView.findViewById(R.id.ListView01);
        table = new ArrayList<>();
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth()/3;
        titles = new TableAdapter.TableCell[3];// 每行5个单元
        titles[0] = new TableAdapter.TableCell("類型",width + 8 * 0,RelativeLayout.LayoutParams.FILL_PARENT,TableAdapter.TableCell.STRING);
        titles[1] = new TableAdapter.TableCell("時間",width + 8 * 1,RelativeLayout.LayoutParams.FILL_PARENT,TableAdapter.TableCell.STRING);
        titles[2] = new TableAdapter.TableCell("刪除",width + 8 * 2,RelativeLayout.LayoutParams.FILL_PARENT,TableAdapter.TableCell.STRING);
        table.add(new TableAdapter.TableRow(titles));

        tableAdapter = new TableAdapter(getContext(), table);
        listView.setAdapter(tableAdapter);
        listView.setOnItemClickListener(new ItemClickEvent());

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("GTCLOUD_Content", MODE_PRIVATE);
        final Firebase Firebase  = new Firebase("https://test-72950.firebaseio.com/Warranty");
        Query queryRef = Firebase.orderByChild("day");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                MaintainStructure maintainStructure = snapshot.getValue(MaintainStructure.class);
                if(maintainStructure != null){
                    if(maintainStructure.user_id.equals(sharedPreferences.getString("userid",""))){
                        main_arrayList.add(maintainStructure);
                        key_array.add(snapshot.getKey());

                        ArrayList<TableAdapter.TableCell[]> arrayList = new ArrayList<>();
                        arrayList.add(getTableItem(maintainStructure.type,maintainStructure.day,titles));
                        for (int i = 0;i<arrayList.size();i++){
                            table.add(new TableAdapter.TableRow(arrayList.get(i)));
                        }

                        tableAdapter.notifyDataSetChanged();
                    }
                }
            }
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (int i = 0 ; i < key_array.size();i++){
                    if(key_array.get(i).equals(dataSnapshot.getKey())){
                        key_array.remove(i);
                        table.remove(i+1);
                        tableAdapter.notifyDataSetChanged();

                    }
                }
                MaintainStructure maintainStructure = dataSnapshot.getValue(MaintainStructure.class);
            }
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    class ItemClickEvent implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
            if(arg2 != 0 ){
                new SweetAlertDialog(getActivity())
                        .setTitleText(main_arrayList.get(arg2-1).type)
                        .setContentText("日期 : "+main_arrayList.get(arg2-1).day+"\n"+
                                "廠牌 : "+ main_arrayList.get(arg2-1).label+"\n"+
                                "公里數 : " + main_arrayList.get(arg2-1).trip+"\n"+
                                "價錢 : " +main_arrayList.get(arg2-1).price+"\n"
                        ).show();
            }
        }
    }

    private TableAdapter.TableCell[] getTableItem(String name, String date, TableAdapter.TableCell[] titles){
        TableAdapter.TableCell[] cells = new TableAdapter.TableCell[3];
        cells[0] = new TableAdapter.TableCell(name, titles[0].width, RelativeLayout.LayoutParams.FILL_PARENT, TableAdapter.TableCell.STRING);
        cells[1] = new TableAdapter.TableCell(date, titles[1].width, RelativeLayout.LayoutParams.FILL_PARENT, TableAdapter.TableCell.STRING);
        cells[2] = new TableAdapter.TableCell(R.drawable.delete,titles[2].width,RelativeLayout.LayoutParams.WRAP_CONTENT,TableAdapter.TableCell.IMAGE);
        return cells;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
