package com.example.notconstraintlayout.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notconstraintlayout.databinding.FragmentDashboardBinding;
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ScannerActivity scannerActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.notconstraintlayout.ui.dashboard.DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(com.example.notconstraintlayout.ui.dashboard.DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();

        Button button = binding.button2;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            scannerActivity = new ScannerActivity();
            scannerActivity.scanQrCode();
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