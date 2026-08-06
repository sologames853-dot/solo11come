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
    private List<Match> fullMatchList = new ArrayList<>();
    private List<Match> filteredList = new ArrayList<>();
    private ImageButton btnHelp, btnLeaderboard;
    private TextView tvAdminAlert, tvEmpty;
    private android.widget.ProgressBar progressBar;
    private android.widget.Button btnRetry;
    private com.google.android.material.tabs.TabLayout tabLayout;

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
        tabLayout = view.findViewById(R.id.tabLayout);

        rvMatches.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MatchAdapter(filteredList, this);
        rvMatches.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new com.google.android.material.tabs.TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(com.google.android.material.tabs.TabLayout.Tab tab) {
                filterMatches(tab.getPosition());
            }

            @Override
            public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) {}
        });

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

    private void filterMatches(int position) {
        filteredList.clear();
        String targetStatus;
        if (position == 0) {
            // Upcoming
            for (Match m : fullMatchList) {
                if (!"started".equalsIgnoreCase(m.getStatus()) && !"result".equalsIgnoreCase(m.getStatus())) {
                    filteredList.add(m);
                }
            }
        } else if (position == 1) {
            // Live
            for (Match m : fullMatchList) {
                if ("started".equalsIgnoreCase(m.getStatus())) {
                    filteredList.add(m);
                }
            }
        } else {
            // Completed
            for (Match m : fullMatchList) {
                if ("result".equalsIgnoreCase(m.getStatus())) {
                    filteredList.add(m);
                }
            }
        }
        
        adapter.notifyDataSetChanged();
        
        if (filteredList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText("No matches in this category");
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
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
                        tvAdminAlert.setText("✔ Your KYC is APPROVED!");
                        tvAdminAlert.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.solo11come.models.User> call, Throwable t) {}
        });
    }

    private void fetchMatches() {
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getCricketInterface().getMatches(Constants.CRICKET_API_KEY, 0).enqueue(new Callback<CricketMatchResponse>() {
            @Override
            public void onResponse(Call<CricketMatchResponse> call, Response<CricketMatchResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<CricketMatch> cricketMatches = response.body().getData();
                    fullMatchList.clear();
                    if (cricketMatches != null) {
                        for (CricketMatch cm : cricketMatches) {
                            Match m = new Match();
                            m.setId(cm.getId());
                            m.setName(cm.getName());
                            m.setMatchStarted(cm.isMatchStarted());
                            m.setMatchEnded(cm.isMatchEnded());
                            m.setHasSquad(cm.isHasSquad());
                            m.setFantasyEnabled(cm.isFantasyEnabled());
                            
                            // Map CricAPI state to internal status string
                            // status: upcoming | started | result
                            if (cm.isMatchEnded()) {
                                m.setStatus("result");
                            } else if (cm.isMatchStarted()) {
                                m.setStatus("started");
                            } else {
                                m.setStatus("upcoming");
                            }
                            
                            // Better Score Display
                            String scoreStr = cm.getStatus(); // Default to "Match starts at..."
                            if (cm.getScore() != null && !cm.getScore().isEmpty()) {
                                StringBuilder sb = new StringBuilder();
                                for (CricketMatch.ScoreSummary ss : cm.getScore()) {
                                    sb.append(ss.getInning()).append(": ").append(ss.getRuns()).append("/").append(ss.getWickets()).append(" (").append(ss.getOvers()).append(")\n");
                                }
                                scoreStr = sb.toString().trim();
                            }
                            m.setScore(scoreStr);
                            
                            if (cm.getTeamInfo() != null && cm.getTeamInfo().size() >= 2) {
                                m.setTeam1(cm.getTeamInfo().get(0).getShortname());
                                m.setTeam2(cm.getTeamInfo().get(1).getShortname());
                                m.setTeam1Logo(cm.getTeamInfo().get(0).getImg());
                                m.setTeam2Logo(cm.getTeamInfo().get(1).getImg());
                            } else if (cm.getTeams() != null && cm.getTeams().size() >= 2) {
                                // Fallback to basic team names if teamInfo is missing
                                m.setTeam1(cm.getTeams().get(0));
                                m.setTeam2(cm.getTeams().get(1));
                            }

                            m.setMatchTime(cm.getDateTimeGMT());
                            fullMatchList.add(m);
                        }
                        filterMatches(tabLayout.getSelectedTabPosition());
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load matches", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CricketMatchResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
