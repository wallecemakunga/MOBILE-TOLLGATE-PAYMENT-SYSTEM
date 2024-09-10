package com.example.tollgate;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    private TextView tvFullName, tvPhoneNumber, tvUsername, tvBalance;
    private LinearLayout llVehicles;

    private Database database;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize UI components
        tvFullName = findViewById(R.id.tv_full_name);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvUsername = findViewById(R.id.tv_username);
        tvBalance = findViewById(R.id.tv_balance);
        llVehicles = findViewById(R.id.ll_vehicles);

        // Initialize the database helper
        database = new Database(this);

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);

        // Fetch the phone number of the logged-in user from shared preferences
        String phoneNumber = sharedPreferences.getString("phone", null);

        if (phoneNumber != null) {
            Log.d(TAG, "Phone number retrieved: " + phoneNumber);

            // Fetch and display user information
            loadUserProfile(phoneNumber);

            // Fetch and display user's vehicle details
            loadUserVehicles(phoneNumber);
        } else {
            Log.e(TAG, "Failed to retrieve phone number from shared preferences");
            Toast.makeText(this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if user info is not available
        }
    }

    private void loadUserProfile(String phoneNumber) {
        Log.d(TAG, "Loading user profile for phone number: " + phoneNumber);
        Cursor cursor = null;

        try {
            cursor = (Cursor) database.getUserDetails(phoneNumber);
            if (cursor != null && cursor.moveToFirst()) {
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow(Database.USER_COLUMN_USERNAME)); // Assuming username is full name
                int balance = cursor.getInt(cursor.getColumnIndexOrThrow(Database.BALANCE_COLUMN_AMOUNT));

                Log.d(TAG, "User details retrieved: Full Name: " + fullName + ", Balance: " + balance);

                tvFullName.setText("Full Name: " + fullName);
                tvPhoneNumber.setText("Phone Number: " + phoneNumber);
                tvUsername.setText("Username: " + fullName); // Adjust as needed if username is different
                tvBalance.setText("Balance: " + balance + " TZS");
            } else {
                Log.e(TAG, "No user details found for phone number: " + phoneNumber);
                Toast.makeText(this, "Failed to retrieve user details", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading user profile", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void loadUserVehicles(String phoneNumber) {
        Log.d(TAG, "Loading vehicles for phone number: " + phoneNumber);
        Cursor cursor = null;

        try {
            cursor = database.getUserVehicles(phoneNumber);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String licensePlate = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_LICENSE_PLATE));
                    String makeModel = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_MAKE_MODEL));
                    String type = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_TYPE));
                    String color = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_COLOR));
                    String vin = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_VIN));
                    String year = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_YEAR_OF_MANUFACTURE));

                    Log.d(TAG, "Vehicle retrieved: License Plate: " + licensePlate + ", Make/Model: " + makeModel);

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
            } else {
                Log.e(TAG, "No vehicles found for phone number: " + phoneNumber);
                Toast.makeText(this, "No vehicles found for this user", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading user vehicles", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Closing database connection");
        // Close the database connection when the activity is destroyed
        database.close();
        super.onDestroy();
    }
}
