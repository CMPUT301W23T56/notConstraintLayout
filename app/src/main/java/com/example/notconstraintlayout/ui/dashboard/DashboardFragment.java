package com.example.notconstraintlayout.ui.dashboard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.notconstraintlayout.QrClass;
import com.example.notconstraintlayout.R;
import com.example.notconstraintlayout.databinding.FragmentDashboardBinding;
import com.example.notconstraintlayout.scanner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    public Button Edit;
    FloatingActionButton my_fab;
    private Activity view;




    // Get the name of the QR code


    // Compute the score of the QR code





    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.username;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username = sharedPreferences.getString("name", "Ada Lovelace");
        textView.setText(username);
        // Pass the context to the getText method
        dashboardViewModel.getText(requireContext()).observe(getViewLifecycleOwner(), textView::setText);






        binding.myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(DashboardFragment.this);


                integrator.setPrompt("Please Scan the code");
                integrator.setBeepEnabled(true);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();





                ScanResultFragment scanResultFragment = ScanResultFragment.newInstance(0,"H");

                // Show the ScanResultFragment
                FragmentManager fragmentManager = getParentFragmentManager();
                scanResultFragment.show(fragmentManager, "scan_result");



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