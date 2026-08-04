package com.solo11come.utils;

import android.util.Log;
import com.solo11come.api.ApiClient;
import com.solo11come.api.DeviceStatusApi;
import com.solo11come.models.GeofencingRequest;
import com.solo11come.models.GeofencingResponse;
import com.solo11come.models.SubscriptionRequest;
import com.solo11come.models.SubscriptionResponse;
import java.util.Collections;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkStatusHelper {

    public interface SubscriptionCallback {
        void onSuccess(SubscriptionResponse response);
        void onError(String message);
    }

    public interface GeofencingCallback {
        void onSuccess(GeofencingResponse response);
        void onError(String message);
    }

    public static void subscribeToReachability(String phoneNumber, String sinkUrl, SubscriptionCallback callback) {
        DeviceStatusApi api = ApiClient.getDeviceStatusInterface();

        SubscriptionRequest.Device device = new SubscriptionRequest.Device(phoneNumber);
        SubscriptionRequest.SubscriptionDetail detail = new SubscriptionRequest.SubscriptionDetail(device);
        SubscriptionRequest.Config config = new SubscriptionRequest.Config(detail, 5, true);
        
        SubscriptionRequest request = new SubscriptionRequest(
                sinkUrl,
                "HTTP",
                Collections.singletonList("org.camaraproject.device-reachability-status-subscriptions.v0.reachability-data"),
                config
        );

        String correlator = UUID.randomUUID().toString();

        api.createSubscription(Constants.RAPID_API_KEY, Constants.RAPID_API_HOST, correlator, request)
                .enqueue(new Callback<SubscriptionResponse>() {
                    @Override
                    public void onResponse(Call<SubscriptionResponse> call, Response<SubscriptionResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            try {
                                String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                                Log.e("API_DETAILED_ERROR", "Status: " + response.code() + " Body: " + errorBody);
                                callback.onError(errorBody);
                            } catch (Exception e) {
                                callback.onError("Error parsing error body");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SubscriptionResponse> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }

    public static void subscribeToGeofencing(String phoneNumber, String sinkUrl, double lat, double lon, int radius, GeofencingCallback callback) {
        DeviceStatusApi api = ApiClient.getDeviceStatusInterface();

        GeofencingRequest.Device device = new GeofencingRequest.Device(phoneNumber);
        GeofencingRequest.Center center = new GeofencingRequest.Center(lat, lon);
        GeofencingRequest.Area area = new GeofencingRequest.Area("CIRCLE", center, radius);
        GeofencingRequest.SubscriptionDetail detail = new GeofencingRequest.SubscriptionDetail(device, area);
        
        GeofencingRequest.Config config = new GeofencingRequest.Config(
                detail,
                true,
                10,
                "2045-03-22T05:40:58.469Z"
        );

        GeofencingRequest request = new GeofencingRequest(
                "HTTP",
                sinkUrl,
                Collections.singletonList("org.camaraproject.geofencing-subscriptions.v0.area-entered"),
                config
        );

        api.createGeofencingSubscription(Constants.RAPID_API_KEY, Constants.RAPID_API_HOST, request)
                .enqueue(new Callback<GeofencingResponse>() {
                    @Override
                    public void onResponse(Call<GeofencingResponse> call, Response<GeofencingResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Failed to create geofencing subscription: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<GeofencingResponse> call, Throwable t) {
                        callback.onError(t.getMessage());
                    }
                });
    }
}
