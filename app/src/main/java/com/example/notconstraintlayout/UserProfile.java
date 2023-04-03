package com.example.notconstraintlayout;

import android.content.Context;

import java.util.ArrayList;

public class UserProfile {
    private long contactInfo;
    private String username;
    private int totalScore;
    private int totalScanned;
    private ArrayList<QrClass> scannedQrCodes;
    private Context context;
    public void removeQrCode(QrClass qrCodeToRemove) {
        if (scannedQrCodes != null && !scannedQrCodes.isEmpty()) {
            for (int i = 0; i < scannedQrCodes.size(); i++) {
                if (scannedQrCodes.get(i).getHash().equals(qrCodeToRemove.getHash())) {
                    scannedQrCodes.remove(i);
                    break;
                }
            }
        }
    }


    public UserProfile(Context context) {
        this.context = context;
    }

    public UserProfile() {
        // Set default values
        this.totalScore = 0;
        this.totalScanned = 0;
        this.scannedQrCodes = new ArrayList<>();
    }

    public UserProfile(Context context, String username, long contactInfo, int totalScore, int totalScanned, ArrayList<QrClass> scannedQrCodes) {
        this.context = context;
        this.username = username;
        this.contactInfo = contactInfo;
        this.totalScore = totalScore;
        this.totalScanned = totalScanned;
        this.scannedQrCodes = scannedQrCodes;
    }

    public void setContactInfo(long contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void setTotalScanned(int totalScanned) {
        this.totalScanned = totalScanned;
    }

    public void setScannedQrCodes(ArrayList<QrClass> scannedQrCodes) {
        this.scannedQrCodes = scannedQrCodes;
    }

    public long getContactInfo() {
        return contactInfo;
    }

    public String getUsername() {
        return username;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getTotalScanned() {
        return totalScanned;
    }

    public void addQrCode(QrClass qrCode) {
        scannedQrCodes.add(qrCode);
    }

    public ArrayList<QrClass> getScannedQrCodes() {
        return scannedQrCodes;
    }
}
