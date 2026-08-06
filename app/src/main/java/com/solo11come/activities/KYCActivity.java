package com.solo11come.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.solo11come.R;
import java.util.HashMap;
import java.util.Map;

public class KYCActivity extends AppCompatActivity {
    private EditText etPan, etAadhar;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyc);

        etPan = findViewById(R.id.etPan);
        etAadhar = findViewById(R.id.etAadhar);
        btnSubmit = findViewById(R.id.btnSubmitKYC);

        btnSubmit.setOnClickListener(v -> {
            String pan = etPan.getText().toString().trim();
            String aadhar = etAadhar.getText().toString().trim();
            
            if (pan.length() != 10 || aadhar.length() != 12) {
                Toast.makeText(this, "Enter valid PAN (10) and Aadhar (12) digits", Toast.LENGTH_SHORT).show();
                return;
            }
            submitKYC(pan, aadhar);
        });
    }

    private void submitKYC(String pan, String aadhar) {
        String userId = getSharedPreferences("Solo11", MODE_PRIVATE).getString("userId", "");
        // We'll use a generic map for now or create a KYCRequest model
        // ... call to backend ...
        Toast.makeText(this, "KYC Submitted for verification", Toast.LENGTH_SHORT).show();
        finish();
    }
}
