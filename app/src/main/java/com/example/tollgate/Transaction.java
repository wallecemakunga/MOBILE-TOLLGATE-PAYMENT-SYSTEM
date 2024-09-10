// Transaction.java
package com.example.tollgate;

public class Transaction {
    private int id;
    private String phoneNumber;
    private String date;
    private double amount;
    private String description;

    // Constructor with parameters
    public Transaction(int id, String phoneNumber, String date, double amount, String description) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.amount = amount;
        this.description = description;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDate() { return date; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }

    public void setId(int id) { this.id = id; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setDate(String date) { this.date = date; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
}
