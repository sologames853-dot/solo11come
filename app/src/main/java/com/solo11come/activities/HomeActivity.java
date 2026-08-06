package com.solo11come.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.solo11come.R;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import com.solo11come.activities.WalletActivity;
import com.solo11come.activities.MyContestsActivity;
import com.solo11come.activities.HelpdeskActivity;
import com.solo11come.activities.KYCActivity;
import com.solo11come.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Solo 11 Come");
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(HomeActivity.this, WalletActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_matches) {
                startActivity(new Intent(HomeActivity.this, MyContestsActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_winner) {
                Toast.makeText(this, "Winners: Coming Soon!", Toast.LENGTH_SHORT).show();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_wallet) {
            startActivity(new Intent(this, WalletActivity.class));
            return true;
        } else if (id == R.id.menu_help) {
            startActivity(new Intent(this, HelpdeskActivity.class));
            return true;
        } else if (id == R.id.menu_refer) {
            shareReferral();
            return true;
        } else if (id == R.id.menu_kyc) {
            startActivity(new Intent(this, KYCActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareReferral() {
        String phone = getSharedPreferences("Solo11", MODE_PRIVATE).getString("userId", "guest");
        String shareMessage = "Join Solo 11 Come! Win Crores! Use my referral code: " + phone + 
                "\nDownload App: http://solo11come.com/download";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(intent, "Share via"));
    }
}
