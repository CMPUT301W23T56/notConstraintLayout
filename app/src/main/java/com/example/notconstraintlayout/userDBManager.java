package com.example.notconstraintlayout;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class userDBManager {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId;
    private DocumentReference userDocRef;
    private Context context;

    public userDBManager(Context context) {
        this.context = context;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
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



    public interface OnUserProfileLoadedListener {
        void onUserProfileLoaded(UserProfile userProfile);
    }
}
