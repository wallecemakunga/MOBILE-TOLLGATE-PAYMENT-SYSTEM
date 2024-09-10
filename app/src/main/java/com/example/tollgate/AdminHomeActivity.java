package com.example.tollgate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminHomeActivity extends AppCompatActivity {

    private static final int ADD_TOLLGATE_REQUEST = 1; // Request code for adding a toll gate
    private TextView tvTotalCars, tvTotalUsers, tvTotalTollgates;
    private BottomNavigationView bottomNavigationView;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        tvTotalCars = findViewById(R.id.tv_total_cars);
        tvTotalUsers = findViewById(R.id.tv_total_users);
        tvTotalTollgates = findViewById(R.id.tv_total_tollgates);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Initialize the database helper
        database = new Database(this);

        // Fetch and display the totals
        loadTotals();

        // Set up navigation item selection listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add_toll_gate:
                        // Start AddTollGateActivity expecting a result
                        Intent intent = new Intent(AdminHomeActivity.this, AddTollGateActivity.class);
                        startActivityForResult(intent, ADD_TOLLGATE_REQUEST);
                        return true;
                    case R.id.action_view_vehicles:
                        startActivity(new Intent(AdminHomeActivity.this, ViewVehiclesActivity.class));
                        return true;
                    case R.id.action_revenue_management:
                        startActivity(new Intent(AdminHomeActivity.this, RevenueManagementActivity.class));
                        return true;
                    case R.id.action_logout:
                        logoutAndReturnToLogin();
                        return true;
                }
                return false;
            }
        });

        // Add click listener to the Total Toll-gates card
        findViewById(R.id.card_total_tollgates).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, ManageTollGatesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the totals displayed on the home screen
        loadTotals();
    }

    private void loadTotals() {
        int totalTollGates = database.getTotalTollGatesCount();
        int totalUsers = database.getTotalUsersCount();
        int totalVehicles = database.getTotalVehiclesCount(); // Fetch total vehicles

        tvTotalTollgates.setText(String.valueOf(totalTollGates));
        tvTotalUsers.setText(String.valueOf(totalUsers));
        tvTotalCars.setText(String.valueOf(totalVehicles)); // Display total vehicles
    }

    // Handle result from AddTollGateActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TOLLGATE_REQUEST && resultCode == RESULT_OK) {
            // Refresh the totals displayed on the home screen
            loadTotals();
        }
    }

    private void logoutAndReturnToLogin() {
        // Clear the session data (if any)
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all stored data in SharedPreferences
        editor.apply();

        // Start LoginActivity and clear the activity stack
        Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Finish this activity
        finish();
    }

    @Override
    protected void onDestroy() {
        // Close the database connection when the activity is destroyed
        database.close();
        super.onDestroy();
    }
}
