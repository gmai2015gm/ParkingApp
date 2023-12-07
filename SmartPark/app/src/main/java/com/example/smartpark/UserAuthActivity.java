package com.example.smartpark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserAuthActivity extends AppCompatActivity {
    Button btnSignIn, btnRegister, btnAuth, btnSwitch;
    EditText etUsername, etEmail, etPass, etPassConf;
    boolean firstTimeUser, authingFlag;
    RequestQueue queue;
    String url;
    SharedPreferences session;
    SharedPreferences.Editor sessionEditor;
    public CookieManager cookies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_auth_activity);

        //Set up Volley
        queue = Volley.newRequestQueue(this);
        url = "https://smartpark-api.onrender.com";

        //Set up sharedprefs
        session = this.getSharedPreferences("Session", Context.MODE_PRIVATE);
        sessionEditor = session.edit();

        //Get any existing cookies
        cookies = (CookieManager) CookieHandler.getDefault();

        //If the user is already signed in, go to landing screen
        if (cookies != null && cookies.getCookieStore().getCookies().size() > 0) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        //Set up cookies
        cookies = new CookieManager();
        CookieHandler.setDefault(cookies);

        //Nothing is authorizing at start
        authingFlag = false;

        //Assign views
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        btnAuth = findViewById(R.id.btnAuth);
        btnSwitch = findViewById(R.id.btnSwitch);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etPassConf = findViewById(R.id.etPassConf);
    }

    //Set up the sign in screen
    public void signIn(View v){
        //Not a new user
        firstTimeUser = false;

        //Show the correct views
        etEmail.setHint("Username/Email");
        etEmail.setVisibility(View.VISIBLE);
        etPass.setVisibility(View.VISIBLE);
        btnSwitch.setText("Register");
        btnSwitch.setVisibility(View.VISIBLE);
        btnAuth.setText("Sign In");
        btnAuth.setVisibility(View.VISIBLE);

        //Hide unused views
        etUsername.setVisibility(View.INVISIBLE);
        etPassConf.setVisibility(View.INVISIBLE);

        //Hide the starting buttons
        btnSignIn.setVisibility(View.INVISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);
    }

    //Set up the registration screen
    public void register(View v){
        //New user
        firstTimeUser = true;

        //Show the correct views
        etUsername.setVisibility(View.VISIBLE);
        etEmail.setHint("Email");
        etEmail.setVisibility(View.VISIBLE);
        etPass.setVisibility(View.VISIBLE);
        etPassConf.setVisibility(View.VISIBLE);
        btnSwitch.setText("Sign In");
        btnSwitch.setVisibility(View.VISIBLE);
        btnAuth.setText("Register");
        btnAuth.setVisibility(View.VISIBLE);

        //Hide the starting buttons
        btnSignIn.setVisibility(View.INVISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);
    }

    //Switch between sign in and register
    public void switchMode(View v){
        //Switch views based on the current mode
        if (firstTimeUser){ //If they were registering, sign in instead
            signIn(v);
        } else { //and vice versa
            register(v);
        }
    }

    //Sign in/register the user then send them to the main screen
    public void authenticateUser(View v){
        if (!authingFlag){ //Prevent spam
            if (firstTimeUser){
                register();
            } else {
                signIn();
            }
        }
    }

    private void signIn() {
        authingFlag = true;

        //Create the sign in
        JSONObject signIn = new JSONObject();
        try {
            signIn.put("loginName", etEmail.getText());
            signIn.put("password", hashPass()); //This helper function returns the hashed password
        } catch (JSONException e) {
            Log.e("Sign In", "Failed to create the sign in JSON");
            authingFlag = false;
            return;
        }

        //Send the sign in request to the server and get response
        JsonObjectRequest signInReq = new JsonObjectRequest(Request.Method.POST, url + "/login", signIn,
                response -> {
                    try {
                        int successFlag = response.getInt("success");
                        if (successFlag == 1){
                            //Save session to sharedprefs
                            sessionEditor.putString("SessionKey", response.getString("sessionID"));
                            sessionEditor.putString("username", response.getString("username"));
                            sessionEditor.commit();
                            System.out.println(response.getString("username"));

                            //Send user to landing screen
                            Log.i("Sign In", "Successful sign in");
                            Intent i = new Intent(this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Log.e("Sign In", "Could not sign in user. (No server error)");
                            Toast.makeText(this, "There was an issue signing in. Please check your credentials and try again.", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e("Sign In", "Could not read the server response.\n" + e);
                    }
                    authingFlag = false;
                }
                , error -> {
                    Log.e("Sign In", "Could not sign in user.");
                    Toast.makeText(this, "Could not reach the server.", Toast.LENGTH_SHORT).show();
                    authingFlag = false;
                }
        );
        queue.add(signInReq);
    }

    private void register() {
        authingFlag = true;

        //Verify the passwords are the same
        if (!etPass.getText().toString().equals(etPassConf.getText().toString())){
            //If the passwords aren't the same, halt the registration and inform the users
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Create the registration
        JSONObject registration = new JSONObject();
        try {
            registration.put("username", etUsername.getText());
            registration.put("email",etEmail.getText());
            registration.put("password", hashPass()); //This helper function returns the hashed password
        } catch (JSONException e) {
            Log.e("Register", "Failed to create the registration JSON");
            authingFlag = false;
            return;
        }

        //Send the registration request to the server and get response
        JsonObjectRequest regiReq = new JsonObjectRequest(Request.Method.POST, url + "/register", registration,
                response -> {
                    try {
                        int successFlag = response.getInt("success");
                        if (successFlag == 1){
                            //Save session to sharedprefs
                            sessionEditor.putString("SessionKey", response.getString("sessionID"));
                            sessionEditor.putString("username", response.getString("username"));
                            sessionEditor.commit();

                            //Send user to landing screen
                            Log.i("Register", "Successful registration");
                            Intent i = new Intent(this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Log.e("Register", "Could not register the user. (No error)");
                        }
                    } catch (JSONException e) {
                        Log.e("Register", "Could not read server response.\n" + e);
                        Toast.makeText(this, "There was an issue registering. Please try again using a different username and email.", Toast.LENGTH_LONG).show();
                    }
                    authingFlag = false;
                }
                , error -> {
                    Log.e("Register", "Could not register user.\n" + error);
                    Toast.makeText(this, "Could not reach the server.", Toast.LENGTH_SHORT).show();
                    authingFlag = false;
                }
        );
        queue.add(regiReq);
    }

    //Hash the user's password and return it
    private String hashPass(){
        //Get the password
        String pass = etPass.getText().toString();

        //Hash the password
        try {
            MessageDigest hasher = MessageDigest.getInstance("MD5"); //Establish the hashing algorithm
            hasher.update(pass.getBytes()); //Hash the password
            byte[] digest = hasher.digest(); //Get the hash digest
            String passHash = new String(digest); //Store the hashed password
            return passHash;
        } catch (NoSuchAlgorithmException e) {
            Log.e("Pass Hash", "Failed to hash the password.");
            return null;
        }
    }
}
