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

    }
}
