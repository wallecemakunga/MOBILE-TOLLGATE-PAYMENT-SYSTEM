package com.example.tollgate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PayTollActivity extends AppCompatActivity {

    EditText etTollAmount, etTollBoothId;
    Button btnPayToll;
    Database db;
    String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_toll);

        etTollAmount = findViewById(R.id.etTollAmount);
        etTollBoothId = findViewById(R.id.etTollBoothId);
        btnPayToll = findViewById(R.id.btnPayToll);

        db = new Database(this);
        userPhone = getIntent().getStringExtra("userPhone");

        btnPayToll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tollAmountStr = etTollAmount.getText().toString();
                String tollBoothId = etTollBoothId.getText().toString();

                if (tollAmountStr.isEmpty() || tollBoothId.isEmpty()) {
                    Toast.makeText(PayTollActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    double tollAmount = Double.parseDouble(tollAmountStr);
                    long result = db.insertTransaction(userPhone, tollAmount, tollBoothId);
                    if (result != -1) {
                        Toast.makeText(PayTollActivity.this, "Toll Payment Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PayTollActivity.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
