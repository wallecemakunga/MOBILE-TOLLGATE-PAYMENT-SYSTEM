package com.example.tollgate;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.SurfaceControl;
import com.example.tollgate.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database extends SQLiteOpenHelper {

    // Database name and version
    public static final String DATABASE_NAME = "TOLLGATE.db";
    public static final int DATABASE_VERSION = 4; // Increment version for new schema changes

    // Table names
    public static final String USER_TABLE_NAME = "users";
    public static final String VEHICLE_TABLE_NAME = "vehicles";
    public static final String TRANSACTION_TABLE_NAME = "transactions";
    public static final String BALANCE_TABLE_NAME = "balances";
    public static final String ADMIN_TABLE_NAME = "admins"; // New admin table
    public static final String TOLL_GATE_TABLE_NAME = "toll_gates"; // New toll gate table


    // User Table Columns
    public static final String USER_COLUMN_USERNAME = "username";
    public static final String USER_COLUMN_PHONE = "phone";
    public static final String USER_COLUMN_PASSWORD = "password";

    // Vehicle Table Columns
    public static final String VEHICLE_COLUMN_ID = "id";
    public static final String VEHICLE_COLUMN_PHONE = "phone";
    public static final String VEHICLE_COLUMN_LICENSE_PLATE = "license_plate";
    public static final String VEHICLE_COLUMN_MAKE_MODEL = "make_model";
    public static final String VEHICLE_COLUMN_TYPE = "type";
    public static final String VEHICLE_COLUMN_COLOR = "color";
    public static final String VEHICLE_COLUMN_VIN = "vin";
    public static final String VEHICLE_COLUMN_YEAR_OF_MANUFACTURE = "year_of_manufacture";

    // Transaction Table Columns
    public static final String TRANSACTION_COLUMN_ID = "id";
    public static final String TRANSACTION_COLUMN_USER_PHONE = "user_phone";
    public static final String TRANSACTION_COLUMN_TOLL_AMOUNT = "toll_amount";
    public static final String TRANSACTION_COLUMN_TOLL_BOOTH_ID = "toll_booth_id";
    public static final String TRANSACTION_COLUMN_TIMESTAMP = "timestamp";

    // Balance Table Columns
    public static final String BALANCE_COLUMN_PHONE = "phone";
    public static final String BALANCE_COLUMN_AMOUNT = "amount";

    // Admin Table Columns
    public static final String ADMIN_COLUMN_PHONE = "phone";
    public static final String ADMIN_COLUMN_PASSWORD = "password";

    // Toll Gate Table Columns
    public static final String TOLL_GATE_COLUMN_ID = "id";
    public static final String TOLL_GATE_COLUMN_NAME = "name";
    public static final String TOLL_GATE_COLUMN_LOCATION = "location";
    public static final String TOLL_GATE_COLUMN_TOLL_AMOUNT = "toll_amount";
    public static final String TOLL_GATE_COLUMN_OPERATIONAL_HOURS = "operational_hours"; // New column


    // Constructor
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create User Table
        String createUserTable = "CREATE TABLE " + USER_TABLE_NAME + " (" +
                USER_COLUMN_USERNAME + " TEXT, " +
                USER_COLUMN_PHONE + " TEXT PRIMARY KEY, " +
                USER_COLUMN_PASSWORD + " TEXT)";

        // Create Vehicle Table
        String createVehicleTable = "CREATE TABLE " + VEHICLE_TABLE_NAME + " (" +
                VEHICLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VEHICLE_COLUMN_PHONE + " TEXT, " +
                VEHICLE_COLUMN_LICENSE_PLATE + " TEXT, " +
                VEHICLE_COLUMN_MAKE_MODEL + " TEXT, " +
                VEHICLE_COLUMN_TYPE + " TEXT, " +
                VEHICLE_COLUMN_COLOR + " TEXT, " +
                VEHICLE_COLUMN_VIN + " TEXT, " +
                VEHICLE_COLUMN_YEAR_OF_MANUFACTURE + " TEXT, " +
                "FOREIGN KEY (" + VEHICLE_COLUMN_PHONE + ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COLUMN_PHONE + "))";

        // Create Transaction Table
        String createTransactionTable = "CREATE TABLE " + TRANSACTION_TABLE_NAME + " (" +
                TRANSACTION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TRANSACTION_COLUMN_USER_PHONE + " TEXT, " +
                TRANSACTION_COLUMN_TOLL_AMOUNT + " REAL, " +
                TRANSACTION_COLUMN_TOLL_BOOTH_ID + " TEXT, " +
                TRANSACTION_COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (" + TRANSACTION_COLUMN_USER_PHONE + ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COLUMN_PHONE + "))";

        // Create Balance Table
        String createBalanceTable = "CREATE TABLE " + BALANCE_TABLE_NAME + " (" +
                BALANCE_COLUMN_PHONE + " TEXT PRIMARY KEY, " +
                BALANCE_COLUMN_AMOUNT + " REAL, " +
                "FOREIGN KEY (" + BALANCE_COLUMN_PHONE + ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COLUMN_PHONE + "))";

        // Create Admin Table
        // Create Admin Table
        String createAdminTable = "CREATE TABLE " + ADMIN_TABLE_NAME + " (" +
                ADMIN_COLUMN_PHONE + " TEXT PRIMARY KEY, " +
                ADMIN_COLUMN_PASSWORD + " TEXT)";

        // Create Toll Gate Table
        String createTollGateTable = "CREATE TABLE " + TOLL_GATE_TABLE_NAME + " (" +
                TOLL_GATE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TOLL_GATE_COLUMN_NAME + " TEXT, " +
                TOLL_GATE_COLUMN_LOCATION + " TEXT, " +
                TOLL_GATE_COLUMN_TOLL_AMOUNT + " REAL, " +
                TOLL_GATE_COLUMN_OPERATIONAL_HOURS + " TEXT)"; // Define operational hours column


