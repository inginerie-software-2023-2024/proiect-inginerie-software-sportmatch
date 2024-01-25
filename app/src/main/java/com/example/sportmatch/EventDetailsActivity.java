package com.example.sportmatch;

import static android.content.ContentValues.TAG;

import static com.example.sportmatch.FCMSend.pushNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Map;

public class EventDetailsActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MAPS_ACTIVITY = 1001;
    private static final String CHANNEL_ID = "event_request_channel";
    private static final String CHANNEL_NAME = "Event Request Channel";
    private static final String CHANNEL_DESC = "Event Request Channel";

    TextView title;
    TextView admin;
    TextView adminInput;
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
    ImageView backhomeF;
    private void sendNotificationToAdmin(String adminId, String eventTitle) {
        // Create a notification channel (required for Android 8.0 and above)
        createNotificationChannel();

        // Build the notification

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null && notificationManager.isNotificationPolicyAccessGranted()) {

            DatabaseReference adminRef = database.getReference("Users").child(adminId).child("deviceToken");
            adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String adminNotificationToken = dataSnapshot.getValue(String.class);


                        // Send the notification to the admin's device using the obtained token
                        if (adminNotificationToken!=null)
                            pushNotification( getApplicationContext(),adminNotificationToken, eventTitle, "New participation request");


                    } else {
                        Log.d(TAG, "Admin's device token not found in the database");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle the error
                    Log.d(TAG, "Error retrieving admin's device token from the database: " + databaseError.getMessage());
                }
            });
        } else {
            Log.d(TAG, "Cannot send notification to admin: notification policy access not granted");
        }
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_feed);
        database = FirebaseDatabase.getInstance();


        title = findViewById(R.id.title);
        admin = findViewById(R.id.detailsAdminF);
        adminInput = findViewById(R.id.detailsAdminInput);
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
        backhomeF = findViewById(R.id.backhomeF);

        String valTitle = getIntent().getStringExtra("valTitle");
        detailsTitle.setText(valTitle);

        String valAdmin = getIntent().getStringExtra("valAdmin");
        adminInput.setText(valAdmin);

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

        String adminId = mEvent.getCreator();
        DatabaseReference adminRef = database.getReference("Users").child(adminId).child("username");
        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String adminUsername = dataSnapshot.getValue(String.class);
                    if (adminUsername != null) {
                        adminInput.setText(adminUsername);
                    }
                } else {
                    Log.d(TAG, "Admin's username not found in the database");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
                Log.d(TAG, "Error retrieving admin's username from the database: " + databaseError.getMessage());
            }
        });


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
        if (userId!=null && mEvent.getRequests() != null && mEvent.getRequests().contains(userId)) {
            isRequestSent = true;
        } else {
            isRequestSent = false;
        }

        if (mEvent.getParticipants() != null && mEvent.getParticipants().contains(userId)) {
            isParticipant = true;
        } else {
            isParticipant = false;
        }

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
                            Object value = dataSnapshot.getValue();
                            if (value instanceof List) {
                                // If the value is a List, cast it and assign to requestsList
                                requestsList.addAll((List<String>) value);
                            } else if (value instanceof Map) {
                                // If the value is a Map, iterate over its values and add to requestsList
                                for (Object item : ((Map<?, ?>) value).values()) {
                                    if (item instanceof String) {
                                        requestsList.add((String) item);
                                    }
                                }
                            }
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
                            sendNotificationToAdmin(mEvent.getCreator(), mEvent.getEventName());

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

        backhomeF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailsActivity.this, BottomNavActivity.class));
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

