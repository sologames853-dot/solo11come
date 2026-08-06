package com.solo11come.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.solo11come.R;

import android.util.Log;
import android.widget.Toast;
import com.solo11come.models.SubscriptionResponse;
import com.solo11come.utils.NetworkStatusHelper;

public class LoginActivity extends AppCompatActivity {
    private EditText etPhone;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPhone = findViewById(R.id.etPhone);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim().replaceAll("\\s+", "");
            if (phone.isEmpty()) {
                Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Automatically prepend +91 if not present
            if (!phone.startsWith("+")) {
                if (phone.startsWith("91") && phone.length() > 10) {
                    phone = "+" + phone;
                } else {
                    phone = "+91" + phone;
                }
            }

            // Simple E.164 validation: + followed by 10-15 digits
            if (!phone.matches("^\\+[1-9]\\d{1,14}$")) {
                Toast.makeText(this, "Invalid phone number. Please enter a valid 10-digit number.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save UserID (Phone) in SharedPreferences
            getSharedPreferences("Solo11", MODE_PRIVATE).edit().putString("userId", phone).apply();

            final String finalPhone = phone;
            Toast.makeText(this, "Checking reachability for " + finalPhone, Toast.LENGTH_SHORT).show();
            
            NetworkStatusHelper.subscribeToReachability(finalPhone, "https://webhook.site/0500bbab-20ms-hf97-94e2-ca891d16p143", new NetworkStatusHelper.SubscriptionCallback() {
                @Override
                public void onSuccess(SubscriptionResponse response) {
                    Log.d("API_SUCCESS", "Subscription ID: " + response.getId());
                }

                @Override
                public void onError(String message) {
                    Log.e("API_ERROR", "Optional API Failed: " + message);
                    // We don't show a toast here to avoid confusing the user 
                    // since this is an optional network check feature.
                }
            });

            // Proceed to Home
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        });
    }
}
