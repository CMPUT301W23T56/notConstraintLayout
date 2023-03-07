package com.example.notconstraintlayout;

import java.util.ArrayList;

public class UserProfile {
    private String contactInfo;
    private String username;
    private int totalScore;
    private int totalScanned;
    private ArrayList<QrCode> scannedQrCodes;

    public UserProfile(String contactInfo, String username) {
        this.contactInfo = contactInfo;
        this.username = username;
    }

    public UserProfile() {
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalScanned() {
        return totalScanned;
    }

    public void setTotalScanned(int totalScanned) {
        this.totalScanned = totalScanned;
    }

    public ArrayList<QrCode> getScannedQrCodes() {
        return scannedQrCodes;
    }

    public void setScannedQrCodes(ArrayList<QrCode> scannedQrCodes) {
        this.scannedQrCodes = scannedQrCodes;
    }
}
