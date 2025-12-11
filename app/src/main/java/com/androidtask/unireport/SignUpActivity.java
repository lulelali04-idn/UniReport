package com.androidtask.unireport;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DBHelper dbHelper = new DBHelper(this);
        EditText etUser = findViewById(R.id.etRegUser);
        EditText etPass = findViewById(R.id.etRegPass);
        Button btnReg = findViewById(R.id.btnRegister);

        btnReg.setOnClickListener(v -> {
            String user = etUser.getText().toString();
            String pass = etPass.getText().toString();
            String role = "student";

            if(user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.registerUser(user, pass, role);
            if(success) {
                Toast.makeText(this, "Registered! Please Sign In.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
        });
    }
}