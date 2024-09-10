package com.example.tollgate;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddVehicleActivity extends AppCompatActivity {

    private EditText editTextLicensePlate, editTextMakeModel, editTextType, editTextColor, editTextVin, editTextYearOfManufacture;
    private Button buttonAddVehicle;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        // Initialize database helper
        database = new Database(this);

        // Initialize UI components
        editTextLicensePlate = findViewById(R.id.editTextLicensePlate);
        editTextMakeModel = findViewById(R.id.editTextMakeModel);
        editTextType = findViewById(R.id.editTextType);
        editTextColor = findViewById(R.id.editTextColor);
        editTextVin = findViewById(R.id.editTextVin);
        editTextYearOfManufacture = findViewById(R.id.editTextYearOfManufacture);
        buttonAddVehicle = findViewById(R.id.buttonAddVehicle);

        // Fetch the logged-in user's phone number from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);
        String loggedInUserPhone = sharedPreferences.getString("phone", null);

        // Debug: Show the phone number
        if (loggedInUserPhone != null) {
            Toast.makeText(this, "Logged-in Phone: " + loggedInUserPhone, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Phone number not found in session", Toast.LENGTH_SHORT).show();
        }

        // Set the button click listener
        buttonAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch user input
                String licensePlate = editTextLicensePlate.getText().toString().trim();
                String makeModel = editTextMakeModel.getText().toString().trim();
                String type = editTextType.getText().toString().trim();
                String color = editTextColor.getText().toString().trim();
                String vin = editTextVin.getText().toString().trim();
                String yearOfManufacture = editTextYearOfManufacture.getText().toString().trim();

                // Check if the phone number is not null
                if (loggedInUserPhone == null || loggedInUserPhone.isEmpty()) {
                    Toast.makeText(AddVehicleActivity.this, "Failed to retrieve phone number from session", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert data into the database
                boolean isInserted = database.insertVehicleData(loggedInUserPhone, licensePlate, makeModel, type, color, vin, yearOfManufacture);

                // Show a Toast message indicating success or failure
                if (isInserted) {
                    Toast.makeText(AddVehicleActivity.this, "Vehicle added successfully", Toast.LENGTH_SHORT).show();
                    // Optionally, clear the fields or navigate to another screen
                    editTextLicensePlate.setText("");
                    editTextMakeModel.setText("");
                    editTextType.setText("");
                    editTextColor.setText("");
                    editTextVin.setText("");
                    editTextYearOfManufacture.setText("");
                } else {
                    Toast.makeText(AddVehicleActivity.this, "Failed to add vehicle", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        // Close the database connection on activity destroy
        database.close();
        super.onDestroy();
    }
}
