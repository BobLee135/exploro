package com.example.exploro.controllers;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exploro.MessageHelper;
import com.example.exploro.R;
import com.example.exploro.models.FriendsAdapter;
import com.example.exploro.models.LeaderboardAdapter;
import com.example.exploro.models.UserModel;
import com.example.exploro.models.schemas.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FriendsFragmentController extends Fragment {

    private RecyclerView recyclerView;
    private FriendsAdapter adapter;

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
        MessageHelper messageHelper = new MessageHelper();
        Intent intent = this.getActivity().getIntent();
        String username = intent.getStringExtra("USER_username");
        UserModel userModel = new UserModel(getActivity());
        userModel.getFriendsUserObjects(username, new UserModel.ResultStatus(){
            @Override
            public void resultLoaded(List<User> users) {
                if (users.isEmpty()){
                    getActivity().findViewById(R.id.noFriendsText).setVisibility(View.VISIBLE);
                } else {
                    adapter = new FriendsAdapter(users, username, userModel, view);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }
        });

        Button addFriendBtn = (Button)view.findViewById(R.id.add_friend);
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(v.getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.add_friend_dialog);
                    dialog.show();
                    EditText usernameInput = (EditText) dialog.findViewById(R.id.usernameInputField);
                    Button addFriendStepTwo = (Button) dialog.findViewById(R.id.add_friend_button);
                    addFriendStepTwo.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            String friendUsername = usernameInput.getText().toString();
                            if (friendUsername.equals(username)){
                                messageHelper.displaySnackbar("You can't add yourself", 2, "Error", usernameInput);
                            } else {
                                Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
                                Matcher matcher = pattern.matcher(friendUsername);
                                boolean checker = matcher.find();
                                if (!checker) {
                                    userModel.getAllUserObjects(new UserModel.ResultStatus() {
                                        @Override
                                        public void resultLoaded(List<User> users) {
                                            User userObject = null;
                                            User friendObject = null;
                                            for (User user : users) {
                                                //if we have found both users
                                                if (userObject != null && friendObject != null) {
                                                    break;
                                                }
                                                if (user.username.equals(friendUsername)) {
                                                    friendObject = user;
                                                }
                                                if (user.username.equals(username)) {
                                                    userObject = user;
                                                }
                                            }
                                            if (userObject == null || friendObject == null) {
                                                messageHelper.displaySnackbar("User does not exist", 2, "Error", usernameInput);
                                            } else {
                                                userModel.addNewFriend(userObject, friendObject);
                                                userModel.getFriendsUserObjects(username, new UserModel.ResultStatus() {
                                                    @Override
                                                    public void resultLoaded(List<User> users) {
                                                        if (users.isEmpty()) {
                                                            getActivity().findViewById(R.id.noFriendsText).setVisibility(View.VISIBLE);
                                                        } else {
                                                            adapter = new FriendsAdapter(users, username, userModel, view);
                                                            recyclerView.setAdapter(adapter);
                                                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                        }
                                                    }
                                                });
                                                getActivity().findViewById(R.id.noFriendsText).setVisibility(View.INVISIBLE);
                                                dialog.hide();
                                            }
                                        }
                                    });
                                } else {
                                    messageHelper.displaySnackbar("No special characters allowed", 2, "Error", usernameInput);

                                }
                            }
                        }
                    });

                }
            }

        );

        return view;
    }
}
