package com.example.smartpark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpark.Models.ParkingLot;

import java.util.ArrayList;

public class ParkingLotAdapter extends BaseAdapter {
    ArrayList<ParkingLot> parkingLots;
    Context context;


    public ParkingLotAdapter(Context context, ArrayList<ParkingLot> parkingLots){
        this.parkingLots = parkingLots;
        this.context = context;
    }

    @Override
    public int getCount() {
        return parkingLots.size();
    }

    @Override
    public Object getItem(int position) {
        return parkingLots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.layout_parking_lot,parent, false);

        }
        ParkingLot p = parkingLots.get(position);
        TextView tvLotName = view.findViewById(R.id.tvLotName);
        TextView tvRating = view.findViewById(R.id.tvRating);
        TextView tvDistance = view.findViewById(R.id.tvDistance);
        tvLotName.setText("Lot Name: "+p.name);
        tvRating.setText("Rating: "+p.avgAvailability);
        tvDistance.setText("Distance: ");

        return view;
    }


    public void updateData(ArrayList<ParkingLot> newParkingLots) {
        parkingLots.clear(); // Clear existing data
        parkingLots.addAll(newParkingLots); // Add new data
        notifyDataSetChanged(); // Notify the adapter to refresh the view
    }
}
