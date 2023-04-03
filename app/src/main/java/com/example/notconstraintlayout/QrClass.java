package com.example.notconstraintlayout;

import android.graphics.Bitmap;

public class QrClass {
    private String hash;
    private com.google.android.gms.maps.model.LatLng location;
    private String name;
    private int points;
    private String face;
    private Bitmap location_image;

    public QrClass(String hash) {
        this.hash = hash;
    }

    public QrClass(String name, com.google.android.gms.maps.model.LatLng location, int points) {
        this.name = name;
        this.location = location;
        this.points = points;
    }
    public QrClass() {
        // Required empty constructor for Firestore deserialization
    }


    public QrClass(String name, int points) {
        this.name = name;
        this.points = points;
    }


    public com.google.android.gms.maps.model.LatLng getLocation() {
        return location;
    }

    public void setLocation(com.google.android.gms.maps.model.LatLng location) {
        this.location = location;
    }

    public Bitmap getLocation_image() {
        return location_image;
    }

    public void setLocation_image(Bitmap location_image) {
        this.location_image = location_image;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public String getFace() {
        return face;
    }


}
