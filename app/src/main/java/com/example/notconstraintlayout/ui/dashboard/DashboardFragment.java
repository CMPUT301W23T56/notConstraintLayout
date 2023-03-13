package com.example.notconstraintlayout.ui.dashboard;

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
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.username;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username = sharedPreferences.getString("name", "Ada Lovelace");
        textView.setText(username);
        // Pass the context to the getText method
        dashboardViewModel.getText(requireContext()).observe(getViewLifecycleOwner(), textView::setText);

////      final TextView textView = binding.textDashboard;
//        final TextView textView = binding.name;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Get the clicked item
//                Object item = parent.getItemAtPosition(position);
//
//                // Handle the click event
//                //Toast.makeText(getApplicationContext(), "Clicked item: " + item.toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
        //FloatingActionButton fab;
//FloatingActionButton fab;
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