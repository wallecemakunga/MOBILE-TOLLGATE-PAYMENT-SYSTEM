package com.example.tollgate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ViewVehiclesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VehicleAdapter vehicleAdapter;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vehicles);

        recyclerView = findViewById(R.id.recycler_view_vehicles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = new Database(this);

        // Fetch vehicle data from the database
        List<Vehicle> vehicleList = fetchVehiclesFromDatabase();

        // Set up the adapter with the vehicle data
        vehicleAdapter = new VehicleAdapter(vehicleList);
        recyclerView.setAdapter(vehicleAdapter);
    }

    // Method to fetch vehicle data from the database
    private List<Vehicle> fetchVehiclesFromDatabase() {
        List<Vehicle> vehicleList = new ArrayList<>();
        SQLiteDatabase db = database.getReadableDatabase();

        String[] projection = {
                Database.VEHICLE_COLUMN_PHONE,
                Database.VEHICLE_COLUMN_LICENSE_PLATE,
                Database.VEHICLE_COLUMN_MAKE_MODEL,
                Database.VEHICLE_COLUMN_TYPE,
                Database.VEHICLE_COLUMN_COLOR,
                Database.VEHICLE_COLUMN_VIN,
                Database.VEHICLE_COLUMN_YEAR_OF_MANUFACTURE
        };

        Cursor cursor = db.query(
                Database.VEHICLE_TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_PHONE));
            String licensePlate = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_LICENSE_PLATE));
            String makeModel = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_MAKE_MODEL));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_TYPE));
            String color = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_COLOR));
            String vin = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_VIN));
            String yearOfManufacture = cursor.getString(cursor.getColumnIndexOrThrow(Database.VEHICLE_COLUMN_YEAR_OF_MANUFACTURE));

            Vehicle vehicle = new Vehicle(phone, licensePlate, makeModel, type, color, vin, yearOfManufacture);
            vehicleList.add(vehicle);
        }

        cursor.close();
        db.close();

        return vehicleList;
    }
}
