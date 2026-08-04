package com.solo11come.models;
import java.util.List;
public class ContestResponse {
    private String status;
    private List<Contest> data;
    public String getStatus() { return status; }
    public List<Contest> getData() { return data; }
}
