package com.example.smartpark;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class ReportActivity extends AppCompatActivity
{
    TextView txtLotName, lblClean, lblSafety;
    SeekBar barClean, barSafety;
    Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        txtLotName = findViewById(R.id.txtLotName);
        lblClean = findViewById(R.id.lblClean);
        lblSafety = findViewById(R.id.lblSafety);
        barClean = findViewById(R.id.barClean);
        barSafety = findViewById(R.id.barSafety);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);


        /*  Progress Bar implementation Logic  */
        //  ---------------------------------

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