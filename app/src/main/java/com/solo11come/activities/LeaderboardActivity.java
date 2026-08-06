package com.solo11come.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.solo11come.R;
import com.solo11come.models.PlayerStats;
import com.solo11come.models.UserTeam;
import com.solo11come.utils.MatchPointsCalculator;
import java.util.HashMap;
import java.util.Map;
import android.widget.Toast;

public class LeaderboardActivity extends AppCompatActivity {
    private TextView tvMyPoints;
    private String contestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        tvMyPoints = findViewById(R.id.tvMyPoints);
        contestId = getIntent().getStringExtra("contestId");

        if (contestId != null) {
            fetchLeaderboard();
        } else {
            simulatePointsCalculation();
        }
    }

    private void fetchLeaderboard() {
        // API Call Removed
    }

    private void simulatePointsCalculation() {
        // Dummy Live Stats
        Map<String, PlayerStats> liveStats = new HashMap<>();
        PlayerStats p1Stats = new PlayerStats("Player 1");
        p1Stats.setRuns(50); // 50 pts
        p1Stats.setBoundaries(4); // 4 pts
        p1Stats.setSixes(2); // 4 pts
        liveStats.put("Player 1", p1Stats);

        PlayerStats p2Stats = new PlayerStats("Player 2");
        p2Stats.setWickets(2); // 50 pts
        liveStats.put("Player 2", p2Stats);

        // Mock User Team (Captain: Player 1, VC: Player 2)
        // In real app, this comes from Intent or DB
        // UserTeam team = ... 

        double points = 154.5; // Example calculated value
        tvMyPoints.setText("My Total Points: " + points);
    }
}
