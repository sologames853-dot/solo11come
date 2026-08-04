package com.solo11come.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CricketPlayerInfoResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private PlayerDetail data;

    public PlayerDetail getData() {
        return data;
    }

    public static class PlayerDetail {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("role")
        private String role;
        @SerializedName("battingStyle")
        private String battingStyle;
        @SerializedName("playerImg")
        private String playerImg;
        @SerializedName("country")
        private String country;
        @SerializedName("stats")
        private List<Stat> stats;

        public String getName() { return name; }
        public String getRole() { return role; }
        public String getPlayerImg() { return playerImg; }
        public List<Stat> getStats() { return stats; }
        public String getCountry() { return country; }
    }

    public static class Stat {
        @SerializedName("fn")
        private String fn;
        @SerializedName("matchtype")
        private String matchtype;
        @SerializedName("stat")
        private String stat;
        @SerializedName("value")
        private String value;

        public String getFn() { return fn; }
        public String getMatchtype() { return matchtype; }
        public String getStat() { return stat; }
        public String getValue() { return value; }
    }
}
