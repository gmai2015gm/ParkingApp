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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    Button btnSettings, btnFindParking, btnViewReports;
    ImageView ivAppInfo;
    public static final String TAG = "SmartPark";
    public static final int LOCATION_REQUEST_CODE = 115;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
//            Log.i("Settings", "Signing Out");
//            //Insert sign out logic here
//            //Send the sign in request to the server and get response
//            JsonObjectRequest signOutReq = new JsonObjectRequest(Request.Method.POST, url + "/logout", null,
//                    response -> {
//                        try {
//                            int successFlag = response.getInt("success");
//                            if (successFlag == 1){
//                                //Sign user out
//                                if (cookies.getCookieStore().getCookies().size() > 0) {
//                                    cookies.getCookieStore().removeAll();
//                                }
                                //Send user to the sign in/registration screen
                    //            i = new Intent(this, UserAuthActivity.class);
                    //            startActivity(i);

//                                Log.i("Sign Out", "Successful sign out");
//                            } else {
//                                Log.e("Sign Out", "Could not sign out user.");
//                            }
//                        } catch (JSONException e) {
//                            Log.e("Sign Out", "Could not read the server response.\n" + e);
//                        }
//                    }
//                    , error -> {
//                Log.e("Sign Out", "Could not reach the server.");
//                Toast.makeText(this, "Could not reach the server.", Toast.LENGTH_SHORT).show();
//            }
//            );
//            queue.add(signOutReq);



//            Intent i = new Intent(this, Settings.class);
//            startActivity(i);
//            finish();
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