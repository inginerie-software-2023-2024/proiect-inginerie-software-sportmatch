package com.example.sportmatch;

import java.util.ArrayList;

public class AllCategory {
    String title;

    ArrayList<Event> eventList;

    public AllCategory(String title, ArrayList<Event> eventList) {
        this.title = title;
        this.eventList = eventList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Event> getEventList() {
        return eventList ;
    }

    public void setEventList(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }


}
