package com.example.sportmatch;

public class Sport
{
    private String sportName;
    private static int id=0;
    private int sportId;
    private int maxParticipants;
    private int minParticipants;

    public Sport(){

    }

    public Sport(String sportName, int maxParticipants, int minParticipants) {
        id++;
        this.sportId=id;
        this.sportName = sportName;
        this.maxParticipants = maxParticipants;
        this.minParticipants = minParticipants;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }
}
