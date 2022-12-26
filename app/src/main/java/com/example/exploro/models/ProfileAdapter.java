package com.example.exploro.models;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.exploro.R;
import com.example.exploro.models.schemas.Trips;
import com.example.exploro.models.schemas.User;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private List<Trips> mTravelList;


    public ProfileAdapter(List<Trips> travelList) {
        mTravelList = travelList;
        for (Trips trips : mTravelList){
            System.out.println(trips.getCountry());
            System.out.println(trips.getCity());
            System.out.println(trips.getPlace());

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
        Trips trip = mTravelList.get(position);
        holder.mCityTextView.setText(trip.getCity());
        holder.mCountryTextView.setText(trip.getCountry());
        holder.mPlaceTextView.setText(trip.getPlace());

    }

    @Override
    public int getItemCount() {
        return mTravelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mCityTextView;
        TextView mCountryTextView;
        TextView mPlaceTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            //ONLY TEMP IDS RIGHT NOW
            mCityTextView = itemView.findViewById(R.id.username_text_view);
            mCountryTextView = itemView.findViewById(R.id.nav_view);
            mPlaceTextView = itemView.findViewById(R.id.recycler_view);

        }
    }
}
