package com.example.sportmatch;

public class SportLocation
{
    private String locationName;
    private static int id=0;
    private int locationId;
    private String streetName;
    private int number;
    private int sector;
    private Sport sport;

    private double longitude;
    private double latitude;

    public SportLocation(){

    }

    public SportLocation(String locationName, String streetName, int number, int sector, Sport sport,double latitude, double longitude)
    {
        this.locationName = locationName;
        id++;
        this.locationId =id;
        this.streetName = streetName;
        this.number = number;
        this.sector = sector;
        this.sport = sport;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        SportLocation.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSector() {
        return sector;
    }

    public void setSector(int sector) {
        this.sector = sector;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
