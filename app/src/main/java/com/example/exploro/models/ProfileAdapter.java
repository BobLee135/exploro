package com.example.exploro.models;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.exploro.R;
import com.example.exploro.models.schemas.Trips;
import com.example.exploro.models.schemas.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private List<Trips> mTravelList;


    public ProfileAdapter(List<Trips> travelList) {
        mTravelList = travelList;
        for (Trips trips : mTravelList){
            System.out.println(trips.getCity());
            System.out.println(trips.getPlace());
            System.out.println(trips.getCountry());

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd");
        Trips trip = mTravelList.get(position);
        holder.mDateTextView.setText(trip.getDate());
        holder.mPlaceTextView.setText(trip.getPlace());

    }

    @Override
    public int getItemCount() {
        return mTravelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mDateTextView;
        TextView mPlaceTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            //ONLY TEMP IDS RIGHT NOW
            mDateTextView = itemView.findViewById(R.id.date_text_view);
            mPlaceTextView = itemView.findViewById(R.id.place_text_view);

        }
    }
}
