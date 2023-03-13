package com.example.notconstraintlayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

public class UserProfile {
    private int contactInfo;
    private String username;
    private int totalScore;
    private int totalScanned;
    private ArrayList<QrClass> scannedQrCodes;
    private Context context;

    public UserProfile(View.OnClickListener context) {
        this.context = context;
    }

    public void setContactInfo(int contactInfo) {
        this.contactInfo = contactInfo;
        saveUserDetails();
    }

    public void setUsername(String username) {
        this.username = username;
        saveUserDetails();
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
        saveUserDetails();
    }

    public void setTotalScanned(int totalScanned) {
        this.totalScanned = totalScanned;
        saveUserDetails();
    }

    public void setScannedQrCodes(ArrayList<QrClass> scannedQrCodes) {
        this.scannedQrCodes = scannedQrCodes;
        saveUserDetails();
    }

    private void saveUserDetails() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("contactInfo", contactInfo);
        editor.putString("username", username);
        editor.putInt("totalScore", totalScore);
        editor.putInt("totalScanned", totalScanned);
        Gson gson = new Gson();
        String qrCodesJson = gson.toJson(scannedQrCodes);
        editor.putString("scannedQrCodes", qrCodesJson);
        editor.apply();
    }
}
