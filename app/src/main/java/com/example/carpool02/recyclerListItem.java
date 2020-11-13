package com.example.carpool02;

public class recyclerListItem {

    String Name,date,time,imageUrl;

    public recyclerListItem(String name, String date, String time, String imageUrl) {
        Name = name;
        this.date = date;
        this.time = time;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
