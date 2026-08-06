package com.solo11come.api;

import com.solo11come.models.SubscriptionRequest;
import com.solo11come.models.SubscriptionResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RapidApiInterface {
    @POST("device-reachability-status-subscriptions/v1/subscriptions")
    Call<SubscriptionResponse> subscribeToReachability(
            @Header("x-rapidapi-key") String apiKey,
            @Header("x-rapidapi-host") String apiHost,
            @Body SubscriptionRequest request
    );
}
