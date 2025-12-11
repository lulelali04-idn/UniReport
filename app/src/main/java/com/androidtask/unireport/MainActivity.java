package com.androidtask.unireport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("UniReportPrefs", MODE_PRIVATE);
        if (!prefs.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        String role = prefs.getString("role", "student");
        TextView tvWelcome = findViewById(R.id.tvWelcomeRole);
        tvWelcome.setText("Welcome, " + role.substring(0, 1).toUpperCase() + role.substring(1));

        Button btnReport = findViewById(R.id.btnGoReport);
        Button btnList = findViewById(R.id.btnGoList);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnReport.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SubmitActivity.class));
        });

        btnList.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewReportsActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();
        });
    }
}