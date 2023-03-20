package com.example.notconstraintlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.notconstraintlayout.UserProfile;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<UserProfile> {

    private ArrayList<UserProfile> profiles;
    private Context context;

    public CustomList(Context context, ArrayList<UserProfile> profiles){
        super(context,0, profiles);
        this.profiles = profiles;
        this.context = context;
    }
}