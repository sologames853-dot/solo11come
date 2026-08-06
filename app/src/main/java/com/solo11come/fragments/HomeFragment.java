package com.solo11come.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.solo11come.R;
import com.solo11come.activities.ContestActivity;
import com.solo11come.adapters.MatchAdapter;
import com.solo11come.models.Match;
import java.util.ArrayList;
import java.util.List;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.solo11come.activities.HelpdeskActivity;
import com.solo11come.activities.LeaderboardActivity;
import com.solo11come.api.ApiClient;
import com.solo11come.models.CricketMatch;
import com.solo11come.models.CricketMatchResponse;
import com.solo11come.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements MatchAdapter.OnMatchClickListener {
    private RecyclerView rvMatches;
    private MatchAdapter adapter;
    private List<Match> matchList;
    private ImageButton btnHelp, btnLeaderboard;
    private TextView tvAdminAlert, tvEmpty;
    private android.widget.ProgressBar progressBar;
    private android.widget.Button btnRetry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvMatches = view.findViewById(R.id.rvMatches);
        btnHelp = view.findViewById(R.id.btnHelp);
        btnLeaderboard = view.findViewById(R.id.btnLeaderboard);
        tvAdminAlert = view.findViewById(R.id.tvAdminAlert);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        progressBar = view.findViewById(R.id.progressBar);
        btnRetry = view.findViewById(R.id.btnRetry);

        rvMatches.setLayoutManager(new LinearLayoutManager(getContext()));

        matchList = new ArrayList<>();
        adapter = new MatchAdapter(matchList, this);
        rvMatches.setAdapter(adapter);

        btnHelp.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), HelpdeskActivity.class));
        });

        btnLeaderboard.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), LeaderboardActivity.class));
        });

        btnRetry.setOnClickListener(v -> fetchMatches());

        checkUserStatus();
        fetchMatches();

        return view;
    }

    private void checkUserStatus() {
        String userId = getContext().getSharedPreferences("Solo11", android.content.Context.MODE_PRIVATE).getString("userId", "");
        if (userId.isEmpty()) return;

        ApiClient.getInterface().getUserProfile(userId).enqueue(new Callback<com.solo11come.models.User>() {
            @Override
            public void onResponse(Call<com.solo11come.models.User> call, Response<com.solo11come.models.User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.solo11come.models.User user = response.body();
                    if ("APPROVED".equals(user.getKycStatus())) {
                        tvAdminAlert.setVisibility(View.VISIBLE);
                        tvAdminAlert.setText("✔ Your KYC is APPROVED! You can now withdraw winnings.");
                        tvAdminAlert.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"));
                    } else if ("REJECTED".equals(user.getKycStatus())) {
                        tvAdminAlert.setVisibility(View.VISIBLE);
                        tvAdminAlert.setText("✘ KYC REJECTED: Please re-upload valid documents.");
                        tvAdminAlert.setBackgroundColor(android.graphics.Color.parseColor("#D32F2F"));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.solo11come.models.User> call, Throwable t) {}
        });
    }

    private void fetchMatches() {
        if (getContext() == null || !com.solo11come.utils.NetworkUtils.isNetworkAvailable(getContext())) {
            progressBar.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            btnRetry.setVisibility(View.VISIBLE);
            tvEmpty.setText("No internet connection. Please check your network.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        btnRetry.setVisibility(View.GONE);

        ApiClient.getInterface().getBackendMatches()
                .enqueue(new Callback<CricketMatchResponse>() {
                    @Override
                    public void onResponse(Call<CricketMatchResponse> call, Response<CricketMatchResponse> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            CricketMatchResponse matchResponse = response.body();
                            List<CricketMatch> cricketMatches = matchResponse.getData();
                            matchList.clear();
                            if (cricketMatches != null && !cricketMatches.isEmpty()) {
                                for (CricketMatch cm : cricketMatches) {
                                    Match m = new Match();
                                    m.setId(cm.getId());
                                    m.setName(cm.getName());
                                    m.setStatus(cm.getStatus());
                                    m.setScore(cm.getStatus()); // Using status as score summary for now

                                    if (cm.getTeamInfo() != null && cm.getTeamInfo().size() >= 2) {
                                        m.setTeam1(cm.getTeamInfo().get(0).getShortname());
                                        m.setTeam2(cm.getTeamInfo().get(1).getShortname());
                                        m.setTeam1Logo(cm.getTeamInfo().get(0).getImg());
                                        m.setTeam2Logo(cm.getTeamInfo().get(1).getImg());
                                    } else {
                                        m.setTeam1("T1");
                                        m.setTeam2("T2");
                                    }
                                    m.setMatchTime(cm.getDate() != null ? cm.getDate() : cm.getStatus());
                                    matchList.add(m);
                                }
                                adapter.notifyDataSetChanged();
                            } else {
                                tvEmpty.setVisibility(View.VISIBLE);
                                btnRetry.setVisibility(View.VISIBLE);
                                tvEmpty.setText("No upcoming matches at the moment.");
                            }
                        } else {
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Failed to load matches", Toast.LENGTH_SHORT).show();
                            }
                            tvEmpty.setVisibility(View.VISIBLE);
                            btnRetry.setVisibility(View.VISIBLE);
                            tvEmpty.setText("Failed to load matches");
                        }
                    }

                    @Override
                    public void onFailure(Call<CricketMatchResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                        btnRetry.setVisibility(View.VISIBLE);
                        tvEmpty.setText("Network Error: " + t.getMessage());
                        Log.e("HOME_FRAGMENT", "Error: " + t.getMessage());
                    }
                });
    }

    @Override
    public void onMatchClick(Match match) {
        Intent intent = new Intent(getContext(), ContestActivity.class);
        intent.putExtra("matchId", match.getId());
        intent.putExtra("team1", match.getTeam1());
        intent.putExtra("team2", match.getTeam2());
        startActivity(intent);
    }
}
