package com.androidtask.unireport;

import android.content.Intent;
import android.content.SharedPreferences; // IMPORT THIS
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    EditText etUser, etPass;
    Button btnLogin;
    TextView tvRegister;
    DBHelper dbHelper;
    SharedPreferences sharedPreferences; // Variable to hold the tool

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. CHECK IF ALREADY LOGGED IN
        // "UniReportPrefs" is the name of the file where we save data
        sharedPreferences = getSharedPreferences("UniReportPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            String role = sharedPreferences.getString("role", "student");
            // If logged in, go straight to Main
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            intent.putExtra("USER_ROLE", role);
            startActivity(intent);
            finish();
            return; // Stop running this activity
        }

        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> {
            String user = etUser.getText().toString();
            String pass = etPass.getText().toString();

            String role = dbHelper.checkLogin(user, pass);

            if(role != null) {
                // 2. SAVE LOGIN STATE
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("role", role);
                editor.apply(); // Commit changes

                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                intent.putExtra("USER_ROLE", role);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}