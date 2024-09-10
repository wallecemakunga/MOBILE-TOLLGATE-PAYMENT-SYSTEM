package com.example.tollgate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

public class RechargeActivity extends AppCompatActivity {

    private EditText etAmount;
    private Button btnConfirm;
    private TextView tvBalance;

    private SharedPreferences sharedPreferences;
    private int maxAmount = 10000; // Maximum allowed amount
    private double currentBalance; // User's current balance
    private String userPhone; // User's phone number

    private Database db; // Database helper instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        etAmount = findViewById(R.id.et_amount);
        btnConfirm = findViewById(R.id.btn_confirm);
        tvBalance = findViewById(R.id.tv_balance);

        // Initialize shared preferences to fetch user's details
        sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
        userPhone = sharedPreferences.getString("phone", "");

        // Initialize the database
        db = new Database(this);

        // Fetch and display the current balance from the database
        currentBalance = db.getBalance(userPhone);
        updateBalanceDisplay();

        // Set up the confirm button click listener
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRecharge();
            }
        });
    }

    private void handleRecharge() {
        String amountText = etAmount.getText().toString();

        // Validate input
        if (amountText.isEmpty()) {
            Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount > maxAmount) {
            Toast.makeText(this, "Amount exceeds maximum allowed limit of 10,000 TZS", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform the recharge (add amount to current balance)
        currentBalance += amount;

        // Update the balance in the database
        if (db.updateUserBalance(userPhone, currentBalance)) {
            // Save the new balance to SharedPreferences (optional if you want to keep it)
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("current_balance", (float) currentBalance);
            editor.apply();

            // Display success message and update balance
            Toast.makeText(this, "Recharge successful! New Balance: " + currentBalance + " TZS", Toast.LENGTH_SHORT).show();
            updateBalanceDisplay();
            // RechargeActivity.java

            finish();


            // Clear the input fields
            etAmount.setText("");
        } else {
            Toast.makeText(this, "Failed to update balance. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBalanceDisplay() {
        tvBalance.setText("Current Balance: " + currentBalance + " TZS");
    }
}
