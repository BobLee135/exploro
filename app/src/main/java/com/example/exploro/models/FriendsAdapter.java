package com.example.exploro.models;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.exploro.R;
import com.example.exploro.models.schemas.User;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private List<User> mUsers;
    private String mUsername;
    private UserModel mUserModel;
    private View mView;



    public FriendsAdapter(List<User> users, String username, UserModel userModel, View view) {
        mUsers = users;
        mUsername = username;
        mUserModel = userModel;
        mView = view;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friends_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.mUsernameTextView.setText(user.getUsername());
        holder.mLevelTextView.setText("Level " + String.valueOf(user.getExperience()/10));
        holder.deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(mView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.delete_friend_dialog);
                TextView dialogView  = (TextView) dialog.findViewById(R.id.username_text_view);
                dialogView.setText(user.getUsername());
                dialog.show();
                Button deleteFriend = (Button) dialog.findViewById(R.id.deleteFriendButton);
                deleteFriend.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        mUserModel.deleteFriend(mUsername, user.getUsername());
                        mUsers.remove(user);
                        if (mUsers.size() == 0){
                            mView.findViewById(R.id.noFriendsText).setVisibility(View.VISIBLE);
                        }
                        notifyItemRemoved(holder.getAdapterPosition());
                        dialog.hide();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mUsernameTextView;
        TextView mLevelTextView;
        ImageView deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mUsernameTextView = itemView.findViewById(R.id.username_text_view);
            mLevelTextView = itemView.findViewById(R.id.level_text_view);
            deleteButton = (ImageView) itemView.findViewById(R.id.deleteButton);
        }
    }
}
