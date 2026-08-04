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
        holder.tvTime.setText(match.getMatchTime());
        holder.tvMatchName.setText(match.getName()); // Set Match/Tournament Name

        if ("started".equalsIgnoreCase(match.getStatus()) || 
            (match.getStatus() != null && match.getStatus().toLowerCase().contains("trail"))) {
            holder.tvStatus.setVisibility(View.VISIBLE);
            holder.tvLiveScore.setVisibility(View.VISIBLE);
            holder.tvLiveScore.setText(match.getScore());
            holder.tvTime.setTextColor(android.graphics.Color.parseColor("#4CAF50")); // Green for live
        } else {
            holder.tvStatus.setVisibility(View.GONE);
            holder.tvLiveScore.setVisibility(View.GONE);
            holder.tvTime.setTextColor(android.graphics.Color.parseColor("#D32F2F")); // Red for upcoming
        }
        
        Glide.with(holder.itemView.getContext())
                .load(match.getTeam1Logo())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivTeam1Logo);
                
        Glide.with(holder.itemView.getContext())
                .load(match.getTeam2Logo())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivTeam2Logo);

        holder.itemView.setOnClickListener(v -> listener.onMatchClick(match));
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
