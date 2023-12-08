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
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartpark.Utilities.PersistentHttpCookieStore;

import org.json.JSONException;

import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.CookieManager;

public class MainActivity extends AppCompatActivity {

    Button btnSignOut, btnFindParking, btnViewReports;
    ImageView ivAppInfo;
    public static final String TAG = "SmartPark";
    public static final int LOCATION_REQUEST_CODE = 115;
    RequestQueue queue;
    String url;
    public java.net.CookieManager cookies;
    PersistentHttpCookieStore cookieStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnFindParking = findViewById(R.id.btnFindParking);
        btnViewReports = findViewById(R.id.btnViewReports);
        ivAppInfo = findViewById(R.id.ivAppInfo);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            Log.d(TAG, "Location access permitted...");
        }

        //Set up Volley
        queue = Volley.newRequestQueue(this);
        url = "https://smartpark-api.onrender.com";

        //Set up cookies
        cookieStore = new PersistentHttpCookieStore(getApplicationContext());
        cookies = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookies);

        btnViewReports.setOnClickListener(v -> {
            //put report activity here
            Intent i = new Intent(this, ReportActivity.class);
            startActivity(i);

        });

        btnSignOut.setOnClickListener(v -> {
                    //Send the sign out request to the server and get response
        JsonObjectRequest signOutReq = new JsonObjectRequest(Request.Method.POST, url + "/logout", null,
                response -> {
                    try {
                        int successFlag = response.getInt("success");
                        if (successFlag == 1){
                            //Clear the session
                            if (cookies.getCookieStore().getCookies().size() > 0) {
                                cookies.getCookieStore().removeAll();
                            }

                            //Send user to the sign in/registration screen
                            startActivity(new Intent(this, UserAuthActivity.class));
                            finish();

                            Log.i("Sign Out", "Successful sign out");
                        } else {
                            Log.e("Sign Out", "Could not sign out user.");
                            Toast.makeText(this, "Error signing out. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e("Sign Out", "Could not read the server response.\n" + e);
                        Toast.makeText(this, "Error signing out. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                , error -> {
                    Log.e("Sign Out", "Could not reach the server.");
                    Toast.makeText(this, "Could not reach the server.", Toast.LENGTH_SHORT).show();
                }
        );
        queue.add(signOutReq);
            
        });

        btnFindParking.setOnClickListener(v -> {
            Intent i = new Intent(this, ParkingLotInfo.class);
            startActivity(i);

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
        protected void onDestroy() {
        super.onDestroy();
        //Send the sign out request to the server and get response
        JsonObjectRequest signOutReq = new JsonObjectRequest(Request.Method.POST, url + "/logout", null,
                response -> {
                    try {
                        int successFlag = response.getInt("success");
                        if (successFlag == 1){
                            //Clear the session
                            if (cookies.getCookieStore().getCookies().size() > 0) {
                                cookies.getCookieStore().removeAll();
                            }

                            //Send user to the sign in/registration screen
//                            startActivity(new Intent(this, UserAuthActivity.class));
                            finish();

                            Log.i("Sign Out", "Successful sign out");
                        } else {
                            Log.e("Sign Out", "Could not sign out user.");
                        }
                    } catch (JSONException e) {
                        Log.e("Sign Out", "Could not read the server response.\n" + e);
                    }
                }
                , error -> {
            Log.e("Sign Out", "Could not reach the server.");
            Toast.makeText(this, "Could not reach the server.", Toast.LENGTH_SHORT).show();
        }
        );
        queue.add(signOutReq);
    }
}
