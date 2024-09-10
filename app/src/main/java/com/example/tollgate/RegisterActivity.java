package com.example.tollgate;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    TextView tv;
    EditText edUsername, edPhone, edPassword, edConfirm;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edUsername = findViewById(R.id.editTextTextPersonName);
        edPhone = findViewById(R.id.Phone);
        edPassword = findViewById(R.id.Password);
        edConfirm = findViewById(R.id.ConfirmPassword);
        tv = findViewById(R.id.loginUser);
        btn = findViewById(R.id.button);

        // Limit phone number to 10 digits
        edPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = edUsername.getText().toString();
                final String phone = edPhone.getText().toString();
                final String password = edPassword.getText().toString();
                String confirm = edConfirm.getText().toString();

                final Database db = new Database(getApplicationContext());

                if (username.isEmpty() || phone.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (phone.length() != 10) {
                    Toast.makeText(getApplicationContext(), "Phone number must be exactly 10 digits", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.equals(confirm)) {
                        if (isValid(password)) {
                            // Check if phone number already exists
                            if (phoneNumberExists(phone)) {
                                Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                db.register(username, phone, password);
                                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();

                                // Save username and phone to SharedPreferences
                                saveToSharedPreferences(username, phone,password);

                                // Navigate to LoginActivity
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish(); // Close RegisterActivity to prevent going back to it
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Password must contain 8 mixed characters", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    // Method to check if phone number already exists
    private boolean phoneNumberExists(String phoneNumber) {
        final Database db = new Database(getApplicationContext());
        return db.checkPhoneNumber(phoneNumber);
    }

    public static boolean isValid(String passwordhere) {
        int f1 = 0, f2 = 0, f3 = 0;
        if (passwordhere.length() < 8) {
            return false;
        } else {
            for (int p = 0; p < passwordhere.length(); p++) {
                if (Character.isLetter(passwordhere.charAt(p))) {
                    f1 = 1;
                }
            }
            for (int r = 0; r < passwordhere.length(); r++) {
                if (Character.isDigit(passwordhere.charAt(r))) {
                    f2 = 1;
                }
            }
            for (int s = 0; s < passwordhere.length(); s++) {
                char c = passwordhere.charAt(s);
                if (c >= 33 && c <= 46 || c == 64) {
                    f3 = 1;
                }
            }
            return f1 == 1 && f2 == 1 && f3 == 1;
        }
    }

    // Save username and phone to SharedPreferences
    private void saveToSharedPreferences(String username, String phone,String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("phone", phone);
        editor.putString("password", password);
        Log.d("RegisterActivity", "Saved Password: " + password); // Log the saved password
        editor.apply(); // Apply changes to SharedPreferences
    }
}
