package com.solo11come.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CricScoreResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<CricScoreData> data;

    public String getStatus() { return status; }
    public List<CricScoreData> getData() { return data; }

    public static class CricScoreData {
        @SerializedName("id")
        private String id;
        @SerializedName("dateTimeGMT")
        private String dateTimeGMT;
        @SerializedName("matchType")
        private String matchType;
        @SerializedName("status")
        private String status;
        @SerializedName("ms")
        private String matchState; // fixture, live, result
        @SerializedName("t1")
        private String t1;
        @SerializedName("t2")
        private String t2;
        @SerializedName("t1s")
        private String t1s;
        @SerializedName("t2s")
        private String t2s;
        @SerializedName("t1img")
        private String t1img;
        @SerializedName("t2img")
        private String t2img;
        @SerializedName("series")
        private String series;

        public String getId() { return id; }
        public String getDateTimeGMT() { return dateTimeGMT; }
        public String getMatchType() { return matchType; }
        public String getStatus() { return status; }
        public String getMatchState() { return matchState; }
        public String getT1() { return t1; }
        public String getT2() { return t2; }
        public String getT1s() { return t1s; }
        public String getT2s() { return t2s; }
        public String getT1img() { return t1img; }
        public String getT2img() { return t2img; }
        public String getSeries() { return series; }
    }
}
