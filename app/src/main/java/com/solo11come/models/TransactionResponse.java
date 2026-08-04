package com.solo11come.models;
import java.util.List;
public class TransactionResponse {
    private String status;
    private List<Transaction> data;
    public String getStatus() { return status; }
    public List<Transaction> getData() { return data; }
}
