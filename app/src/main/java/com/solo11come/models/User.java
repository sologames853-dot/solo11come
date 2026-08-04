package com.solo11come.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("_id")
    private String id;
    private String name;
    private String email;
    private String phone;
    private double balance;
    private String kycStatus; // PENDING, APPROVED, REJECTED

    public User() {}

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public double getBalance() { return balance; }
    public String getKycStatus() { return kycStatus; }
}
