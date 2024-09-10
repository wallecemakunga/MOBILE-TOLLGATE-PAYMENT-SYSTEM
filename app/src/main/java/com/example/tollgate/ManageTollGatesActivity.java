package com.example.tollgate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ManageTollGatesActivity extends AppCompatActivity {

    private LinearLayout tollGatesContainer;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_toll_gates);

        tollGatesContainer = findViewById(R.id.toll_gates_container);
        database = new Database(this);

        loadTollGates();
    }

    private void loadTollGates() {
        List<TollGate> tollGates = database.getAllTollGates();

        for (TollGate tollGate : tollGates) {
            View tollGateView = getLayoutInflater().inflate(R.layout.item_toll_gate, tollGatesContainer, false);

            TextView tvTollGateName = tollGateView.findViewById(R.id.tv_toll_gate_name);
            TextView tvTollGateLocation = tollGateView.findViewById(R.id.tv_toll_gate_location);
            Button btnDelete = tollGateView.findViewById(R.id.btn_delete);

            tvTollGateName.setText(tollGate.getName());
            tvTollGateLocation.setText(tollGate.getLocation());

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean deleted = database.deleteTollGate(tollGate.getId());
                    if (deleted) {
                        Toast.makeText(ManageTollGatesActivity.this, "Toll gate deleted successfully", Toast.LENGTH_SHORT).show();
                        tollGatesContainer.removeView(tollGateView);
                    } else {
                        Toast.makeText(ManageTollGatesActivity.this, "Failed to delete toll gate", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            tollGatesContainer.addView(tollGateView);
        }
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
