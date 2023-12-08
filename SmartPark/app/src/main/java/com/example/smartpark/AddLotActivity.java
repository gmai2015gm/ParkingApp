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
import android.widget.ImageView;
import android.widget.Switch;
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
import com.google.android.gms.maps.MapView;
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
    TextView lblLotLat, txtLotLat, txtNameInput, txtInstructions;
    Button btnSaveLot, btnPlaceMarker;
    Switch switchMethod;
    SharedPreferences lotPref;
    SharedPreferences.Editor editor;
    ArrayList<ParkingLot> parkingLots;
    int lotArrayCount;
    boolean switchFlag, markerPlaced;
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
        switchMethod = findViewById(R.id.switchMethod);
        txtInstructions = findViewById(R.id.txtInstructions);
        btnPlaceMarker = findViewById(R.id.btnPlaceMarker);
        markerPlaced = false;

        // lotPref = getSharedPreferences("SmartPark", Context.MODE_PRIVATE);
        // String parkingLotJSON = lotPref.getString("parkingLots", "");

        parkingLots = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapAddLot);
        mapFragment.getMapAsync(this);
        switchFlag = false;
        btnPlaceMarker.setVisibility(View.INVISIBLE);

        // Set up switch listener to change methods of creating a parking lot.
        switchMethod.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // When the switch is on, switch to method 2 (create lot
                // by getting the user's current location.
                txtInstructions.setText("Method 2: Press the 'Place marker' button to set your " +
                        "current location as the location of the new parking lot.");
                btnPlaceMarker.setVisibility(View.VISIBLE);
                switchMethod.setText("Method 2");
                switchFlag = true;
            } else {
                //  When the switch is off, switch to method 1 (create lot
                //  by manually placing a marker on the map)
                txtInstructions.setText("Method 1: Place a marker on the map to indicate the " +
                        "location for the new parking lot.");
                btnPlaceMarker.setVisibility(View.INVISIBLE);
                switchMethod.setText("Method 1");
                switchFlag = false;
            }
        });

        btnPlaceMarker.setOnClickListener(v -> {
            getUserLocation();
        });

        btnSaveLot.setOnClickListener(v -> {

            if(markerPlaced == true){
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
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(),
                        "ERROR! Please place a marker on the map to indicate the parking" +
                                "lot's location.",
                        Toast.LENGTH_LONG).show();
            }

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

                        SupportMapFragment mapfromButton = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.mapAddLot);
                        mapfromButton.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                map = googleMap;
                                map.clear();
                                lblLotLat.setText("" + userLat + ", " + userLng);

                                LatLng latLng = new LatLng(userLat, userLng);
                                map.addMarker(new MarkerOptions().position(latLng));
                                markerPlaced = true;
                            }
                        });
                    } else {
                        // Handle the case where location is null
                        Toast.makeText(getApplicationContext(),
                                "ERROR! Location was not retrieved.",
                                Toast.LENGTH_SHORT);
                    }
                })
                .addOnFailureListener(this, e -> {
                    // Handle the failure in getting location
                    Toast.makeText(getApplicationContext(),
                            "ERROR! Location was not retrieved.",
                            Toast.LENGTH_SHORT);
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
                if(switchFlag == false) {
                    lotLocation = latLng;
                    map.clear();
                    lblLotLat.setText("" + latLng.latitude + ", " + latLng.longitude);
                    m = map.addMarker(new MarkerOptions().position(latLng));
                    markerPlaced = true;
                }
            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                //
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