package com.example.notconstraintlayout.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notconstraintlayout.CaptureAct;
import com.example.notconstraintlayout.R;
import com.example.notconstraintlayout.databinding.FragmentDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    Button Edit;
    private static final int REQUEST_CODE_QR_SCAN = 0;
    FloatingActionButton scan_button;

    private List<String> scannedCodes;

    ActivityResultLauncher<ScanOptions> barCodeLauncher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Move the registerForActivityResult() method call to onAttach() method
        ScanOptions options = new ScanOptions();
        options.setPrompt("Please Scan the code");
        options.setBeepEnabled(true);
        options.setCaptureActivity(CaptureAct.class); // To capture QR code through the camera

        barCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
            // Show the result of scanned QR code in the text
            if (result.getContents() != null) {
                String name = calculateName(result.getContents());
                int score = computeScore(result.getContents());
                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setTitle("Score and Name")
                        .setMessage("Your score is: " + score + "\nYour Name is: " + name)
                        .setPositiveButton(android.R.string.ok, null)
                        .show(); // Show alertDialogue box to the user
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        scan_button = view.findViewById(R.id.my_fab);
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
                scanQrCode();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void scanQrCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Please Scan the code");
        options.setBeepEnabled(true);
        options.setCaptureActivity(CaptureAct.class); // To capture QR code through the camera

        // Launch the barcode scanner activity and handle the result using the activity result launcher
        barCodeLauncher.launch(options);
    }

    // Rest of the code



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the request code matches the code used to start the scanner
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() != null) {
                    // Handle the scanned result
                    Toast.makeText(getActivity(), result.getContents(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Scanning cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private int computeScore(String Value) {
        // Calculate SHA-256 hash of the QR Value contents
        String sha256 = "";   // This variable will be used to store the SHA-256 hash value
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Calculate hash values of the date
            byte[] hash = digest.digest(Value.getBytes(StandardCharsets.UTF_8)); // The Parameter 'Value' is first converted into byte array and then digest method is called to calculate the hash value
            sha256 = bytesToHex(hash);
        } catch (Exception e) {
        }

        // Find repeated digits in the SHA-256 hash
        Matcher matcher = Pattern.compile("([0-9a-f])\\1+").matcher(sha256);
        List<String> repeatedHexSequences = new ArrayList<>();
        while (matcher.find()) {
            repeatedHexSequences.add(matcher.group());
        }

        // Calculate score based on repeated digits
        int score = 0;
        for (String digit : repeatedHexSequences) {
            int i = digit.length();
            if (i >= 2 && i <= 4) {
                int points = (int) Math.pow(20, i - 1);
                if (digit.matches("[0]+")) { // Checks if the current element digit consists of one or more repeated 0 digits.
                    score = score + points;
                } else if (digit.matches("[1]+")) { // Checks if the current element digit consists of one or more repeated 1 digits
                    score = score + 1;
                } else if (digit.matches("[2-9a-f]+")) { // Checks if the current element digit consists of one or more non-zero hexadecimal digits (2-9, a-f)
                    score = score + Integer.parseInt(digit.substring(0, 1), 16);
                }
            }
        }
        return score;
    }

    // takes an array of bytes as input and returns a string in hexadecimal format
    private String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b)); // converts the current byte to a two-digit hexadecimal string [The String should have minimum length of 2 characters]
        }
        return hex.toString();
    }

    private String calculateName(String code) {
        String[] bit0 = {"cool", "hot"};
        String[] bit1 = {"Fro", "Glo"};
        String[] bit2 = {"Mo", "Lo"};
        String[] bit3 = {"Mega", "Ultra"};
        String[] bit4 = {"Spectral", "Sonic"};
        String[] bit5 = {"Crab", "Shark"};

        String sha256 = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(code.getBytes(StandardCharsets.UTF_8));
            sha256 = bytesToHex(hash);
        } catch (Exception e) {

        }
        StringBuilder hashNameBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int bit = (int) sha256.charAt(i) % 2;
            // Based on the value of the remainder, the code selects a word from one of six String arrays bit0 to bit5
            String[] bits = {bit0[bit], bit1[bit], bit2[bit], bit3[bit], bit4[bit], bit5[bit]};
            hashNameBuilder.append(bits[i]);
        }
        return hashNameBuilder.toString();
    }
}
