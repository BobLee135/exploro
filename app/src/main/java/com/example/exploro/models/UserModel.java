package com.example.exploro.models;

import android.app.Activity;

import com.example.exploro.models.schemas.Trips;
import com.example.exploro.models.schemas.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class UserModel {
    private DatabaseReference db;
    private Activity activity;

    public UserModel(){
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void addUser(String name, String password, String email, String phoneNumber){
        String encodePassword = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
        User user = new User(name, encodePassword, email, phoneNumber);
        user.experience = 0;
        db.child("users").child(name).setValue(user);
    }

    public void addFriend(String name, String password, String email, String phoneNumber){
        String encodePassword = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
        User user = new User(name, encodePassword, email, phoneNumber);
        user.experience = 0;
        db.child("users").child(name).setValue(user);
    }

    public void addTrip(String user, String city, String country, String place){
        Trips trips = new Trips(city, country, place);
        db.child("users").child(user).child("trips").push().setValue(trips);
    }
}
