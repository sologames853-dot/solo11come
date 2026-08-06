package com.solo11come.api;

import com.solo11come.network.RetrofitClient;
import com.solo11come.network.CricketApiClient;

public class ApiClient {
    public static ApiInterface getInterface() {
        return RetrofitClient.getClient().create(ApiInterface.class);
    }

    public static CricketApiInterface getCricketInterface() {
        return CricketApiClient.getClient().create(CricketApiInterface.class);
    }
}
