package com.example.smartpark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smartpark.Models.ParkingLot;

import java.util.ArrayList;

public class ParkingLotAdapter extends BaseAdapter {
    ArrayList<ParkingLot> parkingLots;
    Context context;


    public ParkingLotAdapter(Context context, ArrayList<ParkingLot> parkingLots)
    {
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
        TextView tvAvailability = view.findViewById(R.id.tvAvailability);
        TextView tvSafety = view.findViewById(R.id.tvSafety);
        TextView tvCleanliness = view.findViewById(R.id.tvCleanliness);
        tvLotName.setText("Lot Name: "+p.name);
        tvAvailability.setText("Availability: "+p.avgAvailability);
        tvSafety.setText("Safety: "+p.avgSafety);
        tvCleanliness.setText("Cleanliness: "+p.avgCleanliness);

        return view;
    }


}
