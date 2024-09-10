package com.example.tollgate;

public class User {
    private String username;
    private String phone;

    public User(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }
}
