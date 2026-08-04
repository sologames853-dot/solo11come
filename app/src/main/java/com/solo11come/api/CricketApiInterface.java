package com.solo11come.api;

import com.solo11come.models.CricketMatchResponse;
import com.solo11come.models.CricketMatchInfoResponse;
import com.solo11come.models.CricketPlayerInfoResponse;
import com.solo11come.models.CricketPlayerListResponse;
import com.solo11come.models.CricketMatchXIResponse;
import com.solo11come.models.CricketPointsResponse;
import com.solo11come.models.CricketSquadResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CricketApiInterface {
    @GET("matches")
    Call<CricketMatchResponse> getMatches(
            @Query("apikey") String apiKey,
            @Query("offset") int offset
    );

    @GET("players")
    Call<CricketPlayerListResponse> getPlayers(
            @Query("apikey") String apiKey,
            @Query("offset") int offset
    );

    @GET("currentMatches")
    Call<CricketMatchResponse> getCurrentMatches(
            @Query("apikey") String apiKey,
            @Query("offset") int offset
    );

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

    @GET("players_info")
    Call<CricketPlayerInfoResponse> getPlayerInfo(
            @Query("apikey") String apiKey,
            @Query("id") String playerId
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
}
