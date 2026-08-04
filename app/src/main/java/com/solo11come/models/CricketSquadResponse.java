package com.solo11come.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CricketSquadResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<TeamSquad> data;

    public List<TeamSquad> getData() {
        return data;
    }

    public static class TeamSquad {
        @SerializedName("teamName")
        private String teamName;
        @SerializedName("shortname")
        private String shortname;
        @SerializedName("players")
        private List<SquadPlayer> players;

        public String getTeamName() { return teamName; }
        public String getShortname() { return shortname; }
        public List<SquadPlayer> getPlayers() { return players; }
    }

    public static class SquadPlayer {
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
