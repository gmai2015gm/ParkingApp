package com.example.smartpark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Settings extends AppCompatActivity {

    //Declare the variables!
    Button btnDisableLocation, btnDisableNotifs, btnSignOut, btnDeleteAcct, btnConfirm, btnDecline;
    ImageButton btnBack;
    TextView txtConfMsg;
    RequestQueue queue;
    String url;
    int action;


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

        //Hide the confirmation menu
        txtConfMsg.setVisibility(View.INVISIBLE);
        btnConfirm.setVisibility(View.INVISIBLE);
        btnDecline.setVisibility(View.INVISIBLE);
    }

    //Confirmation menus that actually handle the setting actions
    public void confirm(View v) {
        Intent i;

        //Switch case so each thing is handled appropriately
        switch (action){
            case 1:
                Log.i("Settings", "Deleting Account");
                //Insert deletion logic here

                //Send user to the registration/sign in screen
                i = new Intent(this, UserAuthActivity.class);
                startActivity(i);
                break;
            case 2:
                Log.i("Settings", "Signing Out");
                //Insert sign out logic here

                //Send user to the sign in/registration screen
                i = new Intent(this, UserAuthActivity.class);
                startActivity(i);
                break;
            case 3:
                Log.i("Settings", "Disabling location services");
                //Insert disable permission logic here

                //Send user to the sign in/registration screen
                //Code snippet credits: Waynn Lue & ognian on Stack Overflow
                i = new Intent(Intent.ACTION_MAIN);
                i.addCategory(Intent.CATEGORY_HOME);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
        }
        finish();
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
    public void delAcct(View v) {
        action = 1;
        displayConfirmation("Are you sure you want to delete your account?\nYou will be returned to the registration screen.");
    }

    public void signOut(View v) {
        action = 2;
        displayConfirmation("Are you sure you want to sign out?\nYou will be returned to the sign in screen.");
    }

    public void disableLocServs(View v){
        action = 3;
        displayConfirmation("Are you sure you want to disable location permissions?\nThe app cannot function without these and will close as a result.");
    }

    //Disable notifications needs to be handled differently
    public void disableNotifs(View v) {

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
