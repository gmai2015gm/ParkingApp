package com.example.smartpark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartpark.Models.ParkingLot;
import com.example.smartpark.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class AddLotActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    Marker m;
    LatLng lotLocation;
    EditText editNameInput;
    TextView lblLotLat, txtLotLat, txtNameInput;
    Button btnSaveLot;
    SharedPreferences lotPref;
    SharedPreferences.Editor editor;
    ArrayList<ParkingLot> parkingLots;
    int lotArrayCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lot);


        editNameInput = findViewById(R.id.editNameInput);
        lblLotLat = findViewById(R.id.lblLotLat);
        txtLotLat = findViewById(R.id.txtLotLat);
        txtNameInput = findViewById(R.id.txtNameInput);
        btnSaveLot = findViewById(R.id.btnSaveLot);


        lotPref = getSharedPreferences("SmartPark", Context.MODE_PRIVATE);
        String parkingLotJSON = lotPref.getString("parkingLots", "");

        if (parkingLotJSON.isEmpty()) {
            parkingLots = new ArrayList<>(); // Return an empty list if no favorites are saved yet
            lotArrayCount = 0;
        } else {
            Gson gson = new Gson();
            ParkingLot[] tempArray = gson.fromJson(parkingLotJSON, ParkingLot[].class);
            if (tempArray != null) {
                parkingLots = new ArrayList<>(Arrays.asList(tempArray));
                lotArrayCount = parkingLots.size();
            } else {
                parkingLots = new ArrayList<>();
                lotArrayCount = 0;
            }
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapAddLot);
        mapFragment.getMapAsync(this);

        btnSaveLot.setOnClickListener(v -> {
            String[] coordinates = lblLotLat.getText().toString().split(", ");
            float tempLat = Float.parseFloat(coordinates[0]);
            float tempLong = Float.parseFloat(coordinates[1]);
            String id = lotArrayCount + "";
            Log.d("SmartPark", "Latitude: " + tempLat + ", Longitude: " + tempLong);

            ParkingLot tempLot = new ParkingLot(id, editNameInput.getText().toString(),
                    tempLat, tempLong, 0, 0, 0,
                    new ArrayList<>());
            parkingLots.add(lotArrayCount, tempLot);

            editor = lotPref.edit();
            Gson gson = new Gson();
            String jsonLots = gson.toJson(parkingLots);
            editor.putString("parkingLots", jsonLots);
            editor.commit();
            finish();
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        // map.setInfoWindowAdapter(new ParkingLotInfoWindow());
        // LatLng nyc  = new LatLng(40.7443679675679, -73.98867886292477);
        // map.moveCamera(CameraUpdateFactory.newLatLng(nyc));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Log.d("MyMap","Map was clicked..");
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                lotLocation = latLng;
                map.clear();
                lblLotLat.setText("" + latLng.latitude + ", " + latLng.longitude);
                // Person p = new Person("John",23,100000);
                m = map.addMarker(new MarkerOptions().position(latLng));
                // m.setTitle("Name: "+p.name);
                // m.setSnippet("Age : "+p.age);
                // m.setTag(p);
            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                // Person p = (Person)marker.getTag();
                // Toast.makeText(MainActivity.this,"Salary: $"+p.salary+"",Toast.LENGTH_LONG).show();
            }
        });

    }
/*
    class ParkingLotInfoWindow implements GoogleMap.InfoWindowAdapter{

        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {
            return null;
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {

            View v = LayoutInflater.from(AddLotActivity.this).inflate(R.layout.info_window_layout,null);
            TextView tvMapLotName = v.findViewById(R.id.tvMapLotName);
            TextView tvLotName = v.findViewById(R.id.tvLotName);
            Person p = (Person)marker.getTag();
            txtAge.setText("Age: +"+p.age+"");
            txtName.setText("Name: "+p.name);
            return v;
        }
    }

 */
}