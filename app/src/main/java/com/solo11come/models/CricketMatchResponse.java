package com.solo11come.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CricketMatchResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<CricketMatch> data;
    @SerializedName("info")
    private CricketInfo info;

    public List<CricketMatch> getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public static class CricketInfo {
        @SerializedName("hitsToday")
        private int hitsToday;
        @SerializedName("hitsLimit")
        private int hitsLimit;
    }
}
