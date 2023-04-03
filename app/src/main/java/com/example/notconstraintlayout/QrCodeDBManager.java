package com.example.notconstraintlayout;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;
public class QrCodeDBManager {


    private FirebaseFirestore db= FirebaseFirestore.getInstance();;
    private CollectionReference qrCodesRef= db.collection("qrCodes");



    public QrCodeDBManager() {
    }

//    public void saveQRCodes(QrClass qrCode, OnCompleteListener<Void> onCompleteListener) {
//        qrCodesRef.document(qrCode.getHash())
//                .set(qrCode.toMap())
//                .addOnCompleteListener(onCompleteListener);
//    }

    public void saveQRCodes(QrClass qrCode, OnCompleteListener<Void> onCompleteListener) {
        final DocumentReference qrCodeRef = db.collection("qrCodes").document(qrCode.getHash());
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(qrCodeRef);

                if (snapshot.exists()) {
                    // If the QR code already exists, increment the scannedBy field by 1
                    qrCode.setScannedBy(qrCode.getScannedBy()+1);
                } else {
                    qrCodeRef
                        .set(qrCode.toMap())
                        .addOnCompleteListener(onCompleteListener);
                }

                return null;
            }
        }).addOnCompleteListener(onCompleteListener);
    }

//    public void saveQRCodes(QrClass qrcode) {
//        HashMap<String, Object> data = new HashMap<>();
//        data.put("location image", qrcode.getLocation_image());
//        data.put("name", qrcode.getLocation());
//        data.put("points", qrcode.getPoints());
//        data.put("face", qrcode.getFace());
//        data.put("location", qrcode.getLocation());
//
//        qrCodesRef
//                .document(qrcode.getName())
//                .set(data)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d(TAG, "User profile data has been saved successfully!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "Failed to save user profile data: ", e);
//                    }
//                });
//    }

    public void displayQrCodes(final QrCodeDBManager.OnUsersLoadedListener listener) {
        qrCodesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<QrClass> qrCodes = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        QrClass qrCode = document.toObject(QrClass.class);
                        qrCodes.add(qrCode);
                    }
                    listener.onUsersLoaded(qrCodes);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    listener.onUsersLoaded(Collections.emptyList());
                }
            }
        });
    }

    public interface OnUsersLoadedListener {
        void onUsersLoaded(List<QrClass> userProfiles);
    }
}



