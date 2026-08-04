package com.solo11come.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MongoApiInterface {
    @POST("action/insertOne")
    Call<ResponseBody> insertOne(
            @Header("api-key") String apiKey,
            @Body RequestBody body
    );

    @POST("action/findOne")
    Call<ResponseBody> findOne(
            @Header("api-key") String apiKey,
            @Body RequestBody body
    );

    @POST("action/updateOne")
    Call<ResponseBody> updateOne(
            @Header("api-key") String apiKey,
            @Body RequestBody body
    );
}
