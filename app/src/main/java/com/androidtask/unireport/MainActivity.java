package com.androidtask.unireport;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // UI Components
    EditText etTitle, etDesc, etLocation;
    Spinner spinnerCategory;
    Button btnSubmit, btnLogout;
    ListView listView;

    // Database & Adapter
    DBHelper dbHelper;
    ArrayAdapter<Report> reportAdapter;
    List<Report> reportList;
    String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Initialize UI Elements
        // (Must be done AFTER setContentView to avoid crashes)
        btnLogout = findViewById(R.id.btnLogout);
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etLocation = findViewById(R.id.etLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSubmit = findViewById(R.id.btnSubmit);
        listView = findViewById(R.id.listViewReports);

        // 2. SECURITY CHECK: Are we logged in?
        SharedPreferences prefs = getSharedPreferences("UniReportPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if (!isLoggedIn) {
            // NO -> Redirect to Login Page immediately
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish(); // Close MainActivity so user can't go back
            return;   // STOP running this method
        }

        // 3. LOGGED IN -> Load User Data & App
        userRole = prefs.getString("role", "student"); // Default to student if missing
        setTitle("UniReport (" + userRole + ")");

        // Initialize Database
        dbHelper = new DBHelper(this);

        // Setup Spinner Options
        String[] categories = {"Maintenance", "IT Support", "Cleaning", "Other"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(spinnerAdapter);

        // Load Data
        refreshList();

        // --- BUTTON LISTENERS ---

        // LOGOUT
        btnLogout.setOnClickListener(v -> {
            // Clear saved login info
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            // Go back to Login
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        });

        // SUBMIT REPORT
        btnSubmit.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String desc = etDesc.getText().toString();
            String loc = etLocation.getText().toString();
            // Safety check for spinner
            String cat = (spinnerCategory.getSelectedItem() != null) ?
                    spinnerCategory.getSelectedItem().toString() : "Other";

            if (title.isEmpty() || loc.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter Title and Location", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.addReport(title, desc, loc, cat);
            if (success) {
                Toast.makeText(MainActivity.this, "Report Submitted!", Toast.LENGTH_SHORT).show();
                refreshList();
                clearInputs();
            } else {
                Toast.makeText(MainActivity.this, "Error Saving", Toast.LENGTH_SHORT).show();
            }
        });

        // VIEW DETAILS (Click)
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Report clickedReport = (Report) parent.getItemAtPosition(position);
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);

            // Pass all data to the next screen
            intent.putExtra("ID", clickedReport.getId());
            intent.putExtra("TITLE", clickedReport.getTitle());
            intent.putExtra("DESC", clickedReport.getDescription());
            intent.putExtra("LOC", clickedReport.getLocation());
            intent.putExtra("CAT", clickedReport.getCategory());
            intent.putExtra("STATUS", clickedReport.getStatus());

            startActivity(intent);
        });

        // DELETE REPORT (Long Click - Admin Only)
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            if (userRole.equals("admin")) {
                Report reportToDelete = (Report) parent.getItemAtPosition(position);
                showDeleteDialog(reportToDelete.getId());
            } else {
                Toast.makeText(MainActivity.this, "Only Admins can delete reports", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void showDeleteDialog(int reportId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Report?")
                .setMessage("Are you sure you want to remove this?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    dbHelper.deleteReport(reportId);
                    refreshList();
                    Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void refreshList() {
        reportList = dbHelper.getAllReports();
        reportAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reportList);
        listView.setAdapter(reportAdapter);
    }

    private void clearInputs() {
        etTitle.setText("");
        etDesc.setText("");
        etLocation.setText("");
        spinnerCategory.setSelection(0);
    }
}