package com.solo11come.models;
import java.util.List;
public class LeaderboardResponse {
    private String status;
    private List<UserTeam> data;
    public String getStatus() { return status; }
    public List<UserTeam> getData() { return data; }
}
