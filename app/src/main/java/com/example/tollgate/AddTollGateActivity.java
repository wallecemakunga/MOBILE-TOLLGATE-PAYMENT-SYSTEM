package com.example.tollgate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class AddTollGateActivity extends AppCompatActivity {

    private EditText editTextName, editTextLocation, editTextOperationalHours, editTextAmount;
    private Button buttonAddTollGate;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toll_gate);

        // Initialize UI components
        editTextName = findViewById(R.id.editTextName);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextOperationalHours = findViewById(R.id.editTextOperationalHours);
        editTextAmount = findViewById(R.id.editTextAmount);
        buttonAddTollGate = findViewById(R.id.buttonAddTollGate);

        // Initialize database
        database = new Database(this);

        // Set up onClick listener for Add Toll Gate button
        buttonAddTollGate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from EditText fields
                String name = editTextName.getText().toString().trim();
                String location = editTextLocation.getText().toString().trim();
                String operationalHours = editTextOperationalHours.getText().toString().trim();
                String amountStr = editTextAmount.getText().toString().trim();

                // Validate inputs
                if (name.isEmpty() || location.isEmpty() || operationalHours.isEmpty() || amountStr.isEmpty()) {
                    Toast.makeText(AddTollGateActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert amount to double
                double amount = Double.parseDouble(amountStr);

                // Insert toll gate data into database
                boolean isInserted = database.insertTollGateData(name, location, operationalHours, amount);

                if (isInserted) {
                    Toast.makeText(AddTollGateActivity.this, "Toll Gate added successfully", Toast.LENGTH_SHORT).show();

                    // Set result to notify that data has been changed
                    setResult(RESULT_OK);

                    // Finish this activity and return to the previous one
                    finish();
                } else {
                    Toast.makeText(AddTollGateActivity.this, "Failed to add Toll Gate", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
