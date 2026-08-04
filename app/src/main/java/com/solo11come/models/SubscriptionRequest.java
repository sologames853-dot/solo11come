package com.solo11come.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SubscriptionRequest {
    @SerializedName("sink")
    private String sink;
    @SerializedName("protocol")
    private String protocol;
    @SerializedName("types")
    private List<String> types;
    @SerializedName("config")
    private Config config;

    public SubscriptionRequest(String sink, String protocol, List<String> types, Config config) {
        this.sink = sink;
        this.protocol = protocol;
        this.types = types;
        this.config = config;
    }

    public static class Config {
        @SerializedName("subscriptionDetail")
        private SubscriptionDetail subscriptionDetail;
        @SerializedName("subscriptionMaxEvents")
        private Integer subscriptionMaxEvents;
        @SerializedName("initialEvent")
        private Boolean initialEvent;

        public Config(SubscriptionDetail subscriptionDetail, Integer subscriptionMaxEvents, Boolean initialEvent) {
            this.subscriptionDetail = subscriptionDetail;
            this.subscriptionMaxEvents = subscriptionMaxEvents;
            this.initialEvent = initialEvent;
        }
    }

    public static class SubscriptionDetail {
        @SerializedName("device")
        private Device device;

        public SubscriptionDetail(Device device) {
            this.device = device;
        }
    }

    public static class Device {
        @SerializedName("phoneNumber")
        private String phoneNumber;

        public Device(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}
