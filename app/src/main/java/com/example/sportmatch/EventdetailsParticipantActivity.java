package com.example.sportmatch;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class EventdetailsParticipantActivity extends AppCompatActivity {

    private static final int REQUEST_DIALOG_ACTIVITY = 1;
    public static final int REQUEST_CODE_MAPS_ACTIVITY = 1001;
    Event mEvent;
    TextView title;
    ImageView sportImage;
    TextView detailsTitle;
    TextView detailsSport;
    TextView detailsSportInput;
    TextView detailsPlayers;
    TextView detailsPlayersInput;
    TextView detailsLoc;
    TextView detailsLocInput;
    Button detailsBtnMap;
    TextView detailsDate;
    TextView detailsDateInput;
    TextView detailsTime;
    TextView detailsTimeInput;
    TextView detailsDesc;
    private FirebaseDatabase database;
    TextView detailsDescInput;
    ImageView buttonToChatP;
    ImageView backhomeP;

    SportLocation spLocglobal;

    Sport sportglobal;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //addAttributesToExistingSportLocations();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_participant);
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        mEvent = (Event) intent.getSerializableExtra("eventul");
        if(mEvent == null) {
            Log.d(TAG, "onCreate la details: event is null");
        } else {
            Log.d(TAG, "onCreate la details: event is not null");
        }


        title = findViewById(R.id.titleP);
        sportImage = findViewById(R.id.sportImageP);
        detailsTitle = findViewById(R.id.detailsTitleP);
        detailsSport = findViewById(R.id.detailsSportP);
        detailsSportInput = findViewById(R.id.detailsSportInputP);
        detailsPlayers = findViewById(R.id.detailsPlayersP);
        detailsPlayersInput = findViewById(R.id.detailsPlayersInputP);
        detailsLoc = findViewById(R.id.detailsLocP);
        detailsLocInput = findViewById(R.id.detailsLocInputP);
        detailsBtnMap = findViewById(R.id.detailsBtnMapP);
        detailsDate = findViewById(R.id.detailsDateP);
        detailsDateInput = findViewById(R.id.detailsDateInputP);
        detailsTime = findViewById(R.id.detailsTimeP);
        detailsTimeInput = findViewById(R.id.detailsTimeInputP);
        detailsDesc = findViewById(R.id.detailsDescP);
        detailsDescInput = findViewById(R.id.detailsDescInputP);
        backhomeP = findViewById(R.id.backhomeP);

        Button popupButton = findViewById(R.id.detailsParticipantsButtonP);
        /*if(mEvent.areRequestsEmpty()) {
            popupButton.setText("No requests pending");
            popupButton.setEnabled(false);
            popupButton.setClickable(false);
            popupButton.setBackgroundColor(getResources().getColor(R.color.grey));
        }*/
        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EventdetailsParticipantActivity.this, ParticipantActivity.class);
                intent.putExtra("eventul actual", mEvent);
                startActivity(intent);

            }
        });

        String valueTitle = getIntent().getStringExtra("valTitle");
        String valTitle = getIntent().getStringExtra("valTitle").toUpperCase();
        detailsTitle.setText(valueTitle);

        String valSport = getIntent().getStringExtra("valSport");
        detailsSportInput.setText(valSport);

        String valPlayers = getIntent().getStringExtra("valPlayers");
        detailsPlayersInput.setText(valPlayers);

        String valLoc = getIntent().getStringExtra("valLoc");
        getLocationByNameFromDatabase(valLoc, valSport);
        //Log.e("spLocglobal", spLocglobal.toString());
        //detailsLocInput.setText(valLoc + " (" + spLocglobal.getReview() + "/5)");
        //detailsLocInput.setText("bnm");

        String valDate = getIntent().getStringExtra("valDate");
        detailsDateInput.setText(valDate);

        String valTime = getIntent().getStringExtra("valTime");
        detailsTimeInput.setText(valTime);

        String valDesc = getIntent().getStringExtra("valDesc");
        detailsDescInput.setText(valDesc);

        switch (valSport) {
            case "Volleyball":
                sportImage.setImageResource(R.drawable.volleyball);
                break;
            case "Football":
                sportImage.setImageResource(R.drawable.football);
                break;
            case "Handball":
                sportImage.setImageResource(R.drawable.handball);
                break;
            case "Tennis":
                sportImage.setImageResource(R.drawable.tennis);
                break;
            case "Badminton":
                sportImage.setImageResource(R.drawable.badminton);
                break;
            case "Ping-Pong":
                sportImage.setImageResource(R.drawable.ping_pong);
                break;
            case "Basketball":
                sportImage.setImageResource(R.drawable.basketball);
                break;
            case "Bowling":
                sportImage.setImageResource(R.drawable.bowling);
                break;
        }

        detailsBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(EventdetailsParticipantActivity.this, MapsActivity.class);
                intent2.putExtra("selectedLoc", valLoc);
                intent2.putExtra("selectedSport", valSport);
                intent2.putExtra("Activity", "EventDetailsParticipant");
                startActivityForResult(intent2, REQUEST_CODE_MAPS_ACTIVITY);
            }
        });

        backhomeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventdetailsParticipantActivity.this, OnlyParticipatesEvents.class));
            }
        });

        ////inceput meniu

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_events_participates);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), BottomNavActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_admin_events:
                    startActivity(new Intent(getApplicationContext(), AdminEventsActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_create_event:
                    startActivity(new Intent(getApplicationContext(), CreateEventActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_events_participates:
                    startActivity(new Intent(getApplicationContext(), OnlyParticipatesEvents.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_view_profile:
                    startActivity(new Intent(getApplicationContext(), ViewProfileActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;

            }
            return false;
        });
        ///final meniu
    }


    private void getLocationByNameFromDatabase(String locationName, String sportName) {
        DatabaseReference locationsRef = FirebaseDatabase.getInstance().getReference().child("SportLocations");

        Query query = locationsRef.orderByChild("locationName").equalTo(locationName);

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                        SportLocation location = locationSnapshot.getValue(SportLocation.class);
                        if (location != null && location.getSport().getSportName().equals(sportName)) {
                            // Handle the retrieved SportLocation object
                            spLocglobal = location;
                            detailsLocInput.setText(location.getLocationName() + " (" + location.getReview() + "/5)");
                            System.out.println("Location Name: " + location.getLocationName());
                        } else {
                            // Handle the case when the SportLocation object is null
                            System.out.println("Location not found");
                        }
                    }
                } else {
                    // Handle the case when the dataSnapshot is empty
                    System.out.println("Location not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case
                System.out.println("Database error: " + databaseError.getMessage());
            }
        });
    }


    private void getSportByNameFromDatabase(String sportName) {
        DatabaseReference sportsRef = FirebaseDatabase.getInstance().getReference().child("Sports");

        sportsRef.orderByChild("sportName").equalTo(sportName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot sportSnapshot : dataSnapshot.getChildren()) {
                        Sport sport = sportSnapshot.getValue(Sport.class);
                        if (sport != null) {
                            // Handle the retrieved Sport object
                            sportglobal = sport;
                        } else {
                            // Handle the case when the Sport object is null
                            System.out.println("Sport not found");
                        }
                    }
                } else {
                    // Handle the case when the dataSnapshot is empty
                    System.out.println("Sport not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error case
                System.out.println("Database error: " + databaseError.getMessage());
            }
        });
    }


