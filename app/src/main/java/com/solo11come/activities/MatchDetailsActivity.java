package com.solo11come.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.solo11come.R;

import android.util.Log;
import android.widget.Toast;
import com.solo11come.api.ApiClient;
import com.solo11come.models.CricketMatch;
import com.solo11come.models.CricketMatchInfoResponse;
import com.solo11come.models.CricketPointsResponse;
import com.solo11come.utils.Constants;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchDetailsActivity extends AppCompatActivity {
    private TextView tvTeams, tvStatus, tvVenue, tvToss, tvScorecardData, tvPointsData;
    private Button btnCreateTeam;
    private String matchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);

        tvTeams = findViewById(R.id.tvTeams);
        tvStatus = findViewById(R.id.tvStatus);
        tvVenue = findViewById(R.id.tvVenue);
        tvToss = findViewById(R.id.tvToss);
        tvScorecardData = findViewById(R.id.tvScorecardData);
        tvPointsData = findViewById(R.id.tvPointsData);
        btnCreateTeam = findViewById(R.id.btnCreateTeam);

        matchId = getIntent().getStringExtra("matchId");
        String team1 = getIntent().getStringExtra("team1");
        String team2 = getIntent().getStringExtra("team2");
        tvTeams.setText(team1 + " vs " + team2);

        if (matchId != null) {
            fetchMatchInfo();
            fetchMatchPoints();
        }

        btnCreateTeam.setOnClickListener(v -> {
            Intent intent = new Intent(MatchDetailsActivity.this, TeamSelectionActivity.class);
            intent.putExtra("matchId", matchId);
            intent.putExtra("team1", team1);
            intent.putExtra("team2", team2);
            startActivity(intent);
        });
    }

    private void fetchMatchPoints() {
        ApiClient.getCricketInterface().getMatchPoints(Constants.CRICKET_API_KEY, matchId)
                .enqueue(new Callback<CricketPointsResponse>() {
                    @Override
                    public void onResponse(Call<CricketPointsResponse> call, Response<CricketPointsResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            List<CricketPointsResponse.PlayerPoints> totals = response.body().getData().getTotals();
                            if (totals != null && !totals.isEmpty()) {
                                StringBuilder sb = new StringBuilder();
                                for (CricketPointsResponse.PlayerPoints p : totals) {
                                    sb.append(String.format("%-25s : %.1f pts\n", p.getName(), p.getPoints()));
                                }
                                tvPointsData.setText(sb.toString());
                            } else {
                                tvPointsData.setText("Points not available yet.");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CricketPointsResponse> call, Throwable t) {
                        Log.e("MATCH_POINTS", "Error: " + t.getMessage());
                    }
                });
    }

    private void fetchMatchInfo() {
        ApiClient.getCricketInterface().getMatchInfo(Constants.CRICKET_API_KEY, matchId)
                .enqueue(new Callback<CricketMatchInfoResponse>() {
                    @Override
                    public void onResponse(Call<CricketMatchInfoResponse> call, Response<CricketMatchInfoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            CricketMatch match = response.body().getData();
                            if (match != null) {
                                tvStatus.setText(match.getStatus());
                                tvVenue.setText("Venue: " + match.getVenue());
                                if (match.getTossWinner() != null) {
                                    tvToss.setText("Toss: " + match.getTossWinner() + " opted to " + match.getTossChoice());
                                }

                                if (match.getScorecard() != null && !match.getScorecard().isEmpty()) {
                                    StringBuilder sb = new StringBuilder();
                                    for (CricketMatch.ScorecardInning inning : match.getScorecard()) {
                                        sb.append("\n--- ").append(inning.getInningName()).append(" ---\n");
                                        sb.append("BATTING:\n");
                                        for (CricketMatch.Batting b : inning.getBatting()) {
                                            sb.append(String.format("%-15s %d(%d) %s\n", 
                                                b.getBatsman().getName(), b.getRuns(), b.getBalls(), b.getDismissalText()));
                                        }
                                        sb.append("\nBOWLING:\n");
                                        for (CricketMatch.Bowling bowl : inning.getBowling()) {
                                            sb.append(String.format("%-15s %s- %d- %d- %d\n", 
                                                bowl.getBowler().getName(), bowl.getOvers(), bowl.getMaidens(), bowl.getRuns(), bowl.getWickets()));
                                        }
                                    }
                                    tvScorecardData.setText(sb.toString());
                                } else {
                                    tvScorecardData.setText("Scorecard not available yet.");
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CricketMatchInfoResponse> call, Throwable t) {
                        Log.e("MATCH_DETAILS", "Error: " + t.getMessage());
                    }
                });
    }
}
