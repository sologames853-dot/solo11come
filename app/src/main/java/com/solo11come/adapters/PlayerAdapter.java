package com.solo11come.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.solo11come.R;
import com.solo11come.models.Player;
import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
    public interface OnPlayerSelectedListener {
        void onPlayerSelected(Player player);
    }

    private List<Player> playerList;
    private List<Player> selectedPlayers = new ArrayList<>();
    private OnPlayerSelectedListener listener;

    public PlayerAdapter(List<Player> playerList, OnPlayerSelectedListener listener) {
        this.playerList = playerList;
        this.listener = listener;
    }

    public void setSelectedPlayers(List<Player> selectedPlayers) {
        this.selectedPlayers = selectedPlayers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.tvName.setText(player.getName());
        holder.tvRole.setText(player.getRole() + " | " + player.getTeam());
        holder.tvCredits.setText(String.valueOf(player.getCredits()));

        if (player.isPlaying()) {
            holder.tvPlayingStatus.setVisibility(View.VISIBLE);
            holder.tvPlayingStatus.setText("● Playing");
            holder.tvPlayingStatus.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            holder.tvPlayingStatus.setVisibility(View.GONE);
        }

        Glide.with(holder.itemView.getContext())
                .load(player.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivPlayer);

        if (selectedPlayers.contains(player)) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E8F5E9")); // Light green for selected
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(v -> listener.onPlayerSelected(player));
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRole, tvCredits, tvPlayingStatus;
        ImageView ivPlayer;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPlayerName);
            tvRole = itemView.findViewById(R.id.tvPlayerRole);
            tvCredits = itemView.findViewById(R.id.tvPlayerCredits);
            tvPlayingStatus = itemView.findViewById(R.id.tvPlayingStatus);
            ivPlayer = itemView.findViewById(R.id.ivPlayer);
        }
    }
}
