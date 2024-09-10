package com.example.tollgate;

public class TollgateAmount {
    private String name;
    private double amount;

    public TollgateAmount(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }
}
