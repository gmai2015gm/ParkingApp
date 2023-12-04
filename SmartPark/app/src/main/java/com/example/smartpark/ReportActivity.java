package com.example.smartpark;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ReportActivity extends AppCompatActivity
{
    TextView txtLotName, lblAvailable, lblBusiness, lblClean, lblSafety;
    SeekBar barAvailable, barBusiness, barClean, barSafety;
    Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        txtLotName = findViewById(R.id.txtLotName);
        lblAvailable = findViewById(R.id.lblAvailable);
        lblBusiness = findViewById(R.id.lblBusiness);
        lblClean = findViewById(R.id.lblClean);
        lblSafety = findViewById(R.id.lblSafety);
        barAvailable = findViewById(R.id.barAvailable);
        barBusiness = findViewById(R.id.barBusiness);
        barClean = findViewById(R.id.barClean);
        barSafety = findViewById(R.id.barSafety);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        /*  Progress Bar implementation Logic  */
        //  ---------------------------------

        lblAvailable.setText(barAvailable.getProgress()+" / 10");
        barAvailable.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lblAvailable.setText(i+" / 10");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        lblBusiness.setText(barBusiness.getProgress()+" / 10");
        barBusiness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lblBusiness.setText(i+" / 10");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        lblClean.setText(barClean.getProgress()+" / 10");
        barClean.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lblClean.setText(i+" / 10");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        lblSafety.setText(barSafety.getProgress()+" / 10");
        barSafety.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                lblSafety.setText(i+" / 10");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSave.setOnClickListener(v -> {
            //      LOGIC NOT FINAL. THE RATINGS WILL NEED
            //      TO BE SAVED FOR LATER.
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            finish();
        });

    }

}

