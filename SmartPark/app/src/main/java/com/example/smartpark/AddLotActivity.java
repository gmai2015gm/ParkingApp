package com.example.smartpark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.smartpark.APIHelpers.ParkingLotHelper;
import com.example.smartpark.Models.ParkingLot;
import com.example.smartpark.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

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

        /*
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
        */

        parkingLots = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapAddLot);
        mapFragment.getMapAsync(this);

        btnSaveLot.setOnClickListener(v -> {
            String[] coordinates = lblLotLat.getText().toString().split(", ");
            float tempLat = Float.parseFloat(coordinates[0]);
            float tempLong = Float.parseFloat(coordinates[1]);
            String id = lotArrayCount + "";
            Log.d("SmartPark", "Latitude: " + tempLat + ", Longitude: " + tempLong);

            // Create an instance of ParkingLotHelper
            RequestQueue queue = Volley.newRequestQueue(this);
            ParkingLotHelper parkingLotHelper = new ParkingLotHelper(this, queue);

            // Call addNewLot
            parkingLotHelper.addNewLot(editNameInput.getText().toString(),
                    tempLat, tempLong, new ParkingLotHelper.AdditionCallbackFunction() {
                        @Override
                        public void onComplete(boolean success) {
                            if (success == true) {
                                Log.d("SmartPark", "NEW PARKING LOT ADDED!");
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "ERROR! Parking lot was not created.",
                                        Toast.LENGTH_SHORT);
                            }
                        }
            });
        });
    }


    private void getUserLocation() {

        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check for permissions again, just in case
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }



        locationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double userLat = location.getLatitude();
                        double userLng = location.getLongitude();

                        map.clear();
                        lblLotLat.setText("" + userLat + ", " + userLng);

                        LatLng latLng = new LatLng(userLat, userLng);
                        m = map.addMarker(new MarkerOptions().position(latLng));
                    } else {
                        // Handle the case where location is null
                    }
                })
                .addOnFailureListener(this, e -> {
                    // Handle the failure in getting location
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