package com.example.sportmatch;

import static com.example.sportmatch.FCMSend.pushNotification;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.PeriodicWorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class EventCheckWorker extends Worker {

    public EventCheckWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Code to check for events and perform necessary actions
        checkForExpiredEvents();
        return Result.success();
    }
    private void checkForExpiredEvents() {

        // Retrieve the user ID of the currently logged-in user
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("userIdfromperiodiccheck", userId);

        // Get a reference to the user's device token in the database
        DatabaseReference deviceTokenRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("deviceToken");

        // Attach a listener to retrieve the device token
        deviceTokenRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the data exists
                if (dataSnapshot.exists()) {
                    // Retrieve the device token
                    String deviceToken = dataSnapshot.getValue(String.class);
                    Log.d("deviceToken", deviceToken);

                    // Now that you have the device token, you can proceed with querying events
                    queryEvents(userId, deviceToken);
                } else {
                    Log.d("deviceToken", "Device token not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error getting device token: " + databaseError.getMessage());
            }
        });
    }
    private void queryEvents(String userId, String deviceToken) {
        // Query Firebase to get the events for the logged-in user
        long repeatInterval = Math.max(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS * 15, PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS);

        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events");
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    Event event = itemSnapshot.getValue(Event.class);

                    // Check if the event is relevant to the logged-in user
                    if (event.getParticipants() != null && event.getParticipants().contains(userId) ) {
                        if (event.getDate().contains("/") && event.getTime().contains(":")) {

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            Date eventDate;
                            try {
                                eventDate = dateFormat.parse(event.getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return;
                            }

                            // Convert event time string to Date object
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            Date eventTime;
                            try {
                                eventTime = timeFormat.parse(event.getTime());
                            } catch (ParseException e) {
                                e.printStackTrace();
                                return;
                            }

                            // Combine event date and time
                            Calendar eventCalendar = Calendar.getInstance();
                            eventCalendar.setTime(eventDate);

                            Calendar eventTimeCalendar = Calendar.getInstance();
                            eventTimeCalendar.setTime(eventTime);

                            eventCalendar.set(Calendar.HOUR_OF_DAY, eventTimeCalendar.get(Calendar.HOUR_OF_DAY));
                            eventCalendar.set(Calendar.MINUTE, eventTimeCalendar.get(Calendar.MINUTE));
                            eventCalendar.set(Calendar.SECOND, 0);

                            Calendar currentCalendar = Calendar.getInstance();
                            Date currentDate = currentCalendar.getTime();
                            Log.d("nexpevent", event.toString());
                            if (eventCalendar.getTime().before(currentDate))
                                //if the difference between the current date and the event date is less than repeat interval
                                if (currentDate.getTime() - eventCalendar.getTime().getTime() < repeatInterval)
                                {
                                    //print the event
                                    Log.d("expevent", event.toString());
                                    Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
                                    mapIntent.putExtra("selectedLoc", event.getLocation());
                                    mapIntent.putExtra("selectedSport", event.getSport());
                                    mapIntent.putExtra("Activity", "EventDetailsParticipant");
                                    mapIntent.setAction("OPEN_REVIEW");
                                    pushNotification(getApplicationContext(), deviceToken, event.getEventName(), "Please review the location of the event", mapIntent);

                                }
                                else
                                {
                                    Log.d("expiratmaimultderepeatinterval", event.toString());
                                }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
                Log.d("Error", "Error while reading the database");
            }
        });
    }
}

