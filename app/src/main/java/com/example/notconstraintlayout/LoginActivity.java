package com.example.notconstraintlayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    final String TAG = "Sample";

    Button addProfileButton;
    EditText usernameEditText;
    EditText userContactEditText;

    ArrayList<UserProfile> userProfileList;
    ArrayAdapter<UserProfile> UserProfileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        addProfileButton = findViewById(R.id.add_profile_button);
        usernameEditText = findViewById(R.id.User_name);
        userContactEditText = findViewById(R.id.User_phone);


        userProfileList = new ArrayList<>();
        UserProfileAdapter = new CustomList(this, userProfileList);

        final CollectionReference collectionReference = db.collection("Profiles");

        addProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameEditText.getText().toString();
                final String userContact = userContactEditText.getText().toString();

                HashMap<String, String> data = new HashMap<>();
                if (username.length()>0 && userContact.length()>0) {
                    data.put("Contact Info", userContact);
                    collectionReference
                            .document(username)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d(TAG, "Data has been added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d(TAG, "Data could not be added!" + e.toString());
                                }
                            });
                    usernameEditText.setText("");
                    userContactEditText.setText("");
                }
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                userProfileList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get("Username")));
                    String username = doc.getId();
                    String contactInfo = (String) doc.getData().get("Contact Info");
                    userProfileList.add(new UserProfile(username, contactInfo)); // Adding the cities and provinces from FireStore
                }
                UserProfileAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });
    }
}