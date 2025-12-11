package com.androidtask.unireport;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // UI Components
    EditText etTitle, etDesc, etLocation;
    Button btnSubmit;
    ListView listView;

    // Database & Adapter
    DBHelper dbHelper;
    ArrayAdapter<Report> reportAdapter;
    List<Report> reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Hook up UI elements
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        etLocation = findViewById(R.id.etLocation);
        btnSubmit = findViewById(R.id.btnSubmit);
        listView = findViewById(R.id.listViewReports);

        // 2. Initialize Database
        dbHelper = new DBHelper(this);

        // 3. Load existing reports into the list
        refreshList();

        // 4. Set Button Click Listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input
                String title = etTitle.getText().toString();
                String desc = etDesc.getText().toString();
                String loc = etLocation.getText().toString();

                // Validation
                if (title.isEmpty() || loc.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter Title and Location", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save to DB
                boolean success = dbHelper.addReport(title, desc, loc);

                if (success) {
                    Toast.makeText(MainActivity.this, "Report Submitted!", Toast.LENGTH_SHORT).show();
                    refreshList(); // Update the list view
                    clearInputs(); // Clear the form
                } else {
                    Toast.makeText(MainActivity.this, "Error Saving", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 5. Set List Item Click Listener (To see details)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Report clickedReport = (Report) parent.getItemAtPosition(position);
                // Show a simple toast with the description
                Toast.makeText(MainActivity.this, "Details: " + clickedReport.getDescription(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void refreshList() {
        reportList = dbHelper.getAllReports();
        // Use a simple default layout for the list items
        reportAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reportList);
        listView.setAdapter(reportAdapter);
    }

    private void clearInputs() {
        etTitle.setText("");
        etDesc.setText("");
        etLocation.setText("");
    }
}