package com.solo11come.api;

import com.solo11come.network.RetrofitClient;
import com.solo11come.network.CricketApiClient;
import com.solo11come.network.RapidApiClient;

public class ApiClient {
    public static ApiInterface getInterface() {
        return RetrofitClient.getClient().create(ApiInterface.class);
    }

    public static CricketApiInterface getCricketInterface() {
        return CricketApiClient.getClient().create(CricketApiInterface.class);
    }

    public static RapidApiInterface getRapidInterface() {
        return RapidApiClient.getClient().create(RapidApiInterface.class);
    }
}
