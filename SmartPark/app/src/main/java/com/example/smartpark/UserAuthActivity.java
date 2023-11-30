package com.example.smartpark;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserAuthActivity extends AppCompatActivity {
    Button btnSignIn, btnRegister, btnAuth, btnSwitch;
    EditText etUsername, etEmail, etPass, etPassConf;
    boolean firstTimeUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_auth_activity);

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

        //Hide the original sign in button
        btnSignIn.setVisibility(View.INVISIBLE);
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

        //Hide the original sign in button
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
        //While everything above was cosmetic, this does the heavy lifting.

    }
}
