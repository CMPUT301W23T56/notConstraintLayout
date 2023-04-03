package com.example.notconstraintlayout;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;

import com.example.notconstraintlayout.ui.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
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
        if (userDocRef != null) {
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
        } else {
            Log.d(TAG, "User document reference is null.");
            listener.onUserProfileLoaded(null);
        }
    }

    public void updateProfile(String newUsername, String newPhoneNumber, OnUserProfileUpdatedListener listener) {
        getUserProfile(new OnUserProfileLoadedListener() {
            @Override
            public void onUserProfileLoaded(UserProfile userProfile) {
                if (userProfile != null) {
                    userProfile.setUsername(newUsername);
                    userProfile.setContactInfo(Long.parseLong(newPhoneNumber));
                    saveUserProfile(userProfile);

                    if (listener != null) {
                        listener.onUserProfileUpdated(userProfile);
                    }
                } else {
                    Log.d(TAG, "Failed to load user profile.");

                    if (listener != null) {
                        listener.onUserProfileUpdated(null);
                    }
                }
            }
        });
    }

    public void getUsers(final OnUsersLoadedListener listener) {
        db.collection("Profiles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<UserProfile> userProfiles = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        UserProfile userProfile = document.toObject(UserProfile.class);
                        userProfiles.add(userProfile);
                    }
                    listener.onUsersLoaded(userProfiles);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    listener.onUsersLoaded(Collections.emptyList());
                }
            }
        });
    }

    public interface OnUsersLoadedListener {
        void onUsersLoaded(List<UserProfile> userProfiles);
    }
    public void removeQrCode(QrClass qrCodeToRemove, OnQrCodeRemovedListener removedListener, OnQrCodesChangedListener qrCodesChangedListener) {
        getUserProfile(new OnUserProfileLoadedListener() {
            @Override
            public void onUserProfileLoaded(UserProfile userProfile) {
                if (userProfile != null) {
                    userProfile.removeQrCode(qrCodeToRemove);
                    userProfile.setTotalScanned(userProfile.getTotalScanned() - 1);
                    userProfile.setTotalScore(userProfile.getTotalScore() - qrCodeToRemove.getPoints());
                    saveUserProfile(userProfile);
                    removedListener.onQrCodeRemoved();
                    qrCodesChangedListener.onQrCodesChanged(userProfile.getScannedQrCodes());
                } else {
                    Log.d(TAG, "Failed to load user profile.");
                }
            }
        });
    }

    public interface OnQrCodeRemovedListener {
        void onQrCodeRemoved();
    }


    public void addQrCode(QrClass qrCode, OnQrCodeAddedListener listener, OnQrCodesChangedListener qrCodesChangedListener) {
        getUserProfile(new OnUserProfileLoadedListener() {
            @Override
            public void onUserProfileLoaded(UserProfile userProfile) {
                if (userProfile != null) {
                    userProfile.addQrCode(qrCode);
                    userProfile.setTotalScanned(userProfile.getTotalScanned() + 1);
                    userProfile.setTotalScore(userProfile.getTotalScore() + qrCode.getPoints());
                    saveUserProfile(userProfile);
                    listener.onQrCodeAdded();
                    qrCodesChangedListener.onQrCodesChanged(userProfile.getScannedQrCodes());

                    // Update the UI immediately
                    NavHostFragment navHostFragment = (NavHostFragment) ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
                    DashboardFragment fragment = (DashboardFragment) navHostFragment.getChildFragmentManager().getFragments().get(0);
                    if (fragment != null) {
                        fragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView scoreTextView = fragment.getView().findViewById(R.id.Scorevalue);
                                TextView scannedTextView = fragment.getView().findViewById(R.id.Scannedvalue);
                                scoreTextView.setText(String.valueOf(userProfile.getTotalScore()));
                                scannedTextView.setText(String.valueOf(userProfile.getTotalScanned()));
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "Failed to load user profile.");
                }
            }
        });
    }


    public void addUserDocumentListener(final OnUserDocumentUpdatedListener listener) {
        if (userDocRef != null) {
            userDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        UserProfile userProfile = snapshot.toObject(UserProfile.class);
                        listener.onUserDocumentUpdated(userProfile);
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        } else {
            Log.d(TAG, "User document reference is null.");
            listener.onUserDocumentUpdated(null);
        }
    }


    public interface OnUserDocumentUpdatedListener {
        void onUserDocumentUpdated(UserProfile userProfile);
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
    public interface OnAllUserProfilesLoadedListener {
        void onAllUserProfilesLoaded(ArrayList<UserProfile> userProfiles);
    }
    public interface OnUserProfileUpdatedListener {
        void onUserProfileUpdated(UserProfile userProfile);
    }


}