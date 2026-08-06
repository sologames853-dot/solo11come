package com.solo11come.utils;

import com.solo11come.api.ApiClient;
import com.solo11come.models.SubscriptionRequest;
import com.solo11come.models.SubscriptionResponse;
import java.util.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkStatusHelper {
    public interface SubscriptionCallback {
        void onSuccess(SubscriptionResponse response);
        void onError(String message);
    }

    public static void subscribeToReachability(String phone, String webhookUrl, SubscriptionCallback callback) {
        SubscriptionRequest.Device device = new SubscriptionRequest.Device(phone);
        SubscriptionRequest.SubscriptionDetail detail = new SubscriptionRequest.SubscriptionDetail(device);
        // Using 5 max events as seen in logcat
        SubscriptionRequest.Config config = new SubscriptionRequest.Config(detail, 5, true);
        
        SubscriptionRequest request = new SubscriptionRequest(
                webhookUrl,
                "HTTP",
                // Using the exact type string seen in logcat
                Collections.singletonList("org.camaraproject.device-reachability-status-subscriptions.v0.reachability-data"),
                config
        );

        ApiClient.getRapidInterface().subscribeToReachability(
                Constants.RAPID_API_KEY,
                Constants.RAPID_API_HOST,
                request
        ).enqueue(new Callback<SubscriptionResponse>() {
            @Override
            public void onResponse(Call<SubscriptionResponse> call, Response<SubscriptionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SubscriptionResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}
