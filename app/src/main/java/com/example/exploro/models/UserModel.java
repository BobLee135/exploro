package com.example.exploro.models;

import android.app.Activity;
import android.util.Log;

import com.example.exploro.models.schemas.Trips;
import com.example.exploro.models.schemas.User;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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


    /**
     * Authenticate user against database
     *
     * @param username - input username
     * @param password - input password
     * @param result - Return correctly authenticated users
     */
    public void authUser(String username, String password, final ResultStatus result) {
        db.child("users").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            List<User> users = new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                // If database error
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    result.resultLoaded(users);
                    return;
                }

                // If no user exist with that username then return
                if (task.getResult().getValue() == null) {
                    result.resultLoaded(users);
                    return;
                }

                // Authenticate password
                String aUsername = task.getResult().child("username").getValue().toString();
                String aPassword = task.getResult().child("password").getValue().toString();
                Base64.Decoder decoder = Base64.getDecoder();
                aPassword = new String(decoder.decode(aPassword));


                if (password.equals(aPassword))
                    users.add(task.getResult().getValue(User.class));
                result.resultLoaded(users);
            }
        });
    }

    /**
     * Create a new user in database
     *
     * @param name - full name of user
     * @param username - username
     * @param email - email
     * @param password - password
     * @return
     */

    public User createNewUser(String name, String username, String email, String password) {
        // Encode password
        String encodePassword = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));

        User user = new User(name, email, username, encodePassword, "8989898");
        user.experience = 0;
        // TODO: Write new user to database
        db.child("users").child(username).setValue(user);

        return user;
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
    public void getAllUserObjects(final ResultStatus result) {

        db.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<User> users = new ArrayList<>();
                    for (DataSnapshot children : snapshot.getChildren()) {
                        User user = children.getValue(User.class);
                        users.add(user);
                    }
                    result.resultLoaded(users);
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error getting data", error.toException());
                return;

            }
        });
    }
    public void getFriendsUserObjects(String username, final ResultStatus result) {

        db.child("users").child(username).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<User> users = new ArrayList<>();
                    for (DataSnapshot children : snapshot.getChildren()) {
                        User user = children.getValue(User.class);
                        users.add(user);
                    }
                    result.resultLoaded(users);
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error getting data", error.toException());
                return;

            }
        });
    }
    public void addNewFriend(String username, String friendUsername) {
        User friend = new User();
        friend.username = friendUsername;
        User user = new User();
        user.username = username;
        db.child("users").child(username).child("friends").child(friendUsername).setValue(friend);
        db.child("users").child(friendUsername).child("friends").child(username).setValue(user);

    }
    public void deleteFriend(String username, String friendUsername) {
        db.child("users").child(username).child("friends").child(friendUsername).removeValue();
        db.child("users").child(friendUsername).child("friends").child(username).removeValue();

    }
    public void changeEmail(String username, String newEmail) {
        db.child("users").child(username).child("email").setValue(newEmail);
    }
    public void changePassword(String username, String password) {
        password = Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
        db.child("users").child(username).child("password").setValue(password);
    }
    public void changeUsername(String username, String newUsername) {
        db.child("users").child(username).child("password").setValue(newUsername);
    }
    public void addUserExperience(String username, int experience) {
        final CountDownLatch latch = new CountDownLatch(1);
        db.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                user.experience += experience;
                db.child("users").child(user.username).setValue(user);
                latch.countDown();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Could not get thang");
                latch.countDown();

            }
        });
    }
    public void addTrip(String username, String city, String country, String place){
        Trips trips = new Trips(city, country, place);
        db.child("users").child(username).child("trips").push().setValue(trips);
    }


}
