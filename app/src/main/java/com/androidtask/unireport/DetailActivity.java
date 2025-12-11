package com.androidtask.unireport;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DBHelper dbHelper = new DBHelper(this);

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        TextView tvLocation = findViewById(R.id.tvDetailLocation);
        CheckBox cbFixed = findViewById(R.id.cbFixed);
        Button btnBack = findViewById(R.id.btnBack);
        ImageView ivDetailImage = findViewById(R.id.ivDetailImage);
        LinearLayout layoutImageContainer = findViewById(R.id.layoutImageContainer);

        // Get Data
        int id = getIntent().getIntExtra("ID", -1);
        String title = getIntent().getStringExtra("TITLE");
        String desc = getIntent().getStringExtra("DESC");
        String loc = getIntent().getStringExtra("LOC");
        String cat = getIntent().getStringExtra("CAT");
        String status = getIntent().getStringExtra("STATUS");
        String imageString = getIntent().getStringExtra("IMAGE"); // Get Image String

        tvTitle.setText(title);
        tvDesc.setText(desc);
        tvLocation.setText(loc + " • " + cat);

        // HANDLE IMAGE DISPLAY
        if (imageString != null && !imageString.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ivDetailImage.setImageBitmap(decodedByte);
                layoutImageContainer.setVisibility(View.VISIBLE); // Show container
            } catch (Exception e) {
                layoutImageContainer.setVisibility(View.GONE);
            }
        } else {
            layoutImageContainer.setVisibility(View.GONE);
        }

        // ROLE LOGIC
        SharedPreferences prefs = getSharedPreferences("UniReportPrefs", MODE_PRIVATE);
        String role = prefs.getString("role", "student");

        cbFixed.setChecked("Fixed".equals(status));

        if (role.equals("admin")) {
            cbFixed.setEnabled(true);
            cbFixed.setText("Mark as Fixed");
            cbFixed.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String newStatus = isChecked ? "Fixed" : "Active";
                dbHelper.updateReportStatus(id, newStatus);
                Toast.makeText(this, "Status Updated", Toast.LENGTH_SHORT).show();
            });
        } else {
            cbFixed.setEnabled(false);
            cbFixed.setText("Status: " + status);
        }

        btnBack.setOnClickListener(v -> finish());
    }
}