package com.example.tollgate;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TollGatesActivity extends AppCompatActivity {

    private Spinner spinnerTollGates;
    private Button btnConfirm;
    private TextView tvSelectedTollGate;
    private TextView tvLocation;
    private TextView tvOperationalHours;
    private TextView tvAmount;
    private Database database;
    private SharedPreferences sharedPreferences;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toll_gates);

        // Initialize database helper
        database = new Database(this);

        // Initialize UI components
        spinnerTollGates = findViewById(R.id.spinner_toll_gates);
        btnConfirm = findViewById(R.id.btn_confirm);
        tvSelectedTollGate = findViewById(R.id.tv_selected_toll_gate);
        tvLocation = findViewById(R.id.tv_location);
        tvOperationalHours = findViewById(R.id.tv_operational_hours);
        tvAmount = findViewById(R.id.tv_amount);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
        phone = sharedPreferences.getString("phone", "User");

        // Set up the spinner with toll gates fetched from the database
        setupSpinner();

        // Set up the confirm button click listener
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected toll gate
                String selectedTollGateName = spinnerTollGates.getSelectedItem().toString();

                // Fetch details of the selected toll gate from database
                TollGate tollGate = database.getTollGateDetails(selectedTollGateName);

                // Display the selected toll gate details
                if (tollGate != null) {
                    tvSelectedTollGate.setText("Selected Toll Gate: " + tollGate.getName());
                    tvLocation.setText("Location: " + tollGate.getLocation());
                    tvOperationalHours.setText("Operational Hours: " + tollGate.getOperationalHours());
                    tvAmount.setText("Amount: " + tollGate.getAmount());

                    // Handle balance deduction and record the transaction
                    handleBalanceDeduction(tollGate.getAmount(), tollGate.getId());
                }
            }
        });
    }

    // Method to fetch toll gates from database and populate spinner
    private void setupSpinner() {
        // Get readable database
        SQLiteDatabase db = database.getReadableDatabase();

        // Define projection (columns to fetch from database)
        String[] projection = {Database.TOLL_GATE_COLUMN_NAME};

        // Query the database to fetch toll gates
        Cursor cursor = db.query(Database.TOLL_GATE_TABLE_NAME, projection,
                null, null, null, null, null);

        // List to hold toll gate names
        List<String> tollGatesList = new ArrayList<>();

        // Iterate through cursor and add toll gate names to list
        while (cursor.moveToNext()) {
            String tollGateName = cursor.getString(cursor.getColumnIndexOrThrow(Database.TOLL_GATE_COLUMN_NAME));
            tollGatesList.add(tollGateName);
        }

        // Close cursor and database
        cursor.close();
        db.close();

        // Create adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tollGatesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter to spinner
        spinnerTollGates.setAdapter(adapter);
    }

    // Method to handle balance deduction and record the transaction
    private void handleBalanceDeduction(double tollGateAmount, String tollGateId) {
        // Retrieve the current balance of the user
        double currentBalance = database.getBalance(phone);

        // Check if the user has sufficient balance
        if (currentBalance >= tollGateAmount) {
            // Deduct the toll gate amount from the current balance
            double newBalance = currentBalance - tollGateAmount;

            // Update the balance in the database
            if (database.updateUserBalance(phone, newBalance)) {
                // Insert the transaction record in the database
                long transactionResult = database.insertTransaction(phone, tollGateAmount, tollGateId);

                if (transactionResult != -1) {
                    // Display a success message
                    Toast.makeText(TollGatesActivity.this, "Transaction successful! New balance: " + newBalance, Toast.LENGTH_LONG).show();

                    // Return to the previous screen (or home)
                    finish();
                } else {
                    // Display an error message
                    Toast.makeText(TollGatesActivity.this, "Transaction failed! Please try again.", Toast.LENGTH_LONG).show();
                }
            } else {
                // Display an error message for balance update failure
                Toast.makeText(TollGatesActivity.this, "Failed to update balance. Please try again.", Toast.LENGTH_LONG).show();
            }
        } else {
            // Display an insufficient balance message
            Toast.makeText(TollGatesActivity.this, "Insufficient balance! Please top up.", Toast.LENGTH_LONG).show();
        }
    }
}
