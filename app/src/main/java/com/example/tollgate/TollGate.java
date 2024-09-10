package com.example.tollgate;

public class TollGate {
    private String id;
    private String name;
    private String location;
    private String operationalHours;
    private double amount;

    // Constructor including id
    public TollGate(String id, String name, String location, String operationalHours, double amount) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.operationalHours = operationalHours;
        this.amount = amount;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getOperationalHours() {
        return operationalHours;
    }

    public double getAmount() {
        return amount;
    }
}
