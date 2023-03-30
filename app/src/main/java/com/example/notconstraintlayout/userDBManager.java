package com.example.notconstraintlayout;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class userDBManager {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId;
    private DocumentReference userDocRef;
    private Context context;

    public userDBManager(Context context) {
        this.context = context;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not authenticated, go to LoginActivity
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            return;
        }
        userId = currentUser.getUid();
        userDocRef = db.collection("Profiles").document(userId);
    }

    public void saveUserProfile(UserProfile userProfile) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", userProfile.getUsername());
        data.put("contactInfo", userProfile.getContactInfo());
        data.put("totalScore", userProfile.getTotalScore());
        data.put("totalScanned", userProfile.getTotalScanned());
        data.put("scannedQrCodes", userProfile.getScannedQrCodes());

        userDocRef.set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "User profile data has been saved successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed to save user profile data: ", e);
                    }
                });
    }

    public void getUserProfile(final OnUserProfileLoadedListener listener) {
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserProfile userProfile = document.toObject(UserProfile.class);
                        listener.onUserProfileLoaded(userProfile);
                    } else {
                        Log.d(TAG, "User profile document not found.");
                        listener.onUserProfileLoaded(null);
                    }
                } else {
                    Log.d(TAG, "Failed to retrieve user profile data: ", task.getException());
                    listener.onUserProfileLoaded(null);
                }
            }
        });
    }

    public void updateProfile(UserProfile userProfile, OnQrCodeAddedListener listener) {
        DocumentReference userDocRef = db.collection("users").document(userId);
        userDocRef.set(userProfile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "User profile updated.");
                listener.onQrCodeAdded();
            } else {
                Log.d(TAG, "User profile update failed.");
            }
        });
    }

    public void addQrCode(QrClass qrCode, OnQrCodeAddedListener listener, OnQrCodesChangedListener qrCodesChangedListener) {
        getUserProfile(new OnUserProfileLoadedListener() {
            @Override
            public void onUserProfileLoaded(UserProfile userProfile) {
                if (userProfile != null) {
                    userProfile.addQrCode(qrCode);
                    saveUserProfile(userProfile);
                    listener.onQrCodeAdded();
                    qrCodesChangedListener.onQrCodesChanged(userProfile.getScannedQrCodes());
                } else {
                    Log.d(TAG, "Failed to load user profile.");
                }
            }
        });
    }


    public interface OnUserProfileLoadedListener {
        void onUserProfileLoaded(UserProfile userProfile);
    }

    public interface OnQrCodeAddedListener {
        void onQrCodeAdded();
    }

    public interface OnQrCodesChangedListener {
        void onQrCodesChanged(List<QrClass> qrCodes);
    }

}
