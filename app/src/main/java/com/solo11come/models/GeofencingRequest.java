package com.solo11come.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GeofencingRequest {
    @SerializedName("protocol")
    private String protocol;
    @SerializedName("sink")
    private String sink;
    @SerializedName("types")
    private List<String> types;
    @SerializedName("config")
    private Config config;

    public GeofencingRequest(String protocol, String sink, List<String> types, Config config) {
        this.protocol = protocol;
        this.sink = sink;
        this.types = types;
        this.config = config;
    }

    public static class Config {
        @SerializedName("subscriptionDetail")
        private SubscriptionDetail subscriptionDetail;
        @SerializedName("initialEvent")
        private Boolean initialEvent;
        @SerializedName("subscriptionMaxEvents")
        private Integer subscriptionMaxEvents;
        @SerializedName("subscriptionExpireTime")
        private String subscriptionExpireTime;

        public Config(SubscriptionDetail subscriptionDetail, Boolean initialEvent, Integer subscriptionMaxEvents, String subscriptionExpireTime) {
            this.subscriptionDetail = subscriptionDetail;
            this.initialEvent = initialEvent;
            this.subscriptionMaxEvents = subscriptionMaxEvents;
            this.subscriptionExpireTime = subscriptionExpireTime;
        }
    }

    public static class SubscriptionDetail {
        @SerializedName("device")
        private Device device;
        @SerializedName("area")
        private Area area;

        public SubscriptionDetail(Device device, Area area) {
            this.device = device;
            this.area = area;
        }
    }

    public static class Device {
        @SerializedName("phoneNumber")
        private String phoneNumber;

        public Device(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }

    public static class Area {
        @SerializedName("areaType")
        private String areaType;
        @SerializedName("center")
        private Center center;
        @SerializedName("radius")
        private Integer radius;

        public Area(String areaType, Center center, Integer radius) {
            this.areaType = areaType;
            this.center = center;
            this.radius = radius;
        }
    }

    public static class Center {
        @SerializedName("latitude")
        private Double latitude;
        @SerializedName("longitude")
        private Double longitude;

        public Center(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
