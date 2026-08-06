package com.solo11come.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.solo11come.R;
import com.solo11come.models.Match;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    public interface OnMatchClickListener {
        void onMatchClick(Match match);
    }

    private List<Match> matchList;
    private OnMatchClickListener listener;

    public MatchAdapter(List<Match> matchList, OnMatchClickListener listener) {
        this.matchList = matchList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match match = matchList.get(position);
        holder.tvTeam1.setText(match.getTeam1());
        holder.tvTeam2.setText(match.getTeam2());
        holder.tvMatchName.setText(match.getName());

        String status = match.getStatus();
        
        if ("started".equalsIgnoreCase(status)) {
            // LIVE MATCH
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText("LIVE");
            holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
            holder.tvLiveScore.setVisibility(View.VISIBLE);
            holder.tvLiveScore.setText(match.getScore());
            holder.tvTime.setText("In Progress");
            holder.tvTime.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
        } else if ("result".equalsIgnoreCase(status)) {
            // COMPLETED MATCH
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvStatus.setText("COMPLETED");
            holder.tvStatus.setTextColor(android.graphics.Color.GRAY);
            holder.tvLiveScore.setVisibility(View.VISIBLE);
            holder.tvLiveScore.setText(match.getScore());
            holder.tvTime.setText("Finished");
            holder.tvTime.setTextColor(android.graphics.Color.GRAY);
        } else {
            // UPCOMING MATCH
            holder.tvStatus.setVisibility(View.GONE);
            holder.tvLiveScore.setVisibility(View.GONE);
            updateTimeRemaining(holder.tvTime, match.getMatchTime());
        }
        
        Glide.with(holder.itemView.getContext())
                .load(match.getTeam1Logo())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivTeam1Logo);
                
        Glide.with(holder.itemView.getContext())
                .load(match.getTeam2Logo())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivTeam2Logo);

        holder.itemView.setOnClickListener(v -> {
            if ("result".equalsIgnoreCase(match.getStatus())) {
                android.widget.Toast.makeText(v.getContext(), "Match is completed", android.widget.Toast.LENGTH_SHORT).show();
            } else {
                listener.onMatchClick(match);
            }
        });
    }

    private void updateTimeRemaining(TextView tvTime, String matchTimeStr) {
        try {
            // CricAPI dates are usually like "2023-10-05T08:30:00"
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date matchDate = sdf.parse(matchTimeStr);
            long diff = matchDate.getTime() - System.currentTimeMillis();

            if (diff > 0) {
                long hours = diff / (1000 * 60 * 60);
                long minutes = (diff / (1000 * 60)) % 60;
                long seconds = (diff / 1000) % 60;

                if (hours > 24) {
                    long days = hours / 24;
                    tvTime.setText(days + "d left");
                } else {
                    tvTime.setText(String.format(Locale.getDefault(), "%02dh %02dm left", hours, minutes));
                }
                tvTime.setTextColor(android.graphics.Color.parseColor("#D32F2F"));
            } else {
                tvTime.setText("Starting soon");
                tvTime.setTextColor(android.graphics.Color.parseColor("#D32F2F"));
            }
        } catch (Exception e) {
            tvTime.setText(matchTimeStr);
            tvTime.setTextColor(android.graphics.Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public static class MatchViewHolder extends RecyclerView.ViewHolder {
        TextView tvTeam1, tvTeam2, tvTime, tvStatus, tvLiveScore, tvMatchName;
        ImageView ivTeam1Logo, ivTeam2Logo;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeam1 = itemView.findViewById(R.id.tvTeam1);
            tvTeam2 = itemView.findViewById(R.id.tvTeam2);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvLiveScore = itemView.findViewById(R.id.tvLiveScore);
            tvMatchName = itemView.findViewById(R.id.tvMatchName);
            ivTeam1Logo = itemView.findViewById(R.id.ivTeam1Logo);
            ivTeam2Logo = itemView.findViewById(R.id.ivTeam2Logo);
        }
    }
}
