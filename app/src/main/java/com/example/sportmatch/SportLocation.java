package com.example.sportmatch;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

//todo:make so that one person can only give 1 review to a location
//todo: show the review in the location details

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

    private double review;
    private int nrReviews;
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
        this.review = 0;
        this.nrReviews = 0;
    }

    //set review

    public void addReview(double review) {
        retrieveReviewFromDatabase(review);
    }

    private void retrieveReviewFromDatabase(double review) {
        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("SportLocations").child(String.valueOf(locationId));

        // Retrieve "review" and "nrReviews" values from the database
        locationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve current values from the database
                    double currentReview = dataSnapshot.child("review").getValue(Double.class);
                    int currentNrReviews = dataSnapshot.child("nrReviews").getValue(Integer.class);

                    // Update the review and nrReviews based on the retrieved values
                    updateReviewLocally(currentReview, currentNrReviews, review);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void updateReviewLocally(double currentReview, int currentNrReviews, double review) {
        // Update the review and nrReviews based on the retrieved values from the database
        this.nrReviews = currentNrReviews + 1;
        this.review = (currentReview * (this.nrReviews-1) + review) / this.nrReviews;
        Log.e("review", "updated review: " + this.review);
        Log.e(locationId + "", "updated review: " + this.nrReviews);


        // Log the updated review after combining with the retrieved values
        Log.e("review", "updated review: " + this.review);
        Log.e(locationId + "", "updated review: " + this.review);

        // Update the review and nrReviews in the Firebase Realtime Database
        updateReviewInDatabase();
    }


    private void updateReviewInDatabase() {
        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("SportLocations").child(String.valueOf(locationId));

        // Update the review and nrReviews in the database
        locationRef.child("review").setValue(this.review);
        locationRef.child("nrReviews").setValue(this.nrReviews);
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

    public double getReview() {
        // Use DecimalFormat to format the double value with 2 digits after the decimal point
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedReview = decimalFormat.format(review);

        // Parse the formatted string back to double
        return Double.parseDouble(formattedReview);
    }


    public void setReview(double review) {
        this.review = review;
    }

    public int getNrReviews() {
        return nrReviews;
    }

    public void setNrReviews(int nrReviews) {
        this.nrReviews = nrReviews;
    }
}
