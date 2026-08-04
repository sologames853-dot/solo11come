package com.solo11come.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.solo11come.R;
import com.solo11come.models.UserContestResponse;
import java.util.List;

public class MyContestAdapter extends RecyclerView.Adapter<MyContestAdapter.ViewHolder> {
    private List<UserContestResponse.UserContest> list;

    public MyContestAdapter(List<UserContestResponse.UserContest> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_contest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserContestResponse.UserContest item = list.get(position);
        holder.tvContestName.setText(item.getContestName());
        holder.tvMatchName.setText(item.getMatchName());
        holder.tvPoints.setText("Points: " + item.getTotalPoints());
        holder.tvRank.setText("#" + item.getRank());
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContestName, tvMatchName, tvPoints, tvRank;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContestName = itemView.findViewById(R.id.tvContestName);
            tvMatchName = itemView.findViewById(R.id.tvMatchName);
            tvPoints = itemView.findViewById(R.id.tvPoints);
            tvRank = itemView.findViewById(R.id.tvRank);
        }
    }
}
