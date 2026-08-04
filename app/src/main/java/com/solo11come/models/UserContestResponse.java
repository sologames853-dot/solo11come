package com.solo11come.models;
import java.util.List;
public class UserContestResponse {
    private String status;
    private List<UserContest> data;
    public String getStatus() { return status; }
    public List<UserContest> getData() { return data; }
    
    public static class UserContest {
        private String contestName;
        private String matchName;
        private String prizePool;
        private double totalPoints;
        private int rank;

        public String getContestName() { return contestName; }
        public String getMatchName() { return matchName; }
        public String getPrizePool() { return prizePool; }
        public double getTotalPoints() { return totalPoints; }
        public int getRank() { return rank; }
    }
}
