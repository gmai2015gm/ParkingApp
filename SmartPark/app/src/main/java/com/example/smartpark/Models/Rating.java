package com.example.smartpark.Models;

import java.time.LocalDateTime;

public class Rating
{
    private String ID;
    private User user; //Should this just be user name?
    private ParkingLot parkingLot;
    private int cleanliness;
    private int safety;
    private int availability;
    private LocalDateTime timestamp;
    private String notes;

}
