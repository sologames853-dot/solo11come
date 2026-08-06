package com.solo11come.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.solo11come.R;

import android.widget.Toast;
import com.solo11come.models.CricketMatch;
import java.util.List;

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
        // API Call Removed
    }

    private void fetchMatchInfo() {
        // API Call Removed
    }
}
