package com.example.fotcast;

public class UserModel {
    public String displayName;
    public String email;
    public String registrationNumber;

    public UserModel() {
        // Default constructor required for Firebase
    }

    public UserModel(String displayName, String email, String registrationNumber) {
        this.displayName = displayName;
        this.email = email;
        this.registrationNumber = registrationNumber;
    }
}
