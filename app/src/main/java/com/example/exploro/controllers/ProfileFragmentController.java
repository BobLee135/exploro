package com.example.exploro.controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.exploro.R;
import com.example.exploro.models.LeaderboardAdapter;
import com.example.exploro.models.ProfileAdapter;
import com.example.exploro.models.UserModel;
import com.example.exploro.models.schemas.Trips;
import com.example.exploro.models.schemas.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ProfileFragmentController extends Fragment {
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;

    public ProfileFragmentController() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        // Declare views
        recyclerView = view.findViewById(R.id.profileTripsList);
        TextView profileName = (TextView) view.findViewById(R.id.profileNameText);
        TextView profileLevel = (TextView) view.findViewById(R.id.profileLevelText);

        // Change name text to users name
        Intent currIntent = getActivity().getIntent();
        String name = currIntent.getStringExtra("USER_fullname");
        String username = currIntent.getStringExtra("USER_username");
        UserModel userModel = new UserModel(getActivity());
        userModel.getAllUserObjects(new UserModel.ResultStatus(){
            @Override
            public void resultLoaded(List<User> users) {

                for(User user : users){
                    if (user.username.equals(username)){
                        System.out.println(user.experience);
                        profileLevel.setText("Level " + (user.experience/10));
                    }
                }
            }
        });
        profileName.setText(name);

        // Get all trips
        userModel.getUserTrips(name,new UserModel.TripsResultStatus(){
            @Override
            public void tripsResultLoaded(List<Trips> trips) {
                adapter = new ProfileAdapter(trips);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }

        });

        return view;
    }
}