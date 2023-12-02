package com.example.smartpark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.smartpark.APIHelpers.ParkingLotHelper;
import com.example.smartpark.Models.ParkingLot;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class ParkingLotInfo extends AppCompatActivity implements OnMapReadyCallback{
    RecyclerView rvParkingLots;
    GoogleMap map;
    Switch toggleView;
    SeekBar sbMiles;
    Button btnSearch;
    ArrayList<ParkingLot>parkingLots;
    ParkingLotAdapter adapter;
    TextView tvMiles;
    double lon, lat;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MapsInitializer.initialize(this);
//        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, l->{});

        setContentView(R.layout.activity_parking_lot_info);
        rvParkingLots = findViewById(R.id.rvParkingLots);
        toggleView = findViewById(R.id.toggleView);
        sbMiles = findViewById(R.id.sbMiles);
        parkingLots = new ArrayList<>();
        adapter = new ParkingLotAdapter(this, parkingLots);
        tvMiles = findViewById(R.id.tvMiles);
        btnSearch = findViewById(R.id.btnSearch);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvParkingLots.setLayoutManager(linearLayoutManager);
        rvParkingLots.setAdapter(adapter);
        tvMiles.setText("< 5 miles");

        // Get the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set the initial state of the map and RecyclerView
//        rvParkingLots.setVisibility(View.GONE); // Hide the RecyclerView initially
        mapFragment.getView().setVisibility(View.GONE);
        toggleView.setText("List View"); // Set initial text for the switch
        rvParkingLots.setVisibility(View.VISIBLE);
        // Set up the toggle switch listener
        toggleView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // When the switch is on, show the map and hide the list
                mapFragment.getView().setVisibility(View.VISIBLE);
                rvParkingLots.setVisibility(View.GONE);
                toggleView.setText("Map View");
            } else {
                // When the switch is off, show the list and hide the map
                mapFragment.getView().setVisibility(View.GONE);
                rvParkingLots.setVisibility(View.VISIBLE);
                toggleView.setText("List View");
            }
        });

        // Set up a listener for the SeekBar
        sbMiles.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the TextView with the value of SeekBar progress
                if(progress+5>=50){
                    tvMiles.setText(">"+ String.valueOf(progress + 5) +" miles"); // Adding 5 to make the range 5-50
                }else{
                    tvMiles.setText("<"+ String.valueOf(progress + 5)+" miles");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnSearch.setOnClickListener(l->{
            getUserLocationAndFetchParkingLots();
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
            // Check if the permission is granted.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                Log.d("maps", "getLocation: permissions NOW granted");

            } else { // Permission was denied...
                Log.d("maos", "getLocation: permissions NOT granted");
            }
            break;
            }
            }



    private void getUserLocationAndFetchParkingLots() {

        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        final double userLat;
        final double userLon;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Check for permissions again, just in case
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }



//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setInterval(60000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setFastestInterval(5000);
//        LocationCallback locationCallback= new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//
//                if(locationResult==null)
//                    return;
//                for(Location location:locationResult.getLocations()){
//                    if(location!=null){
////                        userLon = location.getLongitude();
//                    }
//                }
//            }
//        };
//
//        locationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null);
//

        locationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double userLat = location.getLatitude();
                        double userLng = location.getLongitude();
                        int radius = sbMiles.getProgress() + 5; // Adjust based on SeekBar value

//
//                        new ParkingLotHelper().fetchParkingLots(userLat, userLng, radius, map, adapter,this,getApplicationContext());
                    } else {
                        // Handle the case where location is null
                    }
                })
                .addOnFailureListener(this, e -> {
                    // Handle the failure in getting location
                });
    }


    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        for (ParkingLot lot:parkingLots) {
            float lat = lot.latitude;
            float longitude = lot.longitude;
            LatLng current = new LatLng(lat,longitude);
            googleMap.addMarker(new MarkerOptions().position(current).title(lot.name));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        }

    }


    private void updateMapView(ArrayList<ParkingLot> parkingLots) {
        if (map != null) {
            for (ParkingLot lot : parkingLots) {
                LatLng position = new LatLng(lot.latitude, lot.longitude);
                map.addMarker(new MarkerOptions().position(position).title(lot.name));
            }
        }
    }


}