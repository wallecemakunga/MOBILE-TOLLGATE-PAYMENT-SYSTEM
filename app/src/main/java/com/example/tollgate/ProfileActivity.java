package com.example.tollgate;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvFullName, tvPhoneNumber, tvUsername, tvBalance;
    private LinearLayout llVehicles;

    private Database database;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize UI components
        tvFullName = findViewById(R.id.tv_full_name);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvUsername = findViewById(R.id.tv_username);
        tvBalance = findViewById(R.id.tv_balance);
        llVehicles = findViewById(R.id.ll_vehicles);

        // Initialize the database helper
        database = new Database(this);

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE);

        // Fetch the phone number of the logged-in user from shared preferences
        String phoneNumber = sharedPreferences.getString("phone", null);

        if (phoneNumber != null) {
            // Fetch and display user information
            loadUserProfile(phoneNumber);

            // Fetch and display user's vehicle details
            loadUserVehicles(phoneNumber);
        } else {
            Toast.makeText(this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if user info is not available
        }
    }

    private void loadUserProfile(String phoneNumber) {
        User user = database.getUserDetails(phoneNumber);

        if (user != null) {
            double balance = database.getBalance(phoneNumber);

            tvFullName.setText("Full Name: " + user.getUsername());
            tvPhoneNumber.setText("Phone Number: " + phoneNumber);
            tvUsername.setText("Username: " + user.getUsername());
            tvBalance.setText("Balance: " + balance + " TZS");
        } else {
            Toast.makeText(this, "Failed to retrieve user details", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserVehicles(String phoneNumber) {
        Cursor cursor = database.getUserVehicles(phoneNumber);

        if (cursor != null) {
            llVehicles.removeAllViews(); // Clear existing views before adding new ones

            while (cursor.moveToNext()) {
                String licensePlate = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_LICENSE_PLATE));
                String makeModel = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_MAKE_MODEL));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_TYPE));
                String color = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_COLOR));
                String vin = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_VIN));
                String year = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_YEAR_OF_MANUFACTURE));

                // Create a view for each vehicle
                View vehicleView = getLayoutInflater().inflate(R.layout.item_vehicle, llVehicles, false);

                // Populate the view with vehicle details
                ((TextView) vehicleView.findViewById(R.id.tv_license_plate)).setText("License Plate: " + licensePlate);
                ((TextView) vehicleView.findViewById(R.id.tv_make_model)).setText("Make/Model: " + makeModel);
                ((TextView) vehicleView.findViewById(R.id.tv_type)).setText("Type: " + type);
                ((TextView) vehicleView.findViewById(R.id.tv_color)).setText("Color: " + color);
                ((TextView) vehicleView.findViewById(R.id.tv_vin)).setText("VIN: " + vin);
                ((TextView) vehicleView.findViewById(R.id.tv_year)).setText("Year: " + year);

                // Add the vehicle view to the container
                llVehicles.addView(vehicleView);
            }
            cursor.close();
        } else {
            Toast.makeText(this, "No vehicles found for this user", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        // Close the database connection when the activity is destroyed
        database.close();
        super.onDestroy();
    }
}
