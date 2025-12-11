package com.androidtask.unireport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
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
        Button btnBack = findViewById(R.id.btnBack);

        CheckBox cbFixed = findViewById(R.id.cbFixed);

        int id = getIntent().getIntExtra("ID", -1);
        String title = getIntent().getStringExtra("TITLE");
        String desc = getIntent().getStringExtra("DESC");
        String loc = getIntent().getStringExtra("LOC");
        String status = getIntent().getStringExtra("STATUS");

        tvTitle.setText(title);
        tvDesc.setText(desc);
        tvLocation.setText(loc);

        SharedPreferences prefs = getSharedPreferences("UniReportPrefs", MODE_PRIVATE);
        String role = prefs.getString("role", "student");

        // Logic for Checkbox
        if (cbFixed != null) {
            cbFixed.setChecked("Fixed".equals(status));

            if (role.equals("admin")) {
                cbFixed.setEnabled(true);
                cbFixed.setText("Mark as Fixed");

                cbFixed.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    String newStatus = isChecked ? "Fixed" : "Active";
                    dbHelper.updateReportStatus(id, newStatus);
                    Toast.makeText(DetailActivity.this, "Status Updated!", Toast.LENGTH_SHORT).show();
                });
            } else {
                cbFixed.setEnabled(false);
                cbFixed.setText("Status: " + (status == null ? "Active" : status));
            }
        }

        btnBack.setOnClickListener(v -> finish());
    }
}