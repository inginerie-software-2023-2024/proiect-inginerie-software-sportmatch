package com.example.sportmatch;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DBManagement {
    private FirebaseDatabase database;
    private DatabaseReference sportRef;
    private DatabaseReference locationRef;

    public DBManagement(){
        database = FirebaseDatabase.getInstance();
        sportRef = database.getReference("Sports");
        locationRef = database.getReference("SportLocations");
    }

    public void addSport(Sport sport){
        DatabaseReference newSportRef = sportRef.child(String.valueOf(sport.getSportName()));
        newSportRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    newSportRef.setValue(sport);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addLocation(SportLocation location) {
        DatabaseReference newLocationRef = locationRef.child(String.valueOf(location.getLocationId()));
        newLocationRef.setValue(location);
    }

    public void removeSport(Sport sport) {
        DatabaseReference sportToRemoveRef = sportRef.child(String.valueOf(sport.getSportId()));
        sportToRemoveRef.removeValue();
    }

    public void removeLocation(SportLocation location) {
        DatabaseReference locationToRemoveRef = locationRef.child(String.valueOf(location.getLocationId()));
        locationToRemoveRef.removeValue();
    }

}
