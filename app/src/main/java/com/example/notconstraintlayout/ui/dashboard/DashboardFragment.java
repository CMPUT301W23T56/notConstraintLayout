package com.example.notconstraintlayout.ui.dashboard;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.notconstraintlayout.CaptureAct;
import com.example.notconstraintlayout.QrClass;
import com.example.notconstraintlayout.QrCodeAdapter;
import com.example.notconstraintlayout.QrCodeDBManager;
import com.example.notconstraintlayout.R;
import com.example.notconstraintlayout.UserProfile;
import com.example.notconstraintlayout.databinding.FragmentDashboardBinding;
import com.example.notconstraintlayout.userDBManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class DashboardFragment extends Fragment implements userDBManager.OnUserDocumentUpdatedListener {

    private FragmentDashboardBinding binding;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private ListView mListView;
    private QrCodeAdapter mAdapter;
    private ArrayList<QrClass> mQrCodes = new ArrayList<>();
    private static final int REQUEST_CODE_QR_SCAN = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public String name;
    public int score;
    public String face;

    FloatingActionButton scan_button;

    private List<String> scannedCodes;

    private TextView scoreTextView;
    private TextView scannedTextView;

    private TextView textView;
    ActivityResultLauncher<ScanOptions> barCodeLauncher;
    byte[] imageData; // Get the compressed image data
    public QrCodeDBManager qrDb;
    QrClass qrClass;

    public int scannedBy = 0;
    private String currentPhotoPath;
    private StorageReference storageRef;
    Bitmap img;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        textView = binding.username;
        scoreTextView = binding.Scorevalue;
        scannedTextView = binding.Scannedvalue;

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        mListView = binding.dashboardlist;
        mAdapter = new QrCodeAdapter(requireContext(), mQrCodes);
        mListView.setAdapter(mAdapter);
        if (savedInstanceState != null) {
            currentPhotoPath = savedInstanceState.getString("currentPhotoPath");
        }

        userDBManager userDBManager = new userDBManager(requireContext());
        userDBManager.addUserDocumentListener(this);
        userDBManager.getUserProfile(new userDBManager.OnUserProfileLoadedListener() {
            @Override
            public void onUserProfileLoaded(UserProfile userProfile) {
                if (userProfile != null) {
                    String username = userProfile.getUsername();
                    textView.setText(username);
                    scoreTextView.setText(String.valueOf(userProfile.getTotalScore()));
                    scannedTextView.setText(String.valueOf(userProfile.getTotalScanned()));
                    mQrCodes.clear();
                    mQrCodes.addAll(userProfile.getScannedQrCodes());
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Failed to load user profile.");
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QrClass qrCodeToDelete = mQrCodes.get(position);

                // Inflate the custom dialog layout
                LayoutInflater inflater = LayoutInflater.from(requireContext());
                View dialogView = inflater.inflate(R.layout.qr_code_details_dialog, null);
                if (dialogView != null) {
                    ImageView locationImageView = dialogView.findViewById(R.id.location_image_view);
                    Bitmap locationImage = qrCodeToDelete.getLocation_image();
                    // set the locationImage to the ImageView here
                }

                // Get the ImageView and set the image
                ImageView locationImageView = dialogView.findViewById(R.id.location_image_view);
                Bitmap locationImage = qrCodeToDelete.getLocation_image();
                String imageUrl = qrCodeToDelete.getImageUrl(); // Get the image URL from the qrClass

                if (imageUrl != null) {
                    Glide.with(requireContext())
                            .load(imageUrl)
                            .into(locationImageView);
                }

                // Create and show the AlertDialog
                new AlertDialog.Builder(requireContext())
                        .setMessage("Name: " + qrCodeToDelete.getName() + "\nScore: " + qrCodeToDelete.getPoints() + "\nScanned by " + qrCodeToDelete.getScannedBy() + " others" + "\n" + qrCodeToDelete.getFace())
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the selected QR code and update the list
                                QrClass qrCodeToDelete = mQrCodes.get(position);
                                userDBManager.removeQrCode(qrCodeToDelete, new userDBManager.OnQrCodeRemovedListener() {
                                    @Override
                                    public void onQrCodeRemoved() {
                                        Log.d(TAG, "QR code removed.");
                                    }
                                }, new userDBManager.OnQrCodesChangedListener() {
                                    @Override
                                    public void onQrCodesChanged(List<QrClass> qrCodes) {
                                        mQrCodes.clear();
                                        mQrCodes.addAll(qrCodes);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setNeutralButton("Comment", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Context context = getActivity();
                                LayoutInflater inflater = LayoutInflater.from(context);
                                View commentView = inflater.inflate(R.layout.fragment_comment_page, null);

                                ListView commentList = commentView.findViewById(R.id.Comment_list);
                                EditText addComment = commentView.findViewById(R.id.Add_comment);
                                Button sendButton = commentView.findViewById(R.id.button_send);

                                // You can set up an adapter for the ListView and set OnClickListener for the send button here.
                                AlertDialog.Builder commentDialog = new AlertDialog.Builder(context);
                                commentDialog.setView(commentView)
                                        .setTitle("Add Comment")
                                        .setNegativeButton("Cancel", null)
                                        .create()
                                        .show();
                            }
                        })
                        .setView(dialogView)
                        .show();
            }
        });


        binding.sort.setOnClickListener(new View.OnClickListener() {
            private boolean isDescending = true;

            @Override
            public void onClick(View v) {
                Collections.sort(mQrCodes, new Comparator<QrClass>() {
                    @Override
                    public int compare(QrClass o1, QrClass o2) {
                        if (isDescending) {
                            return o2.getPoints() - o1.getPoints();
                        } else {
                            return o1.getPoints() - o2.getPoints();
                        }
                    }
                });
                mAdapter.notifyDataSetChanged();
            }
        });

        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        binding.myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQrCode();
            }
        });
        return root;
    }

    @Override
    public void onUserDocumentUpdated(UserProfile userProfile) {
        if (userProfile != null) {
            // Update the UI with the new values for Total Score and Total Scanned
            if (scoreTextView != null) {
                scoreTextView.setText(String.valueOf(userProfile.getTotalScore()));
            }
            if (scannedTextView != null) {
                scannedTextView.setText(String.valueOf(userProfile.getTotalScanned()));
            }
        }
    }

    private static final int PERMISSION_REQUEST_CAMERA = 2;

    public void openCameraToTakePicture() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
        } else {
            Log.d("Camera", "Opening camera");
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Camera onActivityResult" + String.valueOf(resultCode));
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream); // Compress the image
            imageData = outputStream.toByteArray();

            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("images/" + qrClass.getHash());
            UploadTask uploadTask = imageRef.putBytes(imageData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "Image uploaded to Firebase Storage.");
                    // Get the download URL of the uploaded image
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            qrClass.setImageUrl(imageUrl);
                            Log.d(TAG, "Image URL set");
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed to upload image to Firebase Storage.", e);
                }
            });
        }
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        userDBManager userManager = new userDBManager(requireContext());
        QrCodeDBManager qrDb = new QrCodeDBManager();

        ScanOptions options = new ScanOptions();
        options.setPrompt("Please Scan the code");
        options.setBeepEnabled(true);
        options.setCaptureActivity(CaptureAct.class); // To capture QR code through the camera

        barCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() != null) {
                String name = calculateName(result.getContents());
                int score = computeScore(result.getContents());
                String hash = String.valueOf(result.getContents().hashCode());
                qrClass = new QrClass(name, score, String.valueOf(result.getContents().hashCode()));

                qrClass.setFace(generateTextFace(hash));
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.custom_alert_dialog, null);

                TextView face = customView.findViewById(R.id.face_text_view);
                face.setText(qrClass.getFace());

                TextView nameAndScore = customView.findViewById(R.id.name_and_score);
                nameAndScore.setText("Your Qr Name is: " + name + "\nYour Score is: " + score);

                CheckBox checkBox = customView.findViewById(R.id.item_checkbox);

                Button takePictureButton = customView.findViewById(R.id.take_picture_button);
                takePictureButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCameraToTakePicture();
                    }
                });

                if (isQrCodeAlreadyScanned(hash)) {
                    // Show toast that QR code was already added
                    Toast.makeText(getContext(), "QR code already added", Toast.LENGTH_SHORT).show();
                } else {
                    // Show alert dialog
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Score and Name")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Check if checkbox is selected
                                    if (checkBox.isChecked()) {
                                        CheckBox checkBox = customView.findViewById(R.id.item_checkbox);
                                        if (checkBox.isChecked()) {
                                            Log.d(TAG, "Checkbox checked");
                                            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
                                            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                                                    == PackageManager.PERMISSION_GRANTED) {
                                                // Call the method that requires the CAMERA permission here
                                            } else {
                                                int CAMERA_PERMISSION_REQUEST_CODE = 0;
                                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                                                        CAMERA_PERMISSION_REQUEST_CODE);
                                            }
                                            fusedLocationClient.getLastLocation()
                                                    .addOnSuccessListener(location -> {
                                                        if (location != null) {
                                                            GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                                                            qrClass.setLocation(userLocation);
                                                            Log.d(TAG, "Location set");
                                                        } else {
                                                            Log.d(TAG, "Failed to get last known location.");
                                                        }
                                                    });
                                        }
                                    }
                                    qrDb.saveQRCodes(qrClass, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "QR code saved to Firestore.");
                                            } else {
                                                Log.w(TAG, "Error saving QR code to Firestore.", task.getException());
                                            }
                                        }
                                    });

                                    userManager.addQrCode(qrClass, new userDBManager.OnQrCodeAddedListener() {
                                        @Override
                                        public void onQrCodeAdded() {
                                            Log.d(TAG, "QR code added.");
                                        }
                                    }, new userDBManager.OnQrCodesChangedListener() {
                                        @Override
                                        public void onQrCodesChanged(List<QrClass> qrCodes) {
                                            mQrCodes.clear();
                                            mQrCodes.addAll(qrCodes);
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .setView(customView)
                            .create();
                    alertDialog.show();
                }
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void showEditProfileDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_profile_dialog, null);
        userDBManager userManager = new userDBManager(requireContext());


        EditText editUsername = dialogView.findViewById(R.id.edit_username);
        EditText editPhoneNumber = dialogView.findViewById(R.id.edit_phone_number);
        Button updateButton = dialogView.findViewById(R.id.button_update);

        // Get the current user's information
        userManager.getUserProfile(new userDBManager.OnUserProfileLoadedListener() {
            @Override
            public void onUserProfileLoaded(UserProfile userProfile) {
                if (userProfile != null) {
                    editUsername.setText(userProfile.getUsername());
                    editPhoneNumber.setText(Long.toString(userProfile.getContactInfo()));
                } else {
                    Log.d(TAG, "Failed to load user profile.");
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(dialogView)
                .setTitle("Edit Profile");
        final AlertDialog dialog = builder.create();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the updated information from the EditText fields
                String newUsername = editUsername.getText().toString();
                String newPhoneNumber = editPhoneNumber.getText().toString();

                // Update the user's information in the database
                userManager.updateProfile(newUsername, newPhoneNumber, new userDBManager.OnUserProfileUpdatedListener() {
                    @Override
                    public void onUserProfileUpdated(UserProfile userProfile) {
                        if (userProfile != null) {
                            // Update the UI with the new information
                            textView.setText(userProfile.getUsername());
                            scoreTextView.setText(String.valueOf(userProfile.getTotalScore()));
                            scannedTextView.setText(String.valueOf(userProfile.getTotalScanned()));
                        } else {
                            Log.d(TAG, "Failed to update user profile.");
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean isQrCodeAlreadyScanned(String hash) {
        for (QrClass qr : mQrCodes) {
            if (qr.getHash().equals(hash)) {
                return true;
            }
        }
        return false;
    }

    public void scanQrCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Please Scan the code");
        options.setBeepEnabled(true);
        options.setCaptureActivity(CaptureAct.class); // To capture QR code through the camera

        // Launch the barcode scanner activity and handle the result using the activity result launcher
        barCodeLauncher.launch(options);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraToTakePicture();
            } else {
                Toast.makeText(requireContext(), "Camera permission is required to take pictures.", Toast.LENGTH_SHORT).show();
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
    private String generateTextFace(String code) {
        // Define the character sets for different face parts
        String[] eyes = {"o", "O", "0", "x", "-", ">", "<", "(", ")", "8"};
        String[] eyebrows = {"^", "-", ">", "<", "v", "W"};
        String[] pupils = {"*", ".", "o", "O", "0", "x"};
        String[] noses = {"-", "~", "v", "^", "o", "O", "0", "S"};
        String[] mouths = {"u", "U", "(", ")", "W", "V", "[", "]", "{", "}", "/", "\\"};
        String[] faceShapes = {"O", "0", "C", "(", ")", "D", "P", "Q", "B", "@"};

        // Use the hashCode() of the QR code to ensure that similar strings produce different faces
        int hash = code.hashCode();

        // Use parts of the hash to select face parts
        int eyesIndex = Math.abs(hash) % eyes.length;
        int eyebrowsIndex = Math.abs(hash / eyes.length) % eyebrows.length;
        int pupilsIndex = Math.abs(hash / (eyes.length * eyebrows.length)) % pupils.length;
        int nosesIndex = Math.abs(hash / (eyes.length * eyebrows.length * pupils.length)) % noses.length;
        int mouthsIndex = Math.abs(hash / (eyes.length * eyebrows.length * pupils.length * noses.length)) % mouths.length;
        int faceShapesIndex1 = Math.abs(hash / (eyes.length * eyebrows.length * pupils.length * noses.length * mouths.length)) % faceShapes.length;
        int faceShapesIndex2 = Math.abs(hash / (eyes.length * eyebrows.length * pupils.length * noses.length * mouths.length * faceShapes.length)) % faceShapes.length;

        // Build the face string
        StringBuilder faceBuilder = new StringBuilder();
        faceBuilder.append("----------------------------\n");
        faceBuilder.append("----------------------------\n");
        faceBuilder.append("|                              |\n");
        faceBuilder.append("|       ").append(eyebrows[eyebrowsIndex]).append("             ").append(eyebrows[eyebrowsIndex]).append("      |\n");
        faceBuilder.append("|              ").append(noses[nosesIndex]).append("               |\n");
        faceBuilder.append("|              ").append(noses[nosesIndex]).append("               |\n");

        faceBuilder.append("|             ").append(mouths[mouthsIndex]).append("               |\n");
        faceBuilder.append("|                               |\n");
        faceBuilder.append("-----------------------------\n");
        return faceBuilder.toString();
    }
}
