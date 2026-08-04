package com.solo11come.models;
public class JoinRequest {
    private String userId;
    private String contestId;
    private String teamId;
    public JoinRequest(String userId, String contestId, String teamId) {
        this.userId = userId;
        this.contestId = contestId;
        this.teamId = teamId;
    }
}
