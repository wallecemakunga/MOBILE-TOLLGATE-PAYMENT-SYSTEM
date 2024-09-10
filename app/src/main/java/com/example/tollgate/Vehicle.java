package com.example.tollgate;

public class Vehicle {
    private String phone;
    private String licensePlate;
    private String makeModel;
    private String type;
    private String color;
    private String vin;
    private String yearOfManufacture;

    // Constructor
    public Vehicle(String phone, String licensePlate, String makeModel, String type,
                   String color, String vin, String yearOfManufacture) {
        this.phone = phone;
        this.licensePlate = licensePlate;
        this.makeModel = makeModel;
        this.type = type;
        this.color = color;
        this.vin = vin;
        this.yearOfManufacture = yearOfManufacture;
    }

    // Getters
    public String getPhone() {
        return phone;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getMakeModel() {
        return makeModel;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public String getVin() {
        return vin;
    }

    public String getYearOfManufacture() {
        return yearOfManufacture;
    }
}
