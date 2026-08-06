package com.solo11come.api;

import com.solo11come.models.CricketMatchResponse;
import com.solo11come.models.User;
import com.solo11come.models.UserTeam;
import com.solo11come.models.ContestResponse;
import com.solo11come.models.JoinRequest;
import com.solo11come.models.JoinResponse;
import com.solo11come.models.LeaderboardResponse;
import com.solo11come.models.DepositRequest;
import com.solo11come.models.UserContestResponse;
import com.solo11come.models.TransactionResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("api/matches")
    Call<CricketMatchResponse> getBackendMatches();

    @GET("api/contests/{matchId}")
    Call<ContestResponse> getContests(@Path("matchId") String matchId);

    @POST("joinContest")
    Call<JoinResponse> joinContest(@Body JoinRequest request);

    @GET("api/leaderboard/{contestId}")
    Call<LeaderboardResponse> getLeaderboard(@Path("contestId") String contestId);

    @GET("api/myContests/{userId}")
    Call<UserContestResponse> getMyContests(@Path("userId") String userId);

    @POST("api/deposit")
    Call<Void> depositMoney(@Body DepositRequest request);

    @GET("api/transactions/{userId}")
    Call<TransactionResponse> getTransactions(@Path("userId") String userId);

    @GET("user/{id}")
    Call<User> getUserProfile(@Path("id") String userId);

    @POST("saveTeam")
    Call<Void> saveTeam(@Body UserTeam team);

    @POST("updateWallet")
    Call<User> updateWallet(@Query("userId") String userId, @Query("amount") double amount);
}
