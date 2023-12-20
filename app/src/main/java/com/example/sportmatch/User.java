package com.example.sportmatch;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String birthDate;
    private String fullName;
    private String bio;
    private String deviceToken;

    public User(){

    }
    // Constructor with bio parameter
    public User(String username, String password, String birthDate, String fullName, String bio) {
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        this.fullName = fullName;
        this.bio = bio;
    }

    // Constructor without bio parameter
    public User(String username, String password, String birthDate, String fullName) {
        this(username, password, birthDate, fullName, "Hello! I am using SportMatch!");
    }
    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public static void adaugbio(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    user.setBio("Hello! I am using SportMatch!");
                    userSnapshot.getRef().setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error reading Users data", databaseError.toException());
            }
        });

    }

    public static void getUserById(String userId, ValueEventListener valueEventListener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.child(userId).addListenerForSingleValueEvent(valueEventListener);
    }

}