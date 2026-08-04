package com.solo11come.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SubscriptionResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("sink")
    private String sink;
    @SerializedName("protocol")
    private String protocol;
    @SerializedName("types")
    private List<String> types;
    @SerializedName("status")
    private String status;
    @SerializedName("startsAt")
    private String startsAt;
    @SerializedName("expiresAt")
    private String expiresAt;

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
