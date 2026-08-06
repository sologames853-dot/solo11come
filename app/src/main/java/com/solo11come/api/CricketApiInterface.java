package com.solo11come.api;

import com.solo11come.models.CricketMatchInfoResponse;
import com.solo11come.models.CricketMatchXIResponse;
import com.solo11come.models.CricketPointsResponse;
import com.solo11come.models.CricketSquadResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CricketApiInterface {
    @GET("match_info")
    Call<CricketMatchInfoResponse> getMatchInfo(
            @Query("apikey") String apiKey,
            @Query("id") String matchId
    );

    @GET("match_points")
    Call<CricketPointsResponse> getMatchPoints(
            @Query("apikey") String apiKey,
            @Query("id") String matchId
    );

    @GET("match_squad")
    Call<CricketSquadResponse> getMatchSquad(
            @Query("apikey") String apiKey,
            @Query("id") String matchId
    );

    @GET("match_xi")
    Call<CricketMatchXIResponse> getMatchXI(
            @Query("apikey") String apiKey,
            @Query("id") String matchId
    );

    @GET("cricScore")
    Call<com.solo11come.models.CricScoreResponse> getCricScore(
            @Query("apikey") String apiKey
    );
}
