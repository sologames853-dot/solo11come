package com.solo11come.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.solo11come.R;

public class HelpdeskActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpdesk);

        Button btnFaq = findViewById(R.id.btnFaq);
        btnFaq.setOnClickListener(v -> {
            Toast.makeText(this, "FAQs coming soon!", Toast.LENGTH_SHORT).show();
        });
    }
}
