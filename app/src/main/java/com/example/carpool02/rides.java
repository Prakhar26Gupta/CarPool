package com.example.carpool02;

import java.util.Date;

public class rides {
    String pickUpLocation;
    String destinationLocation;
    String userId;
    Integer noOfAvailableSeats;
    Integer hourOfBooking;
    Integer minuteOfBooking;
    Date dateOfBooking;


    public  rides(){

    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getNoOfAvailableSeats() {
        return noOfAvailableSeats;
    }

    public Integer getHourOfBooking() {
        return hourOfBooking;
    }

    public Integer getMinuteOfBooking() {
        return minuteOfBooking;
    }

    public Date getDateOfBooking() {
        return dateOfBooking;
    }

    public rides(String pickUpLocation, String destinationLocation, String userId, Integer noOfAvailableSeats, Integer hourOfBooking, Integer minuteOfBooking, Date dateOfBooking) {
        this.pickUpLocation = pickUpLocation;
        this.destinationLocation = destinationLocation;
        this.userId = userId;
        this.noOfAvailableSeats = noOfAvailableSeats;
        this.hourOfBooking = hourOfBooking;
        this.minuteOfBooking = minuteOfBooking;
        this.dateOfBooking = dateOfBooking;
    }
}
