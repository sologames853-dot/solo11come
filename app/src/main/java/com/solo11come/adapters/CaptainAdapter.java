package com.solo11come.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.solo11come.R;
import com.solo11come.models.Player;
import java.util.List;

public class CaptainAdapter extends RecyclerView.Adapter<CaptainAdapter.CaptainViewHolder> {
    public interface OnRoleSelectedListener {
        void onCaptainSelected(Player player);
        void onViceCaptainSelected(Player player);
    }

    private List<Player> playerList;
    private OnRoleSelectedListener listener;
    private Player captain, viceCaptain;

    public CaptainAdapter(List<Player> playerList, OnRoleSelectedListener listener) {
        this.playerList = playerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CaptainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_captain_selection, parent, false);
        return new CaptainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CaptainViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.tvName.setText(player.getName());
        holder.tvRole.setText(player.getRole() + " | " + player.getTeam());
        
        holder.tvC.setText("C\n2x");
        holder.tvVC.setText("VC\n1.5x");

        // Captain Bubble
        holder.tvC.setOnClickListener(v -> {
            captain = player;
            if (viceCaptain == player) viceCaptain = null;
            listener.onCaptainSelected(player);
            notifyDataSetChanged();
        });

        // Vice-Captain Bubble
        holder.tvVC.setOnClickListener(v -> {
            viceCaptain = player;
            if (captain == player) captain = null;
            listener.onViceCaptainSelected(player);
            notifyDataSetChanged();
        });

        // UI Feedback for Selection
        if (captain == player) {
            holder.tvC.setBackgroundResource(R.drawable.circle_filled_red);
            holder.tvC.setTextColor(Color.WHITE);
        } else {
            holder.tvC.setBackgroundResource(R.drawable.circle_outline);
            holder.tvC.setTextColor(Color.BLACK);
        }

        if (viceCaptain == player) {
            holder.tvVC.setBackgroundResource(R.drawable.circle_filled_red);
            holder.tvVC.setTextColor(Color.WHITE);
        } else {
            holder.tvVC.setBackgroundResource(R.drawable.circle_outline);
            holder.tvVC.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public static class CaptainViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvRole, tvC, tvVC;

        public CaptainViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPlayerName);
            tvRole = itemView.findViewById(R.id.tvPlayerRole);
            tvC = itemView.findViewById(R.id.tvC);
            tvVC = itemView.findViewById(R.id.tvVC);
        }
    }
}
