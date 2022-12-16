package com.example.exploro.models;

import android.app.Activity;
import android.util.Log;

import com.example.exploro.models.schemas.Trips;
import com.example.exploro.models.schemas.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserModel {
    private DatabaseReference db;
    private Activity activity;


    public interface ResultStatus {
        void resultLoaded(List<User> users);
    }

    public UserModel(Activity activity){
        db = FirebaseDatabase.getInstance().getReference();
        //addUser("Dennis", "test", "dennis@gmail.com","898979");
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


    public int createNewUser(String name, String username, String email, String password) {


        // TODO: Write new user to database

        return 0;
    }

    /**
     * Check for username in database
     *
     * @param username - Username to look for
     * @return found users in ResultStatus
     */
    public void findUsernameInUsers(String username, final ResultStatus result) {
        db.child("users").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            List<User> users = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    return;
                }
                Log.d("firebase", String.valueOf(task.getResult().child("name").getValue()));
                users.add(task.getResult().getValue(User.class));
                result.resultLoaded(users);
                return;
            }
        });
    }

    /**
     * Check for email in database
     *
     * @param email - email to look for
     * @return found users in ResultStatus
     */
    public void findEmailInUsers(String email, final ResultStatus result) {
        db.child("users").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            List<User> users = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    return;
                }
                Log.d("firebase", String.valueOf(task.getResult().child("email").getValue()));
                users.add(task.getResult().getValue(User.class));
                result.resultLoaded(users);
                return;
            }
        });
    }


}
