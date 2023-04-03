package com.example.notconstraintlayout.ui.dashboard;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;

import com.example.notconstraintlayout.R;
import com.example.notconstraintlayout.UserProfile;

public class EditFragment extends DialogFragment {

    private EditText nameEditText;
    private EditText phoneEditText;
    private Button saveButton;
    private Button cancelButton;
    private CheckBox hideInfoCheckBox;
    private UserProfile userProfile;

    public void EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_dashboard_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditText = view.findViewById(R.id.edit_name);
        phoneEditText = view.findViewById(R.id.edit_phone);
        saveButton = view.findViewById(R.id.save_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        hideInfoCheckBox = view.findViewById(R.id.hide_info_checkbox);

        userProfile = new UserProfile(getContext());

        loadUserProfile();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserProfile();

            }
        });
    }

    private void loadUserProfile() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);

        String username = sharedPreferences.getString("username", "");
        int contactInfo = sharedPreferences.getInt("contactInfo", 0);

        nameEditText.setText(username);
        phoneEditText.setText(String.valueOf(contactInfo));
    }

    private void saveUserProfile() {
        String newUsername = nameEditText.getText().toString();
        String newContactInfoString = phoneEditText.getText().toString();

        int newContactInfo = 0;
        if (!newContactInfoString.isEmpty()) {
            newContactInfo = Integer.parseInt(newContactInfoString);
        }

        userProfile.setUsername(newUsername);
        userProfile.setContactInfo(newContactInfo);

        if (hideInfoCheckBox.isChecked()) {
            Toast.makeText(getContext(), "Contact information hidden", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
        }
    }
}