package com.solo11come.models;

public class Transaction {
    private String _id;
    private double amount;
    private String type; // DEPOSIT, WITHDRAW, etc.
    private String status; // PENDING, COMPLETED, REJECTED
    private String utr;
    private String timestamp;

    public String getId() { return _id; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getUtr() { return utr; }
    public String getTimestamp() { return timestamp; }
}
