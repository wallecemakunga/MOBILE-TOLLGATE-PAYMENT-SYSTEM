package com.example.tollgate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText phoneEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView tv;
    private SharedPreferences sharedPreferences;
    private Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        phoneEditText = findViewById(R.id.Phone);
        passwordEditText = findViewById(R.id.Password);
        loginButton = findViewById(R.id.button);
        tv = findViewById(R.id.RegUser);
        sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);

        phoneEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        // Initialize Database
        database = new Database(this);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // Set OnClickListener for login button
        loginButton.setOnClickListener(view -> loginUser());
    }

    private void loginUser() {
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!phone.isEmpty() && !password.isEmpty()) {
            boolean isAdmin = database.adminLogin(phone, password);
            if (isAdmin) {
                Admin admin = database.getAdminDetails(phone);
                if (admin != null) {
                    Toast.makeText(getApplicationContext(), "Admin login successfully", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("admin_phone", admin.getPhone());
                    editor.apply();

                    startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Admin details not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                boolean isUser = database.login(phone, password);
                if (isUser) {
                    User user = database.getUserDetails(phone);
                    if (user != null) {
                        Toast.makeText(getApplicationContext(), "Login successfully", Toast.LENGTH_SHORT).show();
                        // Assuming `user` is an object with the user's details
                        SharedPreferences sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", user.getUsername());
                        editor.putString("phone", user.getPhone());
                        editor.apply();

                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "User details not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Invalid phone number or password", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Please enter phone number and password", Toast.LENGTH_SHORT).show();
        }
    }
}