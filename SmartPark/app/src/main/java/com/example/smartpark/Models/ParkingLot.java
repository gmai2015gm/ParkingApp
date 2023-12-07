package com.example.smartpark.Models;

import java.util.ArrayList;

public class ParkingLot
{
    public String ID;
    public String name;
    public float latitude;
    public float longitude;
    public double avgCleanliness;
    public double avgSafety;
    public double avgAvailability;
    public ArrayList<Rating> ratings;
    public ParkingLot()
    {
    }

    public ParkingLot(String ID, String name, float latitude, float longitude, double avgCleanliness, double avgSafety, double avgAvailability, ArrayList<Rating> ratings) {
        this.ID = ID;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.avgCleanliness = avgCleanliness;
        this.avgSafety = avgSafety;
        this.avgAvailability = avgAvailability;
        this.ratings = ratings;
    }


    @Override
    public String toString() {
        return "ParkingLot{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", avgCleanliness=" + avgCleanliness +
                ", avgSafety=" + avgSafety +
                ", avgAvailability=" + avgAvailability +
                ", ratings=" + ratings +
                '}';
    }

    public double getAvgCleanliness() {
        return avgCleanliness;
    }

    public void setAvgCleanliness(double avgCleanliness) {
        this.avgCleanliness = avgCleanliness;
    }

    public double getAvgSafety() {
        return avgSafety;
    }

    public void setAvgSafety(double avgSafety) {
        this.avgSafety = avgSafety;
    }

    public double getAvgAvailability() {
        return avgAvailability;
    }

    public void setAvgAvailability(double avgAvailability) {
        this.avgAvailability = avgAvailability;
    }
}


