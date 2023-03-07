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
        options.setCaptureActivity(CaptureAct.class); // To capture QR code through the camera
        barCodeLauncher.launch(options);
    }

    //  This sets up a mechanism for launching a barcode scanner activity and receiving its result, which can then be used to process the scanned barcode data in the app
    ActivityResultLauncher<ScanOptions> barCodeLauncher = registerForActivityResult(new ScanContract(), result ->
    {
        // Show the result of scanned QR code in the text
        if (result.getContents() != null) {
            String name = calculateName(result.getContents());
            int score = computeScore(result.getContents());
            new AlertDialog.Builder(this)
                    .setTitle("Score and Name")
                    .setMessage("Your score is: " + score + "\nYour Name is: " + name)
                    .setPositiveButton(android.R.string.ok, null)
                    .show(); // Show alertDialogue box to the user
        }
    });

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

    private int computeScore(String Value) {
        // Calculate SHA-256 hash of the QR Value contents
        String sha256 = "";   // This variable will be used to store the SHA-256 hash value
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // Calculate hash values of the date
            byte[] hash = digest.digest(Value.getBytes(StandardCharsets.UTF_8)); // The Parameter 'Value' is first converted into byte array and then digest method is called to calculate the hash value
            sha256 = bytesToHex(hash);
        } catch (Exception e) {}

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
                    score = score +  1;
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
