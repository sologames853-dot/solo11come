package com.solo11come.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CricketPlayerListResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<PlayerInfo> data;
    @SerializedName("info")
    private ApiInfo info;

    public String getStatus() { return status; }
    public List<PlayerInfo> getData() { return data; }
    public ApiInfo getInfo() { return info; }

    public static class PlayerInfo {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("country")
        private String country;

        public String getId() { return id; }
        public String getName() { return name; }
        public String getCountry() { return country; }
    }

    public static class ApiInfo {
        @SerializedName("hitsToday")
        private int hitsToday;
        @SerializedName("hitsUsed")
        private int hitsUsed;
        @SerializedName("hitsLimit")
        private int hitsLimit;
        @SerializedName("totalRows")
        private int totalRows;

        public int getTotalRows() { return totalRows; }
    }
}
