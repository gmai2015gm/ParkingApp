package com.example.smartpark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    Button btnSettings, btnFindParking;
    ImageButton imgbtnAppInfo;
    public static final String TAG = "SmartPark";
    public static final int LOCATION_REQUEST_CODE = 115;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSettings = findViewById(R.id.btnSettings);
        btnFindParking = findViewById(R.id.btnFindParking);
        imgbtnAppInfo = findViewById(R.id.imgbtnAppInfo);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            Log.d(TAG, "Location access permitted...");
        }


//        btnSettings.setOnClickListener(v -> {
//            Intent i = new Intent(this, Settings.class);
//            startActivity(i);
//            finish();
//        });

        btnFindParking.setOnClickListener(v -> {
//            Intent i = new Intent(this, ParkingLotInfo.class);
//            startActivity(i);
//            finish();
        });

        imgbtnAppInfo.setOnClickListener(v -> {

        });
    }
}