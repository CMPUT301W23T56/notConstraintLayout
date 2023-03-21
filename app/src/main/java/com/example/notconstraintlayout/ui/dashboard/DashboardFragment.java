package com.example.notconstraintlayout.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notconstraintlayout.databinding.FragmentDashboardBinding;
import com.google.zxing.integration.android.IntentIntegrator;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    Button Edit;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.username;
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("name", "defaultUsername");
        textView.setText(username);

        binding.myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(DashboardFragment.this);
                integrator.setPrompt("Please Scan the code");
                integrator.setBeepEnabled(true);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}