package com.example.notconstraintlayout;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.HashMap;

public class QrCodeDBManager {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionRef = db.collection("QRCodes");

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
}
