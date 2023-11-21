package com.example.sportmatch;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBManagement2 {
    private FirebaseDatabase database;
    private static DatabaseReference requestRef;
    private static DatabaseReference participationRef;

    public DBManagement2(){
        database = FirebaseDatabase.getInstance();
        requestRef = database.getReference("Requests");
        participationRef = database.getReference("Participations");
    }

    public static void addRequest(Request request){
        if (request == null) {
            Log.e(TAG, "Request is null. Aborting.");
            return;
        }
        DatabaseReference newRequestRef = requestRef.push();

        newRequestRef.setValue(request, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    Log.d(TAG, "Request added successfully.");
                } else {
                    Log.e(TAG, "Error adding request: " + error.getMessage());
                }
            }
        });
    }


    public void addParticipation(SportLocation location) {
        DatabaseReference newLocationRef = participationRef.child(String.valueOf(location.getLocationId()));
        newLocationRef.setValue(location);
    }

    public void removeRequest(Sport sport) {
        DatabaseReference sportToRemoveRef = requestRef.child(String.valueOf(sport.getSportId()));
        sportToRemoveRef.removeValue();
    }

    public void removeParticipation(SportLocation location) {
        DatabaseReference locationToRemoveRef = participationRef.child(String.valueOf(location.getLocationId()));
        locationToRemoveRef.removeValue();
    }

}
