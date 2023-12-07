package com.example.smartpark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.smartpark.Models.ParkingLot;
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

public class ReportActivity extends AppCompatActivity implements OnMapReadyCallback
{
    TextView txtLotName, lblAvailable, lblClean, lblSafety;
    SeekBar barAvailable, barClean, barSafety;
    Button btnAddLot, btnSave, btnCancel;
    GoogleMap map;
    SharedPreferences lotPref;
    SharedPreferences.Editor editor;

    ArrayList<ParkingLot> parkingLots;
    ParkingLot choseP;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        txtLotName = findViewById(R.id.txtLotName);
        lblAvailable = findViewById(R.id.lblAvailable);
        lblClean = findViewById(R.id.lblClean);
        lblSafety = findViewById(R.id.lblSafety);
        barAvailable = findViewById(R.id.barAvailable);
        barClean = findViewById(R.id.barClean);
        barSafety = findViewById(R.id.barSafety);
        btnAddLot = findViewById(R.id.btnAddLot);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        txtLotName.setText("[parking lot not selected]");
        lotPref = getSharedPreferences("SmartPark", Context.MODE_PRIVATE);
        String parkingLotJSON = lotPref.getString("parkingLots", "");

        if (parkingLotJSON.isEmpty()) {
            parkingLots = new ArrayList<>(); // Return an empty list if no favorites are saved yet
        } else {
            Gson gson = new Gson();
            ParkingLot[] tempArray = gson.fromJson(parkingLotJSON, ParkingLot[].class);
            if (tempArray != null) {
                parkingLots = new ArrayList<>(Arrays.asList(tempArray));
            } else {
                parkingLots = new ArrayList<>();
            }
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapReport);
        mapFragment.getMapAsync(this);


        /*  Progress Bar implementation Logic  */
        //  ---------------------------------

        lblAvailable.setText(barAvailable.getProgress()+" / 10");
        barAvailable.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lblAvailable.setText(i+" / 10");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        lblClean.setText(barClean.getProgress()+" / 10");
        barClean.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lblClean.setText(i+" / 10");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        lblSafety.setText(barSafety.getProgress()+" / 10");
        barSafety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lblSafety.setText(i+" / 10");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnAddLot.setOnClickListener(v -> {
            //      ADD PARKING LOTS TO SHOW UP ON THE MAP.
            Intent i = new Intent(this, AddLotActivity.class);
            startActivity(i);
        });

        btnSave.setOnClickListener(v -> {
            //      LOGIC NOT FINAL. THE RATINGS WILL NEED
            //      TO BE SAVED FOR LATER.
            if(choseP != null){
                Double tempAvailable = (double) barAvailable.getProgress();
                Double tempClean = (double) barClean.getProgress();
                Double tempSafe = (double) barSafety.getProgress();

                for(ParkingLot p : parkingLots){
                    if(p.getID().equals(choseP.getID())){
                        p.setAvgAvailability(tempAvailable);
                        p.setAvgCleanliness(tempClean);
                        p.setAvgSafety(tempSafe);
                    }
                }
            }

            editor = lotPref.edit();
            Gson gson = new Gson();
            String jsonLots = gson.toJson(parkingLots);
            editor.putString("parkingLots", jsonLots);
            editor.commit();
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            finish();
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        txtLotName.setText("[parking lot not selected]");
        String parkingLotJSON = lotPref.getString("parkingLots", "");

        if (parkingLotJSON.isEmpty()) {
            parkingLots = new ArrayList<>(); // Return an empty list if no favorites are saved yet
        } else {
            Gson gson = new Gson();
            ParkingLot[] tempArray = gson.fromJson(parkingLotJSON, ParkingLot[].class);
            if (tempArray != null) {
                parkingLots = new ArrayList<>(Arrays.asList(tempArray));
            } else {
                parkingLots = new ArrayList<>();
            }
        }

        updateMapView(parkingLots);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        map.setInfoWindowAdapter(new ParkingLotInfo.CustomInfoWindowAdapter(getApplicationContext()));
//        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        for (ParkingLot lot:parkingLots) {
            LatLng position = new LatLng(lot.latitude, lot.longitude);
            Marker marker = map.addMarker(new MarkerOptions().position(position).title(lot.name));
            marker.setTag(lot);  // Set the ParkingLot object as a tag
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Object tag = marker.getTag();
                if(tag instanceof ParkingLot){
                    choseP = (ParkingLot) marker.getTag();
                    txtLotName.setText("Lot name: " + choseP.name);
                }

                return false;
            }
        });

    }

    private void updateMapView(ArrayList<ParkingLot> parkingLots) {
//        if (map != null) {
//            for (ParkingLot lot : parkingLots) {
//                LatLng position = new LatLng(lot.latitude, lot.longitude);
//                map.addMarker(new MarkerOptions().position(position).title(lot.name));
//            }
//        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                map.setInfoWindowAdapter(new CustomInfoWindowAdapter(getApplicationContext()));
                if (map != null) {
                    map.clear(); // Clear existing markers
//                    for (ParkingLot lot : parkingLots) {
//                        LatLng position = new LatLng(lot.latitude, lot.longitude);
//                        map.addMarker(new MarkerOptions().position(position).title(lot.name));
//                    }
                    for (ParkingLot lot : parkingLots) {
                        LatLng position = new LatLng(lot.latitude, lot.longitude);
                        Marker marker = map.addMarker(new MarkerOptions().position(position).title(lot.name));
                        marker.setTag(lot);  // Set the ParkingLot object as a tag
                    }

                }
            }
        });
    }

    static class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mWindow;
        private Context mContext;

        // Constructor
        public CustomInfoWindowAdapter(Context context) {
            mContext = context;
            // Inflate the custom layout for the info window
            mWindow = LayoutInflater.from(context).inflate(R.layout.info_window_layout, null);
        }

        // Populates the info window view with the text of the marker
        private void renderWindowText(Marker marker, View view) {
            // Retrieve the ParkingLot object from the marker
            Object tag = marker.getTag();
            if(tag instanceof ParkingLot){
                ParkingLot p = (ParkingLot) marker.getTag();

                // Find and set text for each view in the info window layout
                TextView tvLotName = view.findViewById(R.id.tvMapLotName);
                TextView tvAvailability = view.findViewById(R.id.tvMapAvailability);
                TextView tvSafety = view.findViewById(R.id.tvMapSafety);
                TextView tvCleanliness = view.findViewById(R.id.tvMapCleanliness);

                tvLotName.setText("Lot Name: " + p.name);
                tvAvailability.setText("Availability: " + p.getAvgAvailability());
                tvSafety.setText("Safety: " + p.getAvgSafety());
                tvCleanliness.setText("Cleanliness: " + p.getAvgCleanliness());
            }

        }

        // Returns the entire view for the info window
        @Override
        public View getInfoWindow(Marker marker) {
            renderWindowText(marker, mWindow);
            return mWindow;
        }

        // Returns the contents inside the default info window frame
        @Override
        public View getInfoContents(Marker marker) {
            renderWindowText(marker, mWindow);
            return mWindow;
        }
    }
}

