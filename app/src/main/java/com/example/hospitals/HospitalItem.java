package com.example.hospitals;

public class HospitalItem {
    private int imageResource;
    private String name;
    private String description;
    private String phoneNumber;
    private double latitude;
    private double longitude;

    public HospitalItem(int imageResource, String name, String description, String phoneNumber, double latitude, double longitude) {
        this.imageResource = imageResource;
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

