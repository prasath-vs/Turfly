package com.example.turfly;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.turfly.models.TurfModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

public class AddTurfActivity extends AppCompatActivity {

    private EditText edtTurfName, edtLocation, edtRating, edtMapsLink, edtArea;
    private Button btnUploadImage, btnAddTurf;
    private ImageView imgTurfPreview;
    private Uri imageUri;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    // ✅ Cloudinary Configuration
    private Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dnofkda2n", // ✅ Replace with your Cloudinary Cloud Name
            "api_key", "438983721286826",       // ✅ Replace with your Cloudinary API Key
            "api_secret", "PgbAT-ut6jhFzpQm9QZklzKOtas"  // ✅ Replace with your Cloudinary API Secret
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_turf);

        // ✅ Initialize UI Elements
        edtTurfName = findViewById(R.id.edtTurfName);
        edtLocation = findViewById(R.id.edtLocation);
        edtRating = findViewById(R.id.edtRating);
        edtMapsLink = findViewById(R.id.edtMapsLink);
        edtArea = findViewById(R.id.edtArea);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnAddTurf = findViewById(R.id.btnAddTurf);
        imgTurfPreview = findViewById(R.id.imgTurfPreview);

        // ✅ Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Turfs");

        // ✅ Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        // ✅ Button Click Listeners
        btnUploadImage.setOnClickListener(v -> openGallery());
        btnAddTurf.setOnClickListener(v -> {
            if (validateFields()) {
                uploadImageToCloudinary();
            }
        });
    }

    // ✅ Open Gallery to Select Image
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imgTurfPreview);
        }
    }

    // ✅ Validate Input Fields
    private boolean validateFields() {
        if (edtTurfName.getText().toString().isEmpty() ||
                edtLocation.getText().toString().isEmpty() ||
                edtRating.getText().toString().isEmpty() ||
                edtMapsLink.getText().toString().isEmpty() ||
                edtArea.getText().toString().isEmpty() ||
                imageUri == null) {
            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // ✅ Upload Image to Cloudinary
    private void uploadImageToCloudinary() {
        progressDialog.show();
        new Thread(() -> {
            try {
                File file = getFileFromUri(imageUri);
                Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("secure_url");

                // ✅ Add data to Firebase
                runOnUiThread(() -> addTurfToDatabase(imageUrl));
            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    // ✅ Convert Uri to File
    private File getFileFromUri(Uri uri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File file = new File(getCacheDir(), getFileName(uri) != null ? getFileName(uri) : UUID.randomUUID().toString() + ".jpg");
        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
        return file;
    }

    // ✅ Get File Name from Uri
    private String getFileName(Uri uri) {
        String fileName = null;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (columnIndex != -1) {
                fileName = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return fileName;
    }

    // ✅ Add Turf Data to Firebase
    private void addTurfToDatabase(String imageUrl) {
        String id = databaseReference.push().getKey();

        // ✅ Convert Rating to Double
        double ratingValue;
        try {
            ratingValue = Double.parseDouble(edtRating.getText().toString()); // ✅ Corrected rating format
        } catch (NumberFormatException e) {
            progressDialog.dismiss();
            Toast.makeText(this, "Invalid rating value", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Create Turf Model with Correct Field Assignments
        TurfModel turfModel = new TurfModel(
                id,
                edtTurfName.getText().toString(),
                edtLocation.getText().toString(),
                ratingValue,
                edtMapsLink.getText().toString(),
                imageUrl,  // ✅ Store imageUrl correctly (Cloudinary Image)
                edtArea.getText().toString() // ✅ Store area correctly (Location Area)
        );

        // ✅ Store in Firebase
        databaseReference.child(id).setValue(turfModel).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(AddTurfActivity.this, "Turf Added Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddTurfActivity.this, "Failed to Add Turf!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
