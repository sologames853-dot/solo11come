package com.solo11come.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.solo11come.R;
import com.solo11come.models.Contest;
import java.util.List;

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.ContestViewHolder> {
    public interface OnContestClickListener {
        void onJoinClick(Contest contest);
    }

    private List<Contest> contestList;
    private OnContestClickListener listener;

    public ContestAdapter(List<Contest> contestList, OnContestClickListener listener) {
        this.contestList = contestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contest, parent, false);
        return new ContestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContestViewHolder holder, int position) {
        Contest contest = contestList.get(position);
        holder.tvPrizePool.setText("₹" + contest.getPrizePool());
        holder.btnEntry.setText("₹" + contest.getEntryFee());
        holder.tvSpots.setText(contest.getSpots() + " spots");
        
        holder.btnEntry.setOnClickListener(v -> listener.onJoinClick(contest));
    }

    @Override
    public int getItemCount() {
        return contestList.size();
    }

    public static class ContestViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrizePool, tvSpots;
        Button btnEntry;
        ProgressBar progressBar;

        public ContestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPrizePool = itemView.findViewById(R.id.tvPrizePool);
            tvSpots = itemView.findViewById(R.id.tvSpots);
            btnEntry = itemView.findViewById(R.id.btnEntry);
            progressBar = itemView.findViewById(R.id.contestProgressBar);
        }
    }
}
