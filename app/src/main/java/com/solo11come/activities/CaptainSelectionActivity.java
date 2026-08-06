package com.solo11come.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.solo11come.R;
import com.solo11come.adapters.CaptainAdapter;
import com.solo11come.models.Player;
import com.solo11come.models.UserTeam;
import java.util.ArrayList;
import java.util.List;

import com.solo11come.api.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaptainSelectionActivity extends AppCompatActivity {
    private RecyclerView rvPlayers;
    private CaptainAdapter adapter;
    private List<Player> selectedPlayers;
    private Button btnSaveTeam;
    private Player captain, viceCaptain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_selection);

        rvPlayers = findViewById(R.id.rvCaptainPlayers);
        btnSaveTeam = findViewById(R.id.btnSaveTeam);

        selectedPlayers = (ArrayList<Player>) getIntent().getSerializableExtra("selectedPlayers");
        String matchId = getIntent().getStringExtra("matchId");

        rvPlayers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CaptainAdapter(selectedPlayers, new CaptainAdapter.OnRoleSelectedListener() {
            @Override
            public void onCaptainSelected(Player player) {
                captain = player;
                if (viceCaptain == captain) viceCaptain = null;
                validateAndEnableButton();
            }

            @Override
            public void onViceCaptainSelected(Player player) {
                viceCaptain = player;
                if (captain == viceCaptain) captain = null;
                validateAndEnableButton();
            }
        });
        rvPlayers.setAdapter(adapter);

        btnSaveTeam.setOnClickListener(v -> {
            if (captain != null && viceCaptain != null) {
                saveTeamToNodeBackend(matchId);
            } else {
                Toast.makeText(this, "Select Captain and Vice-Captain", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateAndEnableButton() {
        btnSaveTeam.setEnabled(captain != null && viceCaptain != null);
    }

    private void saveTeamToNodeBackend(String matchId) {
        String userId = getSharedPreferences("Solo11", MODE_PRIVATE).getString("userId", "guest");
        List<String> playerIds = new ArrayList<>();
        for (Player p : selectedPlayers) {
            playerIds.add(p.getId());
        }

        UserTeam team = new UserTeam(matchId, playerIds, captain.getId(), viceCaptain.getId(), userId);
        team.setSelectedPlayers(selectedPlayers);
        team.setCaptain(captain);
        team.setViceCaptain(viceCaptain);

        ApiClient.getInterface().saveTeam(team).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CaptainSelectionActivity.this, "Team Saved Successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CaptainSelectionActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
