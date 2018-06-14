package com.darker.myapplication.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.darker.myapplication.Activity.MainActivity;
import com.darker.myapplication.Maps.Connecter;
import com.darker.myapplication.Maps.MyItem;
import com.darker.myapplication.Maps.OwnIconRendered;
import com.darker.myapplication.R;
import com.darker.myapplication.Structure.MapStructure;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private ClusterManager<MyItem> mClusterManager;

    private GoogleMap mMap;
    float zoom = 7;
    private LocationManager locMgr;
    String bestProv;
    private ArrayList<MapStructure> mapList = new ArrayList<>();
    String elevation = "";
    final Marker[] finalMarker1 = {null};
    public static LatLng latLng2;
    private static final int MESSAGE_CHECKED = 0;

    private Handler handler = new Handler();
    private String tag;

    public static String tpye ="battery";
    SweetAlertDialog pDialog;
    @Nullable
    private Button battery_btn,car_dealers_btn,gas_btn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        findById(view);
        Fragment a = getChildFragmentManager().findFragmentById(R.id.fragment_view_map);
        com.google.android.gms.maps.MapFragment mapFragment = (com.google.android.gms.maps.MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.fragment_view_map);
        mapFragment.getMapAsync(this);

        // 取得定位服務
        locMgr = (LocationManager) ((MainActivity) getContext()).getSystemService(Context.LOCATION_SERVICE);
        // 取得最佳定位
        Criteria criteria = new Criteria();
        bestProv = locMgr.getBestProvider(criteria, true);

        // 如果GPS或網路定位開啟，更新位置
        if (locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER) || locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //  確認 ACCESS_FINE_LOCATION 權限是否授權
            if (ActivityCompat.checkSelfPermission(((MainActivity) getContext()),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locMgr.requestLocationUpdates(bestProv, 1000, 1, this);
            }
        } else {
            Toast.makeText(((MainActivity) getContext()), "請開啟定位服務", Toast.LENGTH_LONG).show();
        }
        return view;
    }
    private void findById(View view) {
        battery_btn = view.findViewById(R.id.battery_btn2);
        car_dealers_btn = view.findViewById(R.id.car_dealers_btn2);
        gas_btn = view.findViewById(R.id.gas_btn2);

        battery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                mClusterManager.clearItems();
                tag = "battery";
                readFireBaseData();
            }
        });
        car_dealers_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                mClusterManager.clearItems();
                tag = "car_dealers";
                readFireBaseData();
            }
        });
        gas_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                mClusterManager.clearItems();
                tag = "gas";
                readFireBaseData();
            }
        });
    }


    private void readFireBaseData() {
        handler.post(runnable);
    }

    final Marker[] finalMarker = {null};

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e("onMapReady", "onMapReady");
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //群集map
        mClusterManager = new ClusterManager<MyItem>(getActivity(), mMap);
        mClusterManager.setRenderer(new OwnIconRendered(getContext(),mMap,mClusterManager));
        mMap.setOnCameraIdleListener(mClusterManager);

        requestPermissions();

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER); // 設定定位資訊由 GPS提供
        double lat = 23.4471421,lng = 120.9861134;
        if(location != null){
            lat = location.getLatitude();  // 取得經度
            lng = location.getLongitude(); // 取得緯度
        }

        LatLng HOME = new LatLng(lat, lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(HOME,zoom));
        mMap.setOnMapClickListener(mapClickListener);
        tag = "battery";
        readFireBaseData();
    }

    private void requestPermissions(){
        if (Build.VERSION.SDK_INT >=23){
            int hasPermission = ActivityCompat.checkSelfPermission(((MainActivity)getContext()), android.Manifest.permission.ACCESS_COARSE_LOCATION);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(((MainActivity)getContext()),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
                return;
            }
        }
        setMyLocation();
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions,int [] grantResults){
        if (requestCode == 1){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                setMyLocation();
            }else {
                Toast.makeText(((MainActivity)getContext()),"未取得授權!",Toast.LENGTH_SHORT).show();
                ((MainActivity)getContext()).finish();
            }
        }else {
            super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

    private void setMyLocation() throws SecurityException{
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
//        String x = "緯=" + Double.toString(location.getLatitude());
//        String y = "經=" + Double.toString(location.getLongitude());
//
//        LatLng Point = new LatLng(location.getLatitude(),location.getLongitude());
//        zoom = 17;
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Point,zoom));
//        Toast.makeText(((MainActivity)getContext()),x + "\n" + y,Toast.LENGTH_LONG).show();

    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Criteria criteria = new Criteria();
        bestProv = locMgr.getBestProvider(criteria, true);
    }
    public void onProviderEnabled(String provider) {

    }
    public void onProviderDisabled(String provider) {

    }


    final Connecter connecter  = Connecter.getInstance();

    private GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            MapFragment.latLng2 = latLng;
            new Thread(new Runnable(){
                @Override
                public void run() {
                    String result = connecter.getJSONContent("https://maps.googleapis.com/maps/api/elevation/json?locations="+Double.parseDouble(String.valueOf( MapFragment.latLng2.latitude))+","+Double.parseDouble(String.valueOf( MapFragment.latLng2.longitude))+"&key=AIzaSyALcD0X57AVTQJq8GoqK3-62Hia5TpoF2I");
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray array = jsonObject.getJSONArray("results");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObjectelevation = array.getJSONObject(i);
                            elevation = jsonObjectelevation.getString("elevation");
                        }
                        Message message = mHandler.obtainMessage(MESSAGE_CHECKED);

                        Bundle bundle = new Bundle();
                        bundle.putString("elevation", elevation);
                        message.setData(bundle);
                        message.sendToTarget();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            switch (msg.what) {
                case MESSAGE_CHECKED:
                    if(finalMarker1[0] != null)
                        finalMarker1[0].remove();
                    finalMarker1[0] =  mMap.addMarker(new MarkerOptions().position(latLng2).title("點選位置海拔"+":"+(int)Float.parseFloat(bundle.getString("elevation"))+"").snippet("單位/公尺"));
                    finalMarker1[0].showInfoWindow();
            }
        }
    };


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();

            Firebase myFirebaseRef = null;
            switch (tag) {
                case "battery" :
                    tpye = "battery";
                    myFirebaseRef = new Firebase("https://motorcycle-cc0fe.firebaseio.com/place/battery");
                    break;
                case "car_dealers" :
                    tpye = "car_dealers";
                    myFirebaseRef = new Firebase("https://motorcycle-cc0fe.firebaseio.com/place/car_dealers");
                    break;
                case "gas" :
                    tpye = "gas";
                    myFirebaseRef= new Firebase("https://motorcycle-cc0fe.firebaseio.com/place/gas");
                    break;
            }
            Query queryRef = myFirebaseRef.orderByChild("lat");

            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    MapStructure mapStructure = snapshot.getValue(MapStructure.class);
//                Log.e("FireBaseTraining", "lat = " + mapStructure.lat +"lng = " + mapStructure.lng+ " , Name = " + mapStructure.name+" , add = " + mapStructure.add);
                    LatLng latLng = null;
                    try {
                        latLng = new LatLng(Double.parseDouble(mapStructure.lat), Double.parseDouble(mapStructure.lng));
                    }catch (Exception e){

                    }
                    if(latLng != null){
                        MyItem myitem = new MyItem(latLng.latitude,latLng.longitude,mapStructure.name,mapStructure.add);
                        mClusterManager.addItem(myitem);
                    }
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(FirebaseError firebaseError) {}
            });

            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) { //下載完成
                    mClusterManager.cluster();
                    pDialog.dismiss();
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });
        }
    };
}
