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
import com.solo11come.models.User;

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
            
            // API Call Removed
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
        // API Call Removed
    }

    private void fetchRecentTransactions() {
        // API Call Removed
    }
}
