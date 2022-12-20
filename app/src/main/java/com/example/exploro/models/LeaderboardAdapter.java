package com.example.exploro.models;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.exploro.R;
import com.example.exploro.models.schemas.User;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private List<User> mUsers;


    public LeaderboardAdapter(List<User> users) {
        mUsers = users;
        for (User user : users){
            System.out.println(user.username);
            System.out.println(user.experience);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.mUsernameTextView.setText(user.getUsername());
        holder.mLevelTextView.setText(String.valueOf(user.getExperience()/10));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mUsernameTextView;
        TextView mLevelTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mUsernameTextView = itemView.findViewById(R.id.username_text_view);
            mLevelTextView = itemView.findViewById(R.id.level_text_view);
        }
    }
}
