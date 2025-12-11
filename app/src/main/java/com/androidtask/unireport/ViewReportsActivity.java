package com.androidtask.unireport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class ViewReportsActivity extends AppCompatActivity {

    ListView listView;
    DBHelper dbHelper;
    List<Report> reportList;
    String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        dbHelper = new DBHelper(this);
        listView = findViewById(R.id.listViewReports);
        Button btnRefresh = findViewById(R.id.btnRefresh);

        SharedPreferences prefs = getSharedPreferences("UniReportPrefs", MODE_PRIVATE);
        userRole = prefs.getString("role", "student");

        loadData();

        btnRefresh.setOnClickListener(v -> {
            loadData();
            Toast.makeText(this, "List Refreshed", Toast.LENGTH_SHORT).show();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Report report = reportList.get(position);
            Intent intent = new Intent(ViewReportsActivity.this, DetailActivity.class);

            // Pass all data including the new IMAGE field
            intent.putExtra("ID", report.getId());
            intent.putExtra("TITLE", report.getTitle());
            intent.putExtra("DESC", report.getDescription());
            intent.putExtra("LOC", report.getLocation());
            intent.putExtra("CAT", report.getCategory());
            intent.putExtra("STATUS", report.getStatus());
            intent.putExtra("IMAGE", report.getImage());

            startActivity(intent);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            if (userRole.equals("admin")) {
                new AlertDialog.Builder(this)
                        .setTitle("Delete Report?")
                        .setMessage("This cannot be undone.")
                        .setPositiveButton("Delete", (d, w) -> {
                            dbHelper.deleteReport(reportList.get(position).getId());
                            loadData(); // Auto refresh after delete
                            Toast.makeText(this, "Report Deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            } else {
                Toast.makeText(this, "Only Admins can delete reports", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void loadData() {
        reportList = dbHelper.getAllReports();
        ArrayAdapter<Report> adapter = new ArrayAdapter<Report>(this, android.R.layout.simple_list_item_2, android.R.id.text1, reportList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                Report report = reportList.get(position);
                String statusIcon = "Fixed".equals(report.getStatus()) ? "✅ " : "🔴 ";
                text1.setText(statusIcon + report.getTitle());
                text1.setTextSize(18);
                text1.setTextColor(Color.BLACK);
                text2.setText(report.getLocation() + " • " + report.getCategory());
                text2.setTextColor(Color.DKGRAY);

                return view;
            }
        };

        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}