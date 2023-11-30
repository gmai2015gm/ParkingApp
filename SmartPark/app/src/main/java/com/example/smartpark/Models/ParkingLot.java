package com.example.smartpark.Models;

import java.util.ArrayList;

public class ParkingLot
{
    private String ID;
    public String name;
    public float latitude;
    public float longitude;
    public double avgCleanliness;
    public double avgSafety;
    public double avgAvailability;
    public double distance;
    public ArrayList<Rating> ratings;
}
