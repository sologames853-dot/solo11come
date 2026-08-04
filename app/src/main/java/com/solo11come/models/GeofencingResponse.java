package com.solo11come.models;

import com.google.gson.annotations.SerializedName;

public class GeofencingResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("status")
    private String status;

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
