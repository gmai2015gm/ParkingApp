package com.example.smartpark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.smartpark.Models.ParkingLot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ParkingLotInfo extends AppCompatActivity implements OnMapReadyCallback {
    RecyclerView rvParkingLots;
    GoogleMap map;
    Switch toggleView;
    SeekBar sbMiles;
    Button btnSearch;
    ArrayList<ParkingLot>parkingLots;
    ParkingLotAdapter adapter;
    TextView tvMiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions().
//                position(sydney).
//                title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}