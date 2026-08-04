package com.solo11come.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CricketPointsResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private PointsData data;

    public String getStatus() { return status; }
    public PointsData getData() { return data; }

    public static class PointsData {
        @SerializedName("totals")
        private List<PlayerPoints> totals;

        public List<PlayerPoints> getTotals() { return totals; }
    }

    public static class PlayerPoints {
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("points")
        private double points;

        public String getId() { return id; }
        public String getName() { return name; }
        public double getPoints() { return points; }
    }
}
