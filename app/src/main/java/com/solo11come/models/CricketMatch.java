package com.solo11come.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CricketMatch {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("matchType")
    private String matchType;
    @SerializedName("status")
    private String status;
    @SerializedName("venue")
    private String venue;
    @SerializedName("date")
    private String date;
    @SerializedName("dateTimeGMT")
    private String dateTimeGMT;
    @SerializedName("teamInfo")
    private List<TeamInfo> teamInfo;
    
    public String getDateTimeGMT() { return dateTimeGMT; }
    public boolean isMatchStarted() { return matchStarted; }
    public boolean isMatchEnded() { return matchEnded; }
    @SerializedName("matchStarted")
    private boolean matchStarted;
    @SerializedName("matchEnded")
    private boolean matchEnded;
    @SerializedName("tossWinner")
    private String tossWinner;
    @SerializedName("tossChoice")
    private String tossChoice;
    @SerializedName("matchWinner")
    private String matchWinner;
    @SerializedName("scorecard")
    private List<ScorecardInning> scorecard;

    public String getId() { return id; }
    public String getName() { return name; }
    public String getStatus() { return status; }
    public List<TeamInfo> getTeamInfo() { return teamInfo; }
    public String getTossWinner() { return tossWinner; }
    public String getTossChoice() { return tossChoice; }
    public String getMatchWinner() { return matchWinner; }
    public String getVenue() { return venue; }
    public String getDate() { return date; }
    public List<ScorecardInning> getScorecard() { return scorecard; }

    public static class TeamInfo {
        @SerializedName("name")
        private String name;
        @SerializedName("shortname")
        private String shortname;
        @SerializedName("img")
        private String img;

        public String getName() { return name; }
        public String getShortname() { return shortname; }
        public String getImg() { return img; }
    }

    public static class ScorecardInning {
        @SerializedName("inning")
        private String inningName;
        @SerializedName("batting")
        private List<Batting> batting;
        @SerializedName("bowling")
        private List<Bowling> bowling;

        public String getInningName() { return inningName; }
        public List<Batting> getBatting() { return batting; }
        public List<Bowling> getBowling() { return bowling; }
    }

    public static class Batting {
        @SerializedName("batsman")
        private Player batsman;
        @SerializedName("r")
        private int runs;
        @SerializedName("b")
        private int balls;
        @SerializedName("4s")
        private int fours;
        @SerializedName("6s")
        private int sixes;
        @SerializedName("dismissal-text")
        private String dismissalText;

        public Player getBatsman() { return batsman; }
        public int getRuns() { return runs; }
        public int getBalls() { return balls; }
        public int getFours() { return fours; }
        public int getSixes() { return sixes; }
        public String getDismissalText() { return dismissalText; }
    }

    public static class Bowling {
        @SerializedName("bowler")
        private Player bowler;
        @SerializedName("o")
        private double overs;
        @SerializedName("m")
        private int maidens;
        @SerializedName("r")
        private int runs;
        @SerializedName("w")
        private int wickets;
        @SerializedName("eco")
        private double economy;

        public Player getBowler() { return bowler; }
        public double getOvers() { return overs; }
        public int getMaidens() { return maidens; }
        public int getRuns() { return runs; }
        public int getWickets() { return wickets; }
        public double getEconomy() { return economy; }
    }

    public static class Player {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;

        public String getId() { return id; }
        public String getName() { return name; }
    }
}
