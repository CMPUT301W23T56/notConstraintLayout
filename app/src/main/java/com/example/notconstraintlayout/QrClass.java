package com.example.notconstraintlayout;

import android.graphics.Bitmap;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class QrClass {
    private String hash;
    private GeoPoint location;
    private String name;
    private int points;
    private String face;
    private Bitmap location_image;

    private int scannedBy = 0;
    private byte[] imageData;

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public QrClass(String hash) {
        this.hash = hash;
    }

    public QrClass() {
        // Required empty constructor for Firestore deserialization
    }

    public QrClass(String name, int score, String contents) {
        this.name = name;
        this.points = score;
        this.hash = contents;
    }


    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
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

    public Map<String, Object> toMap() {
        Map<String, Object> qrCodeMap = new HashMap<>();
        qrCodeMap.put("location image", this.location_image);
        qrCodeMap.put("name", this.name);
        qrCodeMap.put("points", this.points);
        qrCodeMap.put("face", this.face);
        qrCodeMap.put("location", this.location);
        qrCodeMap.put("scannedBy", this.scannedBy);
        return qrCodeMap;
    }


    public String getHash() {
        return hash;
    }

    public int getScannedBy() {
        return scannedBy;
    }

    public void setScannedBy(int scannedBy) {
        this.scannedBy = scannedBy;
    }
}
