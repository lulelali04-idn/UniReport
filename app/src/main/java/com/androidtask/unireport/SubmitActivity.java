package com.androidtask.unireport;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SubmitActivity extends AppCompatActivity {

    EditText etTitle, etDesc, etLocation;
    Spinner spinner;
    Button btnSubmit, btnBack, btnUpload;
    ImageView ivPreview;
    TextView tvImageStatus;
    DBHelper dbHelper;
    String encodedImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        dbHelper = new DBHelper(this);
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etLocation = findViewById(R.id.etLocation);
        spinner = findViewById(R.id.spinnerCategory);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);
        btnUpload = findViewById(R.id.btnUpload);
        ivPreview = findViewById(R.id.ivPreview);
        tvImageStatus = findViewById(R.id.tvImageStatus);

        String[] categories = {"Maintenance", "IT Support", "Cleaning", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinner.setAdapter(adapter);
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            // 1. Get Bitmap
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, true);
                            ivPreview.setImageBitmap(resizedBitmap);
                            ivPreview.setVisibility(View.VISIBLE);
                            tvImageStatus.setText("Image Attached");
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                            byte[] imageBytes = outputStream.toByteArray();
                            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        } catch (Exception e) {
                            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        btnSubmit.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String desc = etDesc.getText().toString();
            String loc = etLocation.getText().toString();
            String cat = spinner.getSelectedItem().toString();

            if (title.isEmpty() || loc.isEmpty()) {
                Toast.makeText(this, "Title & Location required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.addReport(title, desc, loc, cat, encodedImage)) {
                Toast.makeText(this, "Report Sent!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}