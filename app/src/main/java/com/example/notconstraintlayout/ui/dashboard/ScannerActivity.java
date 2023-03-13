package com.example.notconstraintlayout.ui.dashboard;

import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notconstraintlayout.CaptureAct;
import com.example.notconstraintlayout.QrClass;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class ScannerActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_QR_SCAN = 0;
    Button scan_button;
    private List<String> scannedCodes;

    public ScannerActivity() {
        scannedCodes = new ArrayList<>();
    }

    public void scanQrCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Please Scan the code");
        options.setBeepEnabled(true);
        options.setCaptureActivity(CaptureAct.class); // To capture QR code through the camera
        barCodeLauncher.launch(options);
    }

    //  This sets up a mechanism for launching a barcode scanner activity and receiving its result, which can then be used to process the scanned barcode data in the app
    ActivityResultLauncher<ScanOptions> barCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            QrClass qr = new QrClass(result.getContents());
            String name = qr.calculateName(result.getContents());
            int score = qr.computeScore(result.getContents());
            ScanResultFragment.newInstance(score, name).show(getSupportFragmentManager(), null);
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

}
