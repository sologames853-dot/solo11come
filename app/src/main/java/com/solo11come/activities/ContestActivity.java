package com.solo11come.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.solo11come.R;
import com.solo11come.adapters.ContestAdapter;
import com.solo11come.models.Contest;
import java.util.ArrayList;
import java.util.List;

import com.solo11come.api.ApiClient;
import com.solo11come.models.ContestResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;

public class ContestActivity extends AppCompatActivity {
    private RecyclerView rvContests;
    private ContestAdapter adapter;
    private List<Contest> contestList = new ArrayList<>();
    private String matchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        rvContests = findViewById(R.id.rvContests);
        rvContests.setLayoutManager(new LinearLayoutManager(this));

        matchId = getIntent().getStringExtra("matchId");
        
        adapter = new ContestAdapter(contestList, contest -> {
            Intent intent = new Intent(ContestActivity.this, TeamSelectionActivity.class);
            intent.putExtra("matchId", matchId);
            intent.putExtra("team1", getIntent().getStringExtra("team1"));
            intent.putExtra("team2", getIntent().getStringExtra("team2"));
            intent.putExtra("contestId", contest.getId()); // Pass contestId
            startActivity(intent);
        });
        rvContests.setAdapter(adapter);

        if (matchId != null) {
            fetchContests();
        } else {
            loadDummyContests();
        }
    }

    private void fetchContests() {
        ApiClient.getInterface().getContests(matchId).enqueue(new Callback<ContestResponse>() {
            @Override
            public void onResponse(Call<ContestResponse> call, Response<ContestResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contestList.clear();
                    contestList.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    if (contestList.isEmpty()) {
                        loadDummyContests();
                    }
                } else {
                    loadDummyContests();
                }
            }

            @Override
            public void onFailure(Call<ContestResponse> call, Throwable t) {
                loadDummyContests();
                Toast.makeText(ContestActivity.this, "Loading dummy contests (Offline)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDummyContests() {
        contestList.clear();
        // Logic for First Team @ ₹1
        boolean isFirstTeam = getSharedPreferences("Solo11", MODE_PRIVATE).getBoolean("isFirstTeam", true);
        String entryFee = isFirstTeam ? "1" : "49";

        contestList.add(new Contest("10 Crores", entryFee, "1,00,000", "Mega"));
        contestList.add(new Contest("50 Lakhs", "29", "50,000", "Hot"));
        contestList.add(new Contest("2 Lakhs", "10", "20,000", "Practice"));
        adapter.notifyDataSetChanged();
    }
}
