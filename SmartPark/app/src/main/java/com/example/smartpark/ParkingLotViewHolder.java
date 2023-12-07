package com.example.smartpark;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ParkingLotViewHolder extends RecyclerView.ViewHolder{
    TextView tvLotName;
    TextView tvDistance;
    TextView tvRating;
    public ParkingLotViewHolder(@NonNull View itemView) {
        super(itemView);
        tvDistance = itemView.findViewById(R.id.tvSafety);
        tvLotName = itemView.findViewById(R.id.tvLotName);
        tvRating = itemView.findViewById(R.id.tvAvailability);

    }
}
