package com.solo11come.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CricketMatchXIResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<TeamXI> data;

    public String getStatus() { return status; }
    public List<TeamXI> getData() { return data; }

    public static class TeamXI {
        @SerializedName("teamName")
        private String teamName;
        @SerializedName("shortname")
        private String shortname;
        @SerializedName("players")
        private List<XIPlayer> players;

        public String getTeamName() { return teamName; }
        public String getShortname() { return shortname; }
        public List<XIPlayer> getPlayers() { return players; }
    }

    public static class XIPlayer {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("role")
        private String role;
        @SerializedName("playerImg")
        private String playerImg;

        public String getId() { return id; }
        public String getName() { return name; }
        public String getRole() { return role; }
        public String getPlayerImg() { return playerImg; }
    }
}
