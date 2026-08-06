package com.solo11come.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.solo11come.R;
import com.solo11come.adapters.MyContestAdapter;
import com.solo11come.models.UserContestResponse;
import java.util.ArrayList;
import java.util.List;

public class MyContestsActivity extends AppCompatActivity {
    private RecyclerView rv;
    private MyContestAdapter adapter;
    private List<UserContestResponse.UserContest> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contests);

        rv = findViewById(R.id.rvMyContests);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyContestAdapter(list);
        rv.setAdapter(adapter);

        fetchMyContests();
    }

    private void fetchMyContests() {
        // API Call Removed
    }
}
