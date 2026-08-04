package com.solo11come.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.solo11come.R;
import com.solo11come.api.ApiClient;
import com.solo11come.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {
    private TextView tvBalance, tvKycStatus, tvRecentDepositStatus;
    private Button btnAddCash, btnRefer, btnWithdraw, btnUploadScreenshot;
    private EditText etAmount, etUtr;
    private ImageView ivQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        tvBalance = findViewById(R.id.tvWalletBalance);
        tvKycStatus = findViewById(R.id.tvKycStatus);
        tvRecentDepositStatus = findViewById(R.id.tvRecentDepositStatus);
        btnAddCash = findViewById(R.id.btnAddCash);
        btnRefer = findViewById(R.id.btnRefer);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        btnUploadScreenshot = findViewById(R.id.btnUploadScreenshot);
        etAmount = findViewById(R.id.etAmount);
        etUtr = findViewById(R.id.etUtr);
        ivQrCode = findViewById(R.id.ivQrCode);

        // ... (QR text watcher)
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amt = s.toString();
                if (!amt.isEmpty()) {
                    String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=400x400&data=upi://pay?pa=jummankhan4959@upi%26pn=Solo11come%26cu=INR%26am=" + amt;
                    Glide.with(WalletActivity.this).load(qrUrl).into(ivQrCode);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        fetchWalletBalance();
        fetchRecentTransactions();

        btnUploadScreenshot.setOnClickListener(v -> {
            Toast.makeText(this, "Select screenshot from gallery", Toast.LENGTH_SHORT).show();
        });

        btnAddCash.setOnClickListener(v -> {
            String utr = etUtr.getText().toString().trim();
            String amtStr = etAmount.getText().toString().trim();
            
            if (amtStr.isEmpty() || utr.isEmpty()) {
                Toast.makeText(this, "Enter Amount and UTR Number", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amtStr);
            String userId = getSharedPreferences("Solo11", MODE_PRIVATE).getString("userId", "guest");
            
            com.solo11come.models.DepositRequest request = new com.solo11come.models.DepositRequest(userId, amount, utr);
            ApiClient.getInterface().depositMoney(request).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(WalletActivity.this, "Deposit Request Sent! Approval takes 30-60 mins.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(WalletActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnRefer.setOnClickListener(v -> {
            shareReferral();
        });

        btnWithdraw.setOnClickListener(v -> {
            Toast.makeText(this, "Submit Bank A/C & IFSC for Withdrawal. Approval takes 24h.", Toast.LENGTH_LONG).show();
        });
    }

    private void shareReferral() {
        String phone = getSharedPreferences("Solo11", MODE_PRIVATE).getString("userId", "guest");
        String shareMessage = "Win Real Cash on Solo 11 Come! Use my Referral: " + phone + 
                "\nDownload: http://solo11come.com/app";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        intent.setPackage("com.whatsapp");
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            intent.setPackage(null);
            startActivity(Intent.createChooser(intent, "Refer Friends via"));
        }
    }

    private void fetchWalletBalance() {
        String userId = getSharedPreferences("Solo11", MODE_PRIVATE).getString("userId", "guest");
        ApiClient.getInterface().getUserProfile(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    tvBalance.setText("₹" + user.getBalance());
                    
                    // Set KYC Status
                    String status = user.getKycStatus() != null ? user.getKycStatus() : "NOT SUBMITTED";
                    tvKycStatus.setText("KYC: " + status);
                    if (status.equals("APPROVED")) {
                        tvKycStatus.setBackgroundColor(Color.parseColor("#C8E6C9"));
                        tvKycStatus.setTextColor(Color.parseColor("#2E7D32"));
                        btnWithdraw.setEnabled(true);
                    } else if (status.equals("REJECTED")) {
                        tvKycStatus.setBackgroundColor(Color.parseColor("#FFCDD2"));
                        tvKycStatus.setTextColor(Color.parseColor("#C62828"));
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(WalletActivity.this, "Error fetching balance", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRecentTransactions() {
        String userId = getSharedPreferences("Solo11", MODE_PRIVATE).getString("userId", "guest");
        ApiClient.getInterface().getTransactions(userId).enqueue(new Callback<com.solo11come.models.TransactionResponse>() {
            @Override
            public void onResponse(Call<com.solo11come.models.TransactionResponse> call, Response<com.solo11come.models.TransactionResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getData().isEmpty()) {
                    com.solo11come.models.Transaction lastTxn = response.body().getData().get(0);
                    tvRecentDepositStatus.setText("Last Deposit: " + lastTxn.getStatus());
                    
                    if (lastTxn.getStatus().equals("COMPLETED")) {
                        tvRecentDepositStatus.setBackgroundColor(Color.parseColor("#C8E6C9"));
                        tvRecentDepositStatus.setTextColor(Color.parseColor("#2E7D32"));
                    } else if (lastTxn.getStatus().equals("PENDING")) {
                        tvRecentDepositStatus.setBackgroundColor(Color.parseColor("#FFF9C4"));
                        tvRecentDepositStatus.setTextColor(Color.parseColor("#FBC02D"));
                    }
                }
            }

            @Override
            public void onFailure(Call<com.solo11come.models.TransactionResponse> call, Throwable t) {}
        });
    }
}
