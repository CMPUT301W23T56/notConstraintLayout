package com.example.notconstraintlayout;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class QrCodeDBManager {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionRef = db.collection("QRCodes");

    public QrCodeDBManager() {
    }

    public void saveQRCodes(QrClass qrcode) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("location image", qrcode.getLocation_image());
        data.put("name", qrcode.getLocation());
        data.put("points", qrcode.getPoints());
        data.put("face", qrcode.getFace());
        data.put("location", qrcode.getLocation());

        collectionRef
                .document(qrcode.getName())
                .set(data)
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
//    public void getUsers(final QrCodeDBManager.OnUsersLoadedListener listener) {
//        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    List<QrClass> qrCodes = new ArrayList<>();
//                    for (DocumentSnapshot document : task.getResult()) {
//                        QrClass qrCode = document.toObject(QrClass.class);
//                        qrCodes.add(qrCode);
//                    }
//                    listener.onUsersLoaded(qrCodes);
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                    listener.onUsersLoaded(Collections.emptyList());
//                }
//            }
//        });
//    }

    public interface OnUsersLoadedListener {
        void onUsersLoaded(List<QrClass> userProfiles);
    }

}

