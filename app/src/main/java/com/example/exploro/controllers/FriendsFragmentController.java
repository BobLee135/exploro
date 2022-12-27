package com.example.exploro.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exploro.R;
import com.example.exploro.models.LeaderboardAdapter;
import com.example.exploro.models.UserModel;
import com.example.exploro.models.schemas.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FriendsFragmentController extends Fragment {

    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;

    public FriendsFragmentController () {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.friends_fragment, container, false);

        // Views
        recyclerView = view.findViewById(R.id.friendList);
        Intent intent = this.getActivity().getIntent();
        String username = intent.getStringExtra("USER_username");
        UserModel userModel = new UserModel(getActivity());
        userModel.getFriendsUserObjects(username, new UserModel.ResultStatus(){
            @Override
            public void resultLoaded(List<User> users) {
                if (users.isEmpty()){
                    getActivity().findViewById(R.id.noFriendsText).setVisibility(View.VISIBLE);
                } else {
                    adapter = new LeaderboardAdapter(users);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }
        });

        return view;
    }
}
