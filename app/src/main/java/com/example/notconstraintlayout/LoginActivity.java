package com.example.notconstraintlayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;

    final String TAG = "Sample";

    Button addProfileButton;
    EditText usernameEditText;
    EditText userContactEditText;

    UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        mAuth = FirebaseAuth.getInstance();

        addProfileButton = findViewById(R.id.add_profile_button);
        usernameEditText = findViewById(R.id.User_name);
        userContactEditText = findViewById(R.id.User_phone);
        userProfile = new UserProfile(this);

        SharedPreferences sharedPreferences = getSharedPreferences("localdata", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");

        if (!TextUtils.isEmpty(uid)) {
            // User ID exists in SharedPreferences, check if it exists in Firestore
            DocumentReference userRef = db.collection("Profiles").document(uid);
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // User profile exists in Firestore, go to MainActivity
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // User profile does not exist in Firestore, show login page
                            addProfileButton.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.e(TAG, "Error getting user profile data: ", task.getException());
                    }
                }
            });
        } else {
            // User ID does not exist in SharedPreferences, show login page
            addProfileButton.setVisibility(View.VISIBLE);
        }

        addProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameEditText.getText().toString();
                String phoneNumber = userContactEditText.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    phoneNumber = "1234567890"; // default phone number if not provided
                }
                final String email = username + "@example.com";
                final String password = username + phoneNumber;

                String finalPhoneNumber = phoneNumber;
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UserProfile
                                    userProfile.setUsername(username);
                                    userProfile.setContactInfo(Long.parseLong(finalPhoneNumber));
                                    HashMap<String, Object> data = new HashMap<>();
                                    data.put("username", username);
                                    data.put("contactInfo", Long.parseLong(finalPhoneNumber));
                                    db.collection("Profiles")
                                            .document(mAuth.getCurrentUser().getUid())
                                            .set(data)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "Data has been added successfully!");
                                                    } else {
                                                        Log.d(TAG, "Data could not be added! " + task.getException());
                                                    }
                                                }
                                            });
                                    SharedPreferences sharedPreferences = getSharedPreferences("localdata", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("uid", mAuth.getCurrentUser().getUid());
                                    editor.putString("username", username);
                                    editor.apply();
                                    Toast.makeText(LoginActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Log.e(TAG, "Registration failed! " + errorMessage);
                                    Toast.makeText(LoginActivity.this, "Registration failed! Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}

