package com.solo11come.models;
public class DepositRequest {
    private String userId;
    private double amount;
    private String utr;
    public DepositRequest(String userId, double amount, String utr) {
        this.userId = userId;
        this.amount = amount;
        this.utr = utr;
    }
}
