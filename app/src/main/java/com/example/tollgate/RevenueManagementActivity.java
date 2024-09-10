package com.example.tollgate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RevenueManagementActivity extends AppCompatActivity {

    private TextView tvTotalRevenue;
    private RecyclerView rvTollgateAmounts;
    private Button btnCalculateRevenue;
    private Database database;
    private TollgateAmountsAdapter tollgateAmountsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue_management);

        // Initialize UI components
        tvTotalRevenue = findViewById(R.id.tv_total_revenue);
        rvTollgateAmounts = findViewById(R.id.tv_tollgate_amounts);
        btnCalculateRevenue = findViewById(R.id.btn_calculate_revenue);

        // Initialize the database helper
        database = new Database(this);

        // Initialize RecyclerView for tollgate amounts
        rvTollgateAmounts.setLayoutManager(new LinearLayoutManager(this));
        tollgateAmountsAdapter = new TollgateAmountsAdapter(new ArrayList<>());
        rvTollgateAmounts.setAdapter(tollgateAmountsAdapter);

        // Set up the button click listener to calculate and display the revenue details
        btnCalculateRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndDisplayRevenueDetails();
            }
        });
    }

    private void calculateAndDisplayRevenueDetails() {
        // Calculate and display total revenue
        double totalRevenue = database.getTotalRevenue();
        tvTotalRevenue.setText("Total Revenue: " + totalRevenue + " TZS");

        // Fetch tollgate amounts and update RecyclerView
        Map<String, Double> tollgateAmounts = database.getTollgateAmounts();
        List<TollgateAmount> tollgateAmountList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : tollgateAmounts.entrySet()) {
            tollgateAmountList.add(new TollgateAmount(entry.getKey(), entry.getValue()));
        }
        tollgateAmountsAdapter.updateTollgateAmounts(tollgateAmountList);

        // Show a toast message
        Toast.makeText(RevenueManagementActivity.this, "Revenue details calculated!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        // Close database connection on activity destroy
        database.close();
        super.onDestroy();
    }
}
