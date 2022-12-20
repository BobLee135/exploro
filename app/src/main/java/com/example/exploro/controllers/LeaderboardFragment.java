package com.example.exploro.controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.exploro.R;
import com.example.exploro.models.LeaderboardAdapter;
import com.example.exploro.models.UserModel;
import com.example.exploro.models.schemas.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class LeaderboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;

    public LeaderboardFragment() {
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
        View view = inflater.inflate(R.layout.leaderboard_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        Intent intent = this.getActivity().getIntent();
        UserModel userModel = new UserModel(getActivity());
        String username = intent.getStringExtra("USER_username");
        SwitchCompat mySwitch = (SwitchCompat) view.findViewById(R.id.switch1);
        userModel.getAllUserObjects(new UserModel.ResultStatus(){
            @Override
            public void resultLoaded(List<User> users) {
                Collections.sort(users, new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                        return Integer.compare(user1.experience, user2.experience);
                    }
                });
                Collections.reverse(users);
                System.out.println(users.size());
                adapter = new LeaderboardAdapter(users);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getActivity().findViewById(R.id.nofriends).setVisibility(View.INVISIBLE);
                Intent intent = getActivity().getIntent();
                UserModel userModel = new UserModel(getActivity());
                String username = intent.getStringExtra("USER_username");
                if (!b){
                    userModel.getAllUserObjects(new UserModel.ResultStatus() {
                        @Override
                        public void resultLoaded(List<User> users) {

                            Collections.sort(users, new Comparator<User>() {
                                @Override
                                public int compare(User user1, User user2) {
                                    return Integer.compare(user1.experience, user2.experience);
                                }
                            });
                            Collections.reverse(users);
                            System.out.println(users.size());
                            adapter = new LeaderboardAdapter(users);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    });
                }else {
                    userModel.getFriendsUserObjects(username, new UserModel.ResultStatus() {
                        @Override
                        public void resultLoaded(List<User> users) {
                            if (users.isEmpty()){
                                getActivity().findViewById(R.id.nofriends).setVisibility(View.VISIBLE);
                            }
                            Collections.sort(users, new Comparator<User>() {
                                @Override
                                public int compare(User user1, User user2) {
                                    return Integer.compare(user1.experience, user2.experience);
                                }
                            });
                            Collections.reverse(users);
                            for (User user : users){
                                System.out.println(user.username);
                            }
                            adapter = new LeaderboardAdapter(users);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    });
                }
            }
        });
        return view;
    }
}