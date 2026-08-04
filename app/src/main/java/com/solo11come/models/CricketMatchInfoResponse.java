package com.solo11come.models;

import com.google.gson.annotations.SerializedName;

public class CricketMatchInfoResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private CricketMatch data;

    public CricketMatch getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }
}
