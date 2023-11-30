package com.example.smartpark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpark.Models.ParkingLot;

import java.util.ArrayList;

public class ParkingLotAdapter extends RecyclerView.Adapter<ParkingLotViewHolder>{
    ArrayList<ParkingLot> parkingLots;
    Context context;


    public ParkingLotAdapter(Context context, ArrayList<ParkingLot> parkingLots){
        this.parkingLots = parkingLots;
        this.context = context;
    }
    @NonNull
    @Override
    public ParkingLotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_parking_lot, parent, false);
        return new ParkingLotViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingLotViewHolder holder, int position) {
        ParkingLot p = parkingLots.get(position);
        holder.tvLotName.setText("Lot Name: "+p.name);
        holder.tvRating.setText("Rating: "+p.avgAvailability);
        holder.tvDistance.setText("Distance: "+p.distance);
    }

    @Override
    public int getItemCount() {
        return parkingLots.size();
    }
}
