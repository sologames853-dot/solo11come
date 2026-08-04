package com.solo11come.utils;

import android.util.Log;
import com.solo11come.api.ApiClient;
import com.solo11come.models.CricketPlayerInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CricketDataHelper {

    public interface PlayerInfoCallback {
        void onSuccess(CricketPlayerInfoResponse.PlayerDetail player);
        void onError(String message);
    }

    public static void getPlayerDetails(String playerId, PlayerInfoCallback callback) {
        ApiClient.getCricketInterface().getPlayerInfo(Constants.CRICKET_API_KEY, playerId)
                .enqueue(new Callback<CricketPlayerInfoResponse>() {
                    @Override
                    public void onResponse(Call<CricketPlayerInfoResponse> call, Response<CricketPlayerInfoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body().getData());
                        } else {
                            callback.onError("Failed to fetch player info");
                        }
                    }

                    @Override
                    public void onFailure(Call<CricketPlayerInfoResponse> call, Throwable t) {
                        Log.e("CricketDataHelper", "Error: " + t.getMessage());
                        callback.onError(t.getMessage());
                    }
                });
    }
}
