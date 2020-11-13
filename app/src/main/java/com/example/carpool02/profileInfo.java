package com.example.carpool02;

import java.util.Date;

public class profileInfo {
    String name,email,phoneNUmber,gender,carModel,carNumber,userId,diplayPictureUrl;
    Date dob;
    public profileInfo(){

    }

    public profileInfo(String Name, String email, String phoneNUmber, String gender, String carModel, String carNumber, String userId, String diplayPictureUrl, Date dob) {
        this.name = Name;
        this.email = email;
        this.phoneNUmber = phoneNUmber;
        this.gender = gender;
        this.carModel = carModel;
        this.carNumber = carNumber;
        this.userId = userId;
        this.diplayPictureUrl = diplayPictureUrl;
        this.dob = dob;
    }

    public String getUserId() {
        return userId;
    }

    public String getDiplayPictureUrl() {
        return diplayPictureUrl;
    }

    public Date getDob() {
        return dob;
    }

    public String getname() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNUmber() {
        return phoneNUmber;
    }

    public String getGender() {
        return gender;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarNumber() {
        return carNumber;
    }
}
