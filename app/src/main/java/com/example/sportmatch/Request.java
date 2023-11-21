package com.example.sportmatch;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class Request implements Serializable {
    private String uid;
    @PropertyName("event")
    Event event;
    @PropertyName("user")
    String user;

    public Request( String uid, Event event, String user) {
        this.uid = uid;
        this.event = event;
        this.user = user;
        //DBManagement2.addRequest(this);
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

}
