package com.example.sportmatch;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Event  implements Serializable {
    private String key;
    private String eventName;
    private String sport;
    private String nrPlayers;
    private String location;
    private String date;
    private String time;
    private String description;



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    private String creator;
    private List<String> participants; // list of user IDs who have registered for this event
    private List<String> requests; // list of user IDs who have requested to join this event
    private String chatId;

    public Event (){

    }
    String getChatId() {
        return chatId;
    }
    public Event(String eventName, String sport, String nrPlayers, String location, String date, String time, String description, String creator, List<String> participants) {
        this.key = null;
        this.eventName = eventName;
        this.sport = sport;
        this.nrPlayers = nrPlayers;
        this.location = location;
        this.date = date;
        this.time = time;
        this.description = description;
        this.creator = creator;
        this.participants = participants;
        this.requests = new ArrayList<>();
        // Add the event creator to the registered players list
    }


    public String getUid() {
        return key;
    }

    public void setUid(String uid) {
        this.key = uid;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getNrPlayers() {
        return nrPlayers;
    }

    public void setNrPlayers(String nrPlayers) {
        this.nrPlayers = nrPlayers;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getParticipants() {
        participants =removeDuplicates(participants);
        return participants;
    }


    public void addParticipant( String userId) {

        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events").child(key);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    if (event != null) {
                        List<String> participants = event.getParticipants();
                        if (participants != null && !participants.contains(userId)) {
                            participants.add(userId);  // Add the new participant

                            // Update the modified Event object in Firebase
                            event.setParticipants(participants);
                            eventRef.setValue(event);
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


    public void setParticipants(List<String> participants) {
        participants=removeDuplicates(participants);
        this.participants = participants;
    }

    public boolean myequals(Event obj) {
        if (obj == null ||
                !this.eventName.equals(obj.getEventName()) ||
                !this.key.equals(obj.getKey()) ||
                !this.sport.equals(obj.getSport()) ||
                this.nrPlayers != obj.getNrPlayers() ||
                !this.location.equals(obj.getLocation()) ||
                !this.date.equals(obj.getDate()) ||
                !this.time.equals(obj.getTime()) ||
                !this.description.equals(obj.getDescription()) ||
                !this.creator.equals(obj.getCreator()) ||
                !this.participants.equals(obj.getParticipants()) ||
                !this.chatId.equals(obj.getChatId())) {
            return false;
        }
        return true;
    }

    public List<String> getRequests() {
        requests= removeDuplicates(requests);
        return requests;
    }

    public void addRequest(String userId) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events").child(key);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    if (event != null) {
                        List<String> requests = event.getRequests();
                        if (requests != null && !requests.contains(userId)) {
                            requests.add(userId);  // Add the new participant

                            // Update the modified Event object in Firebase
                            event.setParticipants(requests);
                            eventRef.setValue(event);
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

    public boolean areRequestsEmpty() {
        if(requests == null)
            return true;
        return requests.isEmpty();
    }

    public void removeRequestFromEvent( String userId) {
        Log.e("setRequests", "setRequests: " + requests);

        final String eventId = this.getKey();
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events").child(key);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Event event = dataSnapshot.getValue(Event.class);
                    if (event != null) {
                        List<String> requests = event.getRequests();
                        if (requests != null) {
                            requests.remove(userId);  // Remove the specific request

                            // Update the modified Event object in Firebase
                            event.setRequests(requests);
                            eventRef.setValue(event);
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
        this.requests.remove(userId);
        Log.e("setRequests", "setRequests: " + requests);

    }


    public void setRequests(List<String> requests) {
        requests= removeDuplicates(requests);
        this.requests = requests;
    }

    public static <T> List<T> removeDuplicates(List<T> list) {
        if (list == null) {
            return null;
        }
        return new ArrayList<>(new HashSet<>(list));
    }

}
