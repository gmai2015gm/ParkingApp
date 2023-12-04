package com.example.smartpark;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AppInfo extends AppCompatActivity {

    ImageView ivCloseAppInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        ivCloseAppInfo = findViewById(R.id.ivCloseAppInfo);

        ivCloseAppInfo.setOnClickListener(v -> {
            finish();
        });
    }

}
