package com.example.smartpark.Models;

import java.time.LocalDateTime;

public class Rating
{
    private String ID;
    private String username; //Should this just be user name?
    private String parkingLotID;
    private int cleanliness;
    private int safety;
    private int availability;
    private LocalDateTime timestamp;
    private String notes;

    public Rating(String ID, String username, String parkingLot, int cleanliness, int safety, int availability, LocalDateTime timestamp, String notes) {
        this.ID = ID;
        this.username = username;
        this.parkingLotID = parkingLot;
        this.cleanliness = cleanliness;
        this.safety = safety;
        this.availability = availability;
        this.timestamp = timestamp;
        this.notes = notes;
    }
}