// Execute the table creation statements
        db.execSQL(createUserTable);
        db.execSQL(createVehicleTable);
        db.execSQL(createTransactionTable);
        db.execSQL(createBalanceTable);
        db.execSQL(createAdminTable);
        db.execSQL(createTollGateTable);

// Optionally, insert a default admin user
        ContentValues adminValues = new ContentValues();
        adminValues.put(ADMIN_COLUMN_PHONE, "0655239585"); // Replace with actual admin phone number
        adminValues.put(ADMIN_COLUMN_PASSWORD, "Choppa@1234"); // Replace with actual admin password
        db.insert(ADMIN_TABLE_NAME, null, adminValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Create Transaction Table if upgrading from version 1 to 2
            String createTransactionTable = "CREATE TABLE " + TRANSACTION_TABLE_NAME + " (" +
                    TRANSACTION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TRANSACTION_COLUMN_USER_PHONE + " TEXT, " +
                    TRANSACTION_COLUMN_TOLL_AMOUNT + " REAL, " +
                    TRANSACTION_COLUMN_TOLL_BOOTH_ID + " TEXT, " +
                    TRANSACTION_COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (" + TRANSACTION_COLUMN_USER_PHONE + ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COLUMN_PHONE + "))";
            db.execSQL(createTransactionTable);
        }

        if (oldVersion < 3) {
            // Create Balance Table if upgrading from version 2 to 3
            String createBalanceTable = "CREATE TABLE " + BALANCE_TABLE_NAME + " (" +
                    BALANCE_COLUMN_PHONE + " TEXT PRIMARY KEY, " +
                    BALANCE_COLUMN_AMOUNT + " REAL, " +
                    "FOREIGN KEY (" + BALANCE_COLUMN_PHONE + ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COLUMN_PHONE + "))";
            db.execSQL(createBalanceTable);
        }

        if (oldVersion < 4) {
            // Create Admin Table if upgrading from version 3 to 4
            String createAdminTable = "CREATE TABLE " + ADMIN_TABLE_NAME + " (" +
                    ADMIN_COLUMN_PHONE + " TEXT PRIMARY KEY, " +
                    ADMIN_COLUMN_PASSWORD + " TEXT)";
            db.execSQL(createAdminTable);

            // Optionally, insert a default admin user during upgrade
            ContentValues adminValues = new ContentValues();
            adminValues.put(ADMIN_COLUMN_PHONE, "0655239585"); // Replace with actual admin phone number
            adminValues.put(ADMIN_COLUMN_PASSWORD, "Choppa@1234"); // Replace with actual admin password
            db.insert(ADMIN_TABLE_NAME, null, adminValues);
        }
        if (oldVersion < 5) {
            // Create Toll Gate Table if upgrading from version 4 to 5
            String createTollGateTable = "CREATE TABLE " + TOLL_GATE_TABLE_NAME + " (" +
                    TOLL_GATE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TOLL_GATE_COLUMN_NAME + " TEXT, " +
                    TOLL_GATE_COLUMN_LOCATION + " TEXT, " +
                    TOLL_GATE_COLUMN_TOLL_AMOUNT + " REAL, " +
                    TOLL_GATE_COLUMN_OPERATIONAL_HOURS + " TEXT)";
            db.execSQL(createTollGateTable);
        }
    }

    // Method to register a new user
    public void register(String username, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_COLUMN_USERNAME, username);
        values.put(USER_COLUMN_PHONE, phone);
        values.put(USER_COLUMN_PASSWORD, password); // Use hashed password
        long newRowId = db.insert(USER_TABLE_NAME, null, values);

        // Initialize balance for the new user
        if (newRowId != -1) {
            addInitialBalance(phone, 0.0); // Add zero initial balance
        }

        db.close();
        Log.d("Database", "User registered with phone: " + phone);
    }


    public long insertTransaction(String userPhone, double tollAmount, String tollBoothId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;
        db.beginTransaction();
        try {
            // Insert transaction record
            ContentValues transactionValues = new ContentValues();
            transactionValues.put(TRANSACTION_COLUMN_USER_PHONE, userPhone);
            transactionValues.put(TRANSACTION_COLUMN_TOLL_AMOUNT, tollAmount);
            transactionValues.put(TRANSACTION_COLUMN_TOLL_BOOTH_ID, tollBoothId);
            result = db.insert(TRANSACTION_TABLE_NAME, null, transactionValues);

            if (result != -1) {
                // Update user's balance
                double currentBalance = getBalance(userPhone);
                double newBalance = currentBalance - tollAmount;
                ContentValues balanceValues = new ContentValues();
                balanceValues.put(BALANCE_COLUMN_AMOUNT, newBalance);
                String balanceWhereClause = BALANCE_COLUMN_PHONE + "=?";
                String[] balanceWhereArgs = {userPhone};
                db.update(BALANCE_TABLE_NAME, balanceValues, balanceWhereClause, balanceWhereArgs);

                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
            db.close();
        }
        return result;
    }

    public Admin getAdminDetails(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Admin admin = null;
        String[] columns = {ADMIN_COLUMN_PHONE, ADMIN_COLUMN_PASSWORD}; // Include more columns as needed
        String selection = ADMIN_COLUMN_PHONE + "=?";
        String[] selectionArgs = {phone};

        Cursor cursor = db.query(ADMIN_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String adminPhone = cursor.getString(cursor.getColumnIndex(ADMIN_COLUMN_PHONE));
            @SuppressLint("Range") String adminPassword = cursor.getString(cursor.getColumnIndex(ADMIN_COLUMN_PASSWORD));
            // Assuming Admin class has a constructor: Admin(String phone, String password)
            admin = new Admin(adminPhone, adminPassword);
        }
        cursor.close();
        db.close();
        return admin;
    }

    // method to update balance
    public boolean updateUserBalance(String phone, double newBalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BALANCE_COLUMN_AMOUNT, newBalance);

        int rowsAffected = db.update(BALANCE_TABLE_NAME, values, BALANCE_COLUMN_PHONE + "=?", new String[]{phone});
        db.close();
        return rowsAffected > 0;
    }

    // Method to verify login credentials for a user
    public boolean login(String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {USER_COLUMN_PHONE};
        String selection = USER_COLUMN_PHONE + "=? AND " + USER_COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {phone, password};
        Cursor cursor = db.query(USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        boolean loggedIn = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return loggedIn;
    }

    public boolean insertVehicleData(String phone, String licensePlate, String makeModel, String type, String color, String vin, String yearOfManufacture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VEHICLE_COLUMN_PHONE, phone);  // Make sure this is correctly referencing the phone column
        values.put(VEHICLE_COLUMN_LICENSE_PLATE, licensePlate);
        values.put(VEHICLE_COLUMN_MAKE_MODEL, makeModel);
        values.put(VEHICLE_COLUMN_TYPE, type);
        values.put(VEHICLE_COLUMN_COLOR, color);
        values.put(VEHICLE_COLUMN_VIN, vin);
        values.put(VEHICLE_COLUMN_YEAR_OF_MANUFACTURE, yearOfManufacture);

        long result = db.insert(VEHICLE_TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }


    // Method to verify login credentials for an admin
    public boolean adminLogin(String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ADMIN_COLUMN_PHONE};
        String selection = ADMIN_COLUMN_PHONE + "=? AND " + ADMIN_COLUMN_PASSWORD + "=?";
        String[] selectionArgs = {phone, password};
        Cursor cursor = db.query(ADMIN_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        boolean loggedIn = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return loggedIn;
    }


    // Method to check if a phone number is already registered
    public boolean checkPhoneNumber(String phoneNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {USER_COLUMN_PHONE};
        String selection = USER_COLUMN_PHONE + "=?";
        String[] selectionArgs = {phoneNumber};
        Cursor cursor = db.query(USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    public void initializeUserBalance(String phone) {
        if (!checkBalanceEntryExists(phone)) {
            addInitialBalance(phone, 0.0);
        }
    }

    private boolean checkBalanceEntryExists(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {BALANCE_COLUMN_PHONE};
        String selection = BALANCE_COLUMN_PHONE + "=?";
        String[] selectionArgs = {phone};
        try (Cursor cursor = db.query(BALANCE_TABLE_NAME, columns, selection, selectionArgs, null, null, null)) {
            return cursor.getCount() > 0;
        }
    }


    // Method to get the current balance for a user by phone number
    @SuppressLint("Range")
    public double getBalance(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(BALANCE_TABLE_NAME,
                new String[]{BALANCE_COLUMN_AMOUNT},
                BALANCE_COLUMN_PHONE + "=?",
                new String[]{phone},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            double balance = cursor.getDouble(0);
            cursor.close();
            return balance;
        }

        if (cursor != null) {
            cursor.close();
        }
        return 0; // Default balance if not found
    }

    // Update the user's balance in the database



    // Method to get user's vehicles
    public User getUserDetails(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        String[] columns = {USER_COLUMN_USERNAME, USER_COLUMN_PHONE};
        String selection = USER_COLUMN_PHONE + "=?";
        String[] selectionArgs = {phone};

        Cursor cursor = db.query(USER_TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(USER_COLUMN_USERNAME));
            user = new User(username, phone);
        }

        cursor.close();
        db.close();
        return user;
    }
    public Cursor getUserVehicles(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(VEHICLE_TABLE_NAME,
                null,
                VEHICLE_COLUMN_PHONE + "=?",
                new String[]{phone},
                null, null, null);
    }

    // Method to insert data into toll_gates table
    public boolean insertTollGateData(String tollGateName, String location, String operationalHours, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TOLL_GATE_COLUMN_NAME, tollGateName);
        values.put(TOLL_GATE_COLUMN_LOCATION, location);
        values.put(TOLL_GATE_COLUMN_OPERATIONAL_HOURS, operationalHours);
        values.put(TOLL_GATE_COLUMN_TOLL_AMOUNT, amount);

        long result = db.insert(TOLL_GATE_TABLE_NAME, null, values);
        if (result != -1) {
            // Update the total count of toll gates
            updateTotalTollGatesCount();
        }
        db.close();
        return result != -1;
    }

    private void updateTotalTollGatesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TOLL_GATE_TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            int totalCount = cursor.getInt(0);
            cursor.close();
        }
        db.close();
    }


    // Method to get all details of a toll gate by name
    public TollGate getTollGateDetails(String tollGateName) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define a projection specifying the columns to fetch
        String[] projection = {
                TOLL_GATE_COLUMN_ID,
                TOLL_GATE_COLUMN_NAME,
                TOLL_GATE_COLUMN_LOCATION,
                TOLL_GATE_COLUMN_OPERATIONAL_HOURS,
                TOLL_GATE_COLUMN_TOLL_AMOUNT
        };

        // Define the selection criteria
        String selection = TOLL_GATE_COLUMN_NAME + " = ?";
        String[] selectionArgs = {tollGateName};

        // Query the database
        Cursor cursor = db.query(
                TOLL_GATE_TABLE_NAME, // The table to query
                projection,           // The array of columns to return
                selection,            // The columns for the WHERE clause
                selectionArgs,        // The values for the WHERE clause
                null,                 // Don't group the rows
                null,                 // Don't filter by row groups
                null                  // The sort order
        );

        // Check if the cursor is not empty
        if (cursor != null && cursor.moveToFirst()) {
            // Extract the details from the cursor
            String id = cursor.getString(cursor.getColumnIndexOrThrow(TOLL_GATE_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TOLL_GATE_COLUMN_NAME));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(TOLL_GATE_COLUMN_LOCATION));
            String operationalHours = cursor.getString(cursor.getColumnIndexOrThrow(TOLL_GATE_COLUMN_OPERATIONAL_HOURS));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(TOLL_GATE_COLUMN_TOLL_AMOUNT));

            // Close the cursor
            cursor.close();

            // Return the TollGate object with all parameters
            return new TollGate(id, name, location, operationalHours, amount);
        }

        // If no record found, return null
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }


    public int getTotalTollGatesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TOLL_GATE_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public int getTotalUsersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + USER_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public int getTotalVehiclesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + VEHICLE_TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }


    // Optional: Method to add initial balance for a user
    public void addInitialBalance(String phone, double initialBalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues balanceValues = new ContentValues();
        balanceValues.put(BALANCE_COLUMN_PHONE, phone);
        balanceValues.put(BALANCE_COLUMN_AMOUNT, initialBalance);
        db.insert(BALANCE_TABLE_NAME, null, balanceValues);
        db.close();
    }

    public boolean updateBalance(String phone, double newBalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BALANCE_COLUMN_AMOUNT, newBalance);

        int rowsAffected = db.update(BALANCE_TABLE_NAME, contentValues, BALANCE_COLUMN_PHONE + "=?", new String[]{phone});
        return rowsAffected > 0; // Return true if the balance was updated successfully
    }

    public double getTotalRevenue() {
        double totalRevenue = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT SUM(" + TRANSACTION_COLUMN_TOLL_AMOUNT + ") AS total FROM " + TRANSACTION_TABLE_NAME;
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                totalRevenue = cursor.getDouble(cursor.getColumnIndex("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return totalRevenue;
    }


    public double getOverallBalance() {
        double overallBalance = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT SUM(" + BALANCE_COLUMN_AMOUNT + ") AS total FROM " + BALANCE_TABLE_NAME;
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                overallBalance = cursor.getDouble(cursor.getColumnIndex("total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return overallBalance;
    }

    public Map<String, Double> getTollgateAmounts() {
        Map<String, Double> tollgateAmounts = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT tg." + TOLL_GATE_COLUMN_NAME + ", SUM(t." + TRANSACTION_COLUMN_TOLL_AMOUNT + ") AS total " +
                    "FROM " + TRANSACTION_TABLE_NAME + " t " +
                    "INNER JOIN " + TOLL_GATE_TABLE_NAME + " tg ON t." + TRANSACTION_COLUMN_TOLL_BOOTH_ID + " = tg." + TOLL_GATE_COLUMN_ID +
                    " GROUP BY tg." + TOLL_GATE_COLUMN_NAME;

            cursor = db.rawQuery(query, null);

            while (cursor != null && cursor.moveToNext()) {
                String tollGateName = cursor.getString(cursor.getColumnIndex(TOLL_GATE_COLUMN_NAME));
                double amount = cursor.getDouble(cursor.getColumnIndex("total"));
                tollgateAmounts.put(tollGateName, amount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return tollgateAmounts;
    }

    public List<Transaction> getTransactionsByPhoneNumber(String phoneNumber) {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM transactions WHERE phoneNumber = ?", new String[]{phoneNumber});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                // Use fully qualified class name to avoid conflict
                transactions.add(new com.example.tollgate.Transaction(id, phoneNumber, date, amount, description));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return transactions;
    }

    public List<TollGate> getAllTollGates() {
        List<TollGate> tollGates = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TOLL_GATE_TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(TOLL_GATE_COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(TOLL_GATE_COLUMN_NAME));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(TOLL_GATE_COLUMN_LOCATION));
                String operationalHours = cursor.getString(cursor.getColumnIndexOrThrow(TOLL_GATE_COLUMN_OPERATIONAL_HOURS));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(TOLL_GATE_COLUMN_TOLL_AMOUNT));

                TollGate tollGate = new TollGate(id, name, location, operationalHours, amount);
                tollGates.add(tollGate);
            }
            cursor.close();
        }
        return tollGates;
    }

    public boolean deleteTollGate(String tollGateId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TOLL_GATE_TABLE_NAME, TOLL_GATE_COLUMN_ID + " = ?", new String[]{tollGateId});
        return rowsDeleted > 0;
    }

}
