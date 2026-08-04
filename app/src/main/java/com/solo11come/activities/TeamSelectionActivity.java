package com.solo11come.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.solo11come.R;
import com.solo11come.adapters.PlayerAdapter;
import com.solo11come.models.Player;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import com.solo11come.api.ApiClient;
import com.solo11come.models.CricketSquadResponse;
import com.solo11come.models.CricketMatchXIResponse;
import com.solo11come.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.HashSet;
import java.util.Set;

public class TeamSelectionActivity extends AppCompatActivity implements PlayerAdapter.OnPlayerSelectedListener {
    private RecyclerView rvPlayers;
    private PlayerAdapter adapter;
    private List<Player> playerList;
    private List<Player> selectedPlayers = new ArrayList<>();
    private TextView tvSelectionCount, tvCreditCount;
    private Button btnContinue;
    private double totalCredits = 100.0;
    private double usedCredits = 0.0;
    private String matchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_selection);

        tvSelectionCount = findViewById(R.id.tvSelectionCount);
        tvCreditCount = findViewById(R.id.tvCreditCount);
        btnContinue = findViewById(R.id.btnContinue);
        rvPlayers = findViewById(R.id.rvPlayers);

        rvPlayers.setLayoutManager(new LinearLayoutManager(this));
        
        matchId = getIntent().getStringExtra("matchId");
        playerList = new ArrayList<>();
        adapter = new PlayerAdapter(playerList, this);
        rvPlayers.setAdapter(adapter);

        if (matchId != null) {
            fetchSquad();
        } else {
            loadDummyPlayers();
        }

        updateUI();

        btnContinue.setOnClickListener(v -> {
            if (selectedPlayers.size() == 11) {
                Intent intent = new Intent(TeamSelectionActivity.this, CaptainSelectionActivity.class);
                intent.putExtra("selectedPlayers", (ArrayList<Player>) selectedPlayers);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select 11 players", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchSquad() {
        ApiClient.getCricketInterface().getMatchSquad(Constants.CRICKET_API_KEY, matchId)
                .enqueue(new Callback<CricketSquadResponse>() {
                    @Override
                    public void onResponse(Call<CricketSquadResponse> call, Response<CricketSquadResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<CricketSquadResponse.TeamSquad> squads = response.body().getData();
                            playerList.clear();
                            if (squads != null && !squads.isEmpty()) {
                                for (CricketSquadResponse.TeamSquad team : squads) {
                                    if (team.getPlayers() != null) {
                                        for (CricketSquadResponse.SquadPlayer sp : team.getPlayers()) {
                                            playerList.add(new Player(sp.getId(), sp.getName(), sp.getRole(), 9.0, team.getShortname(), sp.getPlayerImg()));
                                        }
                                    }
                                }
                                fetchPlayingXI();
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d("SQUAD_FETCH", "No squad data from API, loading dummy players");
                                loadDummyPlayers();
                            }
                        } else {
                            loadDummyPlayers();
                        }
                    }

                    @Override
                    public void onFailure(Call<CricketSquadResponse> call, Throwable t) {
                        Log.e("SQUAD_FETCH", "Error: " + t.getMessage());
                        loadDummyPlayers();
                    }
                });
    }

    private void fetchPlayingXI() {
        ApiClient.getCricketInterface().getMatchXI(Constants.CRICKET_API_KEY, matchId)
                .enqueue(new Callback<CricketMatchXIResponse>() {
                    @Override
                    public void onResponse(Call<CricketMatchXIResponse> call, Response<CricketMatchXIResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<CricketMatchXIResponse.TeamXI> squads = response.body().getData();
                            if (squads != null && !squads.isEmpty()) {
                                Set<String> playingIds = new HashSet<>();
                                for (CricketMatchXIResponse.TeamXI team : squads) {
                                    if (team.getPlayers() != null) {
                                        for (CricketMatchXIResponse.XIPlayer p : team.getPlayers()) {
                                            playingIds.add(p.getId());
                                        }
                                    }
                                }
                                
                                for (Player player : playerList) {
                                    if (playingIds.contains(player.getId())) {
                                        player.setPlaying(true);
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                Toast.makeText(TeamSelectionActivity.this, "Lineups Out!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CricketMatchXIResponse> call, Throwable t) {
                        Log.e("XI_FETCH", "Error fetching XI: " + t.getMessage());
                    }
                });
    }

    private void loadDummyPlayers() {
        String t1 = getIntent().getStringExtra("team1");
        String t2 = getIntent().getStringExtra("team2");
        if (t1 == null) t1 = "T1";
        if (t2 == null) t2 = "T2";

        playerList.add(new Player("1", "Player 1", "WK", 9.0, t1, ""));
        playerList.add(new Player("2", "Player 2", "BAT", 10.5, t1, ""));
        playerList.add(new Player("3", "Player 3", "BAT", 9.5, t1, ""));
        playerList.add(new Player("4", "Player 4", "AR", 9.0, t1, ""));
        playerList.add(new Player("5", "Player 5", "BOWL", 8.5, t1, ""));
        playerList.add(new Player("6", "Player 6", "BOWL", 8.0, t1, ""));

        playerList.add(new Player("7", "Player 7", "WK", 9.0, t2, ""));
        playerList.add(new Player("8", "Player 8", "BAT", 10.0, t2, ""));
        playerList.add(new Player("9", "Player 9", "BAT", 9.0, t2, ""));
        playerList.add(new Player("10", "Player 10", "AR", 9.5, t2, ""));
        playerList.add(new Player("11", "Player 11", "BOWL", 8.5, t2, ""));
        playerList.add(new Player("12", "Player 12", "BOWL", 8.0, t2, ""));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPlayerSelected(Player player) {
        if (selectedPlayers.contains(player)) {
            selectedPlayers.remove(player);
            usedCredits -= player.getCredits();
        } else {
            if (selectedPlayers.size() >= 11) {
                Toast.makeText(this, "You can only select 11 players", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Check role limits (Dream11 Style)
            int wk = 0, bat = 0, ar = 0, bowl = 0;
            for (Player p : selectedPlayers) {
                String role = p.getRole().toUpperCase();
                if (role.contains("WK")) wk++;
                else if (role.contains("BAT")) bat++;
                else if (role.contains("AR") || role.contains("AL")) ar++;
                else if (role.contains("BOWL") || role.contains("BLOW")) bowl++;
            }

            String newRole = player.getRole().toUpperCase();
            if ((newRole.contains("WK") && wk >= 4) ||
                (newRole.contains("BAT") && bat >= 6) ||
                ((newRole.contains("AR") || newRole.contains("AL")) && ar >= 4) ||
                ((newRole.contains("BOWL") || newRole.contains("BLOW")) && bowl >= 6)) {
                Toast.makeText(this, "Role limit reached for " + player.getRole(), Toast.LENGTH_SHORT).show();
                return;
            }

            if (usedCredits + player.getCredits() > totalCredits) {
                Toast.makeText(this, "Not enough credits", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedPlayers.add(player);
            usedCredits += player.getCredits();
        }
        updateUI();
        adapter.setSelectedPlayers(selectedPlayers);
    }

    private void updateUI() {
        int wk = 0, bat = 0, ar = 0, bowl = 0;
        for (Player p : selectedPlayers) {
            String role = p.getRole().toUpperCase();
            if (role.contains("WK")) wk++;
            else if (role.contains("BAT")) bat++;
            else if (role.contains("AR") || role.contains("AL")) ar++;
            else if (role.contains("BOWL") || role.contains("BLOW")) bowl++;
        }

        tvSelectionCount.setText(String.format("%d/11 (WK:%d BAT:%d AR:%d BOWL:%d)", 
            selectedPlayers.size(), wk, bat, ar, bowl));
        tvCreditCount.setText("Credits: " + (totalCredits - usedCredits));
        
        // Enable continue only if 11 players and minimum requirements met
        boolean valid = selectedPlayers.size() == 11 && wk >= 1 && bat >= 3 && ar >= 1 && bowl >= 3;
        btnContinue.setEnabled(valid);
        if (selectedPlayers.size() == 11 && !valid) {
            Toast.makeText(this, "Check role requirements: 1-4 WK, 3-6 BAT, 1-4 AR, 3-6 BOWL", Toast.LENGTH_SHORT).show();
        }
    }
}
