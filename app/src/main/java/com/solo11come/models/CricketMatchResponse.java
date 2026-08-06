package com.solo11come.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CricketMatchResponse {
    @SerializedName("apikey")
    private String apikey;
    @SerializedName("data")
    private List<CricketMatch> data;
    @SerializedName("status")
    private String status;
    @SerializedName("info")
    private CricketInfo info;
    @SerializedName("offset")
    private int offset;

    public String getApikey() {
        return apikey;
    }

    public List<CricketMatch> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public CricketInfo getInfo() {
        return info;
    }

    public int getOffset() {
        return offset;
    }

    public static class CricketInfo {
        @SerializedName("hitsToday")
        private int hitsToday;
        @SerializedName("hitsUsed")
        private int hitsUsed;
        @SerializedName("hitsLimit")
        private int hitsLimit;
        @SerializedName("credits")
        private int credits;
        @SerializedName("server")
        private int server;
        @SerializedName("offsetRows")
        private int offsetRows;
        @SerializedName("totalRows")
        private int totalRows;
        @SerializedName("queryTime")
        private double queryTime;
        @SerializedName("s")
        private int s;
        @SerializedName("cache")
        private int cache;

        public int getHitsToday() { return hitsToday; }
        public int getHitsUsed() { return hitsUsed; }
        public int getHitsLimit() { return hitsLimit; }
        public int getCredits() { return credits; }
        public int getServer() { return server; }
        public int getOffsetRows() { return offsetRows; }
        public int getTotalRows() { return totalRows; }
        public double getQueryTime() { return queryTime; }
        public int getS() { return s; }
        public int getCache() { return cache; }
    }
}
