package com.solo11come.api;

import com.solo11come.network.RetrofitClient;
import com.solo11come.network.RapidApiClient;
import com.solo11come.network.CricketApiClient;
import com.solo11come.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static ApiInterface getInterface() {
        return RetrofitClient.getClient().create(ApiInterface.class);
    }

    public static DeviceStatusApi getDeviceStatusInterface() {
        return RapidApiClient.getClient().create(DeviceStatusApi.class);
    }

    public static CricketApiInterface getCricketInterface() {
        return CricketApiClient.getClient().create(CricketApiInterface.class);
    }

    public static MongoApiInterface getMongoInterface() {
        return new Retrofit.Builder()
                .baseUrl(Constants.MONGO_DATA_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MongoApiInterface.class);
    }
}
