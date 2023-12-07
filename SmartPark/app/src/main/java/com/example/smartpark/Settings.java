package com.example.smartpark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.net.CookieHandler;
import java.net.CookieManager;

public class Settings extends AppCompatActivity {

    //Declare the variables!
    Button btnDisableLocation, btnDisableNotifs, btnSignOut, btnDeleteAcct, btnConfirm, btnDecline;
    ImageButton btnBack;
    TextView txtConfMsg;
    RequestQueue queue;
    String url;
    int action;
    CookieManager cookies;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        //Set up Volley
        queue = Volley.newRequestQueue(this);
        url = "https://smartpark-api.onrender.com";

        //Assign views
        btnDisableLocation = findViewById(R.id.btnRemoveLocation);
        btnDisableNotifs = findViewById(R.id.btnRemoveNotifs);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnDeleteAcct = findViewById(R.id.btnConfirm);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnDecline = findViewById(R.id.btnDecline);
        btnBack = findViewById(R.id.btnBack);
        txtConfMsg = findViewById(R.id.txtConfMsg);

        //Get cookies
        cookies = (CookieManager) CookieHandler.getDefault();

        //Hide the confirmation menu
        txtConfMsg.setVisibility(View.INVISIBLE);
        btnConfirm.setVisibility(View.INVISIBLE);
        btnDecline.setVisibility(View.INVISIBLE);
    }

    //Confirmation menus that actually handle the setting actions
    public void confirm(View v) {
        //Switch case so each thing is handled appropriately
        switch (action){
            case 1:
                Log.i("Settings", "Signing Out");
                //Insert sign out logic here
                //Send the sign in request to the server and get response
                JsonObjectRequest signOutReq = new JsonObjectRequest(Request.Method.POST, url + "/logout", null,
                        response -> {
                            try {
                                int successFlag = response.getInt("success");
                                if (successFlag == 1){
                                    //Sign user out
                                    if (cookies.getCookieStore().getCookies().size() > 0) {
                                        cookies.getCookieStore().removeAll();
                                    }

                                    //Send user to the sign in/registration screen
                                    startActivity(new Intent(this, UserAuthActivity.class));
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

                break;
            case 2:
                Log.i("Settings", "Disabling location services");
                //Insert disable permission logic here

                //Send user to the sign in/registration screen
                //Code snippet credits: Waynn Lue & ognian on Stack Overflow
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                break;
            case 3:
        }
    }

    public void decline(View v){
        //Do nothing, just hide the confirmation menu
        txtConfMsg.setVisibility(View.INVISIBLE);
        btnConfirm.setVisibility(View.INVISIBLE);
        btnDecline.setVisibility(View.INVISIBLE);
    }

    public void back(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    //Methods for the various setting actions
    public void signOut(View v) {
        action = 1;
        displayConfirmation("Are you sure you want to sign out?\nYou will be returned to the sign in screen.");
    }

    public void disableLocServs(View v){
        action = 2;
        displayConfirmation("Are you sure you want to disable location permissions?\nThe app cannot function without these and will close as a result.");
    }

    //Disable notifications needs to be handled differently
    public void disableNotifs(View v) {
        action = 3;
        displayConfirmation("Are you sure you want to disable notifications?");
    }

    //Helper method to display the confirmation
    private void displayConfirmation(String message) {
        //Change text to the message
        txtConfMsg.setText(message);

        //Show the menu
        txtConfMsg.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.VISIBLE);
        btnDecline.setVisibility(View.VISIBLE);
    }
}
