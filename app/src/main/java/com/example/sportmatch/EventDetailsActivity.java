package com.example.sportmatch;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MAPS_ACTIVITY = 1001;
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
    TextView detailsDescInput;
    Button detailsBtnParticipate;
    private FirebaseDatabase database;
    Event mEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_feed);
        database = FirebaseDatabase.getInstance();



        title = findViewById(R.id.title);
        sportImage = findViewById(R.id.sportImage);
        detailsTitle = findViewById(R.id.detailsTitle);
        detailsSport = findViewById(R.id.detailsSport);
        detailsSportInput = findViewById(R.id.detailsSportInput);
        detailsPlayers = findViewById(R.id.detailsPlayers);
        detailsPlayersInput = findViewById(R.id.detailsPlayersInput);
        detailsLoc = findViewById(R.id.detailsLoc);
        detailsLocInput = findViewById(R.id.detailsLocInput);
        detailsBtnMap = findViewById(R.id.detailsBtnMap);
        detailsDate = findViewById(R.id.detailsDate);
        detailsDateInput = findViewById(R.id.detailsDateInput);
        detailsTime = findViewById(R.id.detailsTime);
        detailsTimeInput = findViewById(R.id.detailsTimeInput);
        detailsDesc = findViewById(R.id.detailsDesc);
        detailsDescInput = findViewById(R.id.detailsDescInput);
        detailsBtnParticipate = findViewById(R.id.detailsBtnParticipate);

        String valTitle = getIntent().getStringExtra("valTitle");
        detailsTitle.setText(valTitle);

        String valSport = getIntent().getStringExtra("valSport");
        detailsSportInput.setText(valSport);

        String valPlayers = getIntent().getStringExtra("valPlayers");
        detailsPlayersInput.setText(valPlayers);

        String valLoc = getIntent().getStringExtra("valLoc");
        detailsLocInput.setText(valLoc);

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
        Intent intent = getIntent();
        mEvent = (Event) intent.getSerializableExtra("eventul");
        if(mEvent == null) {
            Log.d(TAG, "onCreate la details: event is null");
        } else {
            Log.d(TAG, "onCreate la details: event is not null");
        }
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        String userId = null;
        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            Log.e(TAG, "onCreate: No user is currently logged in");
        }
        // Check if the user is in the requests list or participants list
        boolean isRequestSent = false;
        boolean isParticipant = false;
        // Check if the user is in the requests list or participants list
        if (mEvent.getRequests() != null && mEvent.getRequests().contains(userId)) {
            isRequestSent = true;
        } else {
            isRequestSent = false;
        }

        if (mEvent.getParticipants() != null && mEvent.getParticipants().contains(userId)) {
            isParticipant = true;
        } else {
            isParticipant = false;
        }

// Show or hide the "Participate" button based on the conditions
//        if (isRequestSent || isParticipant) {
////            detailsBtnParticipate.setVisibility(View.GONE); // Hide the button
//        } else {
//            detailsBtnParticipate.setVisibility(View.VISIBLE); // Show the button
//        }


        detailsBtnParticipate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventDetailsActivity.this, "Your participation request was sent to admin!", Toast.LENGTH_SHORT).show();

                DatabaseReference requestsRef = database.getReference("Requests");
                String requestId = requestsRef.push().getKey();
                Request request = new Request(requestId, mEvent, FirebaseAuth.getInstance().getCurrentUser().getUid());
                mEvent.setRequests(new ArrayList<>());
                mEvent.addRequest(FirebaseAuth.getInstance().getCurrentUser().getUid());
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference eventsRef = database.getReference("Events");
                DatabaseReference eventRef = eventsRef.child(mEvent.getKey());
                DatabaseReference requestsReff = eventRef.child("requests");

                requestsReff.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> requestsList = new ArrayList<>();

                        if (dataSnapshot.exists()) {
                            // If "requests" field already exists, retrieve its current value
                            requestsList.addAll((List<String>) dataSnapshot.getValue());
                        }

                        // Add the new request to the list
                        requestsList.add(userId);
                        // Update the "requests" field in the database
                        requestsReff.setValue(requestsList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle the error
                        Log.d("Error", "Error while updating requests field");
                    }
                });



                if(requestId == null) {
                    Log.d(TAG, "onClick: requestId is null");
                } else
                    requestsRef.child(requestId).setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Request saved successfully

                            Toast.makeText(getApplicationContext(), "Request sent", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), BottomNavActivity.class));
                            Log.e("Firebase", "Request sent successfully");
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                            Toast.makeText(getApplicationContext(), "Failed to send request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            // You can also log the error for debugging purposes
                            Log.e("Firebase", "Error sending request", e);
                        }
                    });

            }
        });


        ////inceput meniu


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

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


        //MAP
        detailsBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(EventDetailsActivity.this, MapsActivity.class);
                intent2.putExtra("selectedLoc", valLoc);
                intent2.putExtra("selectedSport", valSport);
                intent2.putExtra("Activity", "EventDetailsFeed");
                startActivityForResult(intent2, REQUEST_CODE_MAPS_ACTIVITY);
            }
        });



    }

}