/*    private void addAttributesToExistingSportLocations() {
        DatabaseReference locationsRef = FirebaseDatabase.getInstance().getReference("SportLocations");

        locationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                        int locationId = Integer.parseInt(locationSnapshot.getKey());

                        // Check if "review" and "nrReviews" attributes exist
                        boolean reviewExists = locationSnapshot.child("review").exists();
                        boolean nrReviewsExists = locationSnapshot.child("nrReviews").exists();

                        // If attributes don't exist, set them to 0 or generate random if nrReviews is 0

                            // Check if nrReviews is 0 and set a random review
                            if (nrReviewsExists) {
                                long nrReviews = (long) locationSnapshot.child("nrReviews").getValue();
                                if (nrReviews == 0) {
                                    double randomReview = generateRandomRating();
                                    int randomNrReviews = generateRandomNrReviews();
                                    addReviewAndNrReviews(locationId, randomReview, randomNrReviews);
                                } else {
                                    addReviewAndNrReviews(locationId, 0, 0);
                                }
                            } else {
                                addReviewAndNrReviews(locationId, 0, 0);
                            }


                        if (!nrReviewsExists) {
                            addReviewAndNrReviews(locationId, 0, 0);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Empty implementation or handle errors if necessary
            }
        });


    }

    // Call this method from MainActivity to add "review" and "nrReviews" attributes
    private void addReviewAndNrReviews(int locationId, double review, int nrReviews) {
        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("SportLocations").child(String.valueOf(locationId));

        // Set default values if not provided
        if (review < 0) {
            review = 0;
        }

        if (nrReviews < 0) {
            nrReviews = 0;
        }

        // Update the "review" and "nrReviews" attributes in the database
        Map<String, Object> updates = new HashMap<>();
        updates.put("review", review);
        updates.put("nrReviews", nrReviews);

        locationRef.updateChildren(updates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("Firebase", "Data could not be saved: " + databaseError.getMessage());
                } else {
                    Log.d("Firebase", "Review and nrReviews added successfully to SportLocation " + locationId);
                }
            }
        });
    }

    public double generateRandomRating() {
        // Create an instance of the Random class
        Random random = new Random();

        // Generate a random double between 1 and 5
        double randomRating = 1 + (5 - 1) * random.nextDouble();

        // Round the randomRating to two decimal places
        randomRating = Math.round(randomRating * 100.0) / 100.0;

        return randomRating;
    }

    public int generateRandomNrReviews() {
        // Create an instance of the Random class
        Random random = new Random();

        // Generate a random number of reviews between 1 and 100
        return random.nextInt(100) + 1;
    }*/

    }
