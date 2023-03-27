package com.example.notconstraintlayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class scanner extends AppCompatActivity {

    private static final int REQUEST_CODE_QR_SCAN = 0;
    Button scan_button;
    private List<String> scannedCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xyz);
        scannedCodes = new ArrayList<>();

        scan_button = findViewById(R.id.scan_button);
        scan_button.setOnClickListener(v ->
        {
            scanQrCode();
        });
    }

    private void scanQrCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Please Scan the code");
        options.setBeepEnabled(true);
        options.setCaptureActivity(CaptureAct.class);} // To capture QR code through the camera
//        barCodeLauncher.launch(options);
//    }
//
//    //  This sets up a mechanism for launching a barcode scanner activity and receiving its result, which can then be used to process the scanned barcode data in the app
//    ActivityResultLauncher<ScanOptions> barCodeLauncher = registerForActivityResult(new ScanContract(), result ->
//    {
//        // Show the result of scanned QR code in the text
//        if (result.getContents() != null) {
//            String name = calculateName(result.getContents());
//            int score = computeScore(result.getContents());
//            new AlertDialog.Builder(this)
//                    .setTitle("Score and Name")
//                    .setMessage("Your score is: " + score + "\nYour Name is: " + name)
//                    .setPositiveButton(android.R.string.ok, null)
//                    .show(); // Show alertDialogue box to the user
//        }
//    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scanning cancelled", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    // takes an array of bytes as input and returns a string in hexadecimal format



}
