package com.example.smartpark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button btnSettings, btnFindParking, btnViewReports;
    ImageView ivAppInfo;
    public static final String TAG = "SmartPark";
    public static final int LOCATION_REQUEST_CODE = 115;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSettings = findViewById(R.id.btnSettings);
        btnFindParking = findViewById(R.id.btnFindParking);
        btnViewReports = findViewById(R.id.btnViewReports);
        ivAppInfo = findViewById(R.id.ivAppInfo);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            Log.d(TAG, "Location access permitted...");
        }


        btnViewReports.setOnClickListener(v -> {
                                //put report activity here
//            Intent i = new Intent(this, .class);
//            startActivity(i);
//            finish();
        });

        btnSettings.setOnClickListener(v -> {
            Intent i = new Intent(this, Settings.class);
            startActivity(i);
            finish();
        });

        btnFindParking.setOnClickListener(v -> {
//            Intent i = new Intent(this, ParkingLotInfo.class);
//            startActivity(i);
//            finish();
        });

        ivAppInfo.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this, R.style.DialogStyle);
            dialog.setContentView(R.layout.activity_app_info);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);
            ImageView ivCloseAppInfo = dialog.findViewById(R.id.ivCloseAppInfo);
            ivCloseAppInfo.setOnClickListener(v2 -> {
                dialog.dismiss();
            });
            dialog.show();

        });
    }
}