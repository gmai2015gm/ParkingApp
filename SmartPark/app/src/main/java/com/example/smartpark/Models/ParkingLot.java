package com.example.smartpark.Models;


import java.util.ArrayList;

public class ParkingLot {

    public String ID;
    public String name;
    public float latitude;
    public float longitude;
    public double avgCleanliness;
    public double avgSafety;
    public double rating;
    public double avgAvailability;
    public double distance;
    public ArrayList<Rating> ratings;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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
                ", rating=" + rating +
                ", avgAvailability=" + avgAvailability +
                ", distance=" + distance +
                ", ratings=" + ratings +
                '}';
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
    
}