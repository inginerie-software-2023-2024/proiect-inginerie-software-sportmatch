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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EventDetailsAdminActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MAPS_ACTIVITY = 1001;
    private static final int REQUEST_DIALOG_ACTIVITY = 1;
    Event mEvent;
    TextView title;
    Button detailsBtnEdit;
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
    ImageView buttonToChat1;
    ImageView backhomeA;

//    EventDetailsAdapter eventDetailsAdapter;
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_admin);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        mEvent = (Event) intent.getSerializableExtra("eventul");
        if(mEvent == null) {
            Log.d(TAG, "onCreate la details: event is null");
        } else {
            Log.d(TAG, "onCreate la details: event is not null");
        }


        title = findViewById(R.id.title);
        detailsBtnEdit = findViewById(R.id.detailsBtnEdit);
        sportImage = findViewById(R.id.sportImage);
        detailsTitle = findViewById(R.id.detailsTitle);
        detailsSport = findViewById(R.id.detailsSport);
        detailsSportInput = findViewById(R.id.detailsSportInput);
        detailsPlayers = findViewById(R.id.detailsPlayers);
        detailsPlayersInput = findViewById(R.id.detailsPlayersInput);
        detailsLoc = findViewById(R.id.detailsLoc);
        detailsLocInput = findViewById(R.id.detailsLocInput);
        detailsDate = findViewById(R.id.detailsDate);
        detailsDateInput = findViewById(R.id.detailsDateInput);
        detailsTime = findViewById(R.id.detailsTime);
        detailsTimeInput = findViewById(R.id.detailsTimeInput);
        detailsDesc = findViewById(R.id.detailsDesc);
        detailsDescInput = findViewById(R.id.detailsDescInput);
        detailsBtnMap = findViewById(R.id.detailsBtnMap);
        buttonToChat1 = findViewById(R.id.buttonToChat);
        backhomeA = findViewById(R.id.backhomeA);



        String valueTitle = getIntent().getStringExtra("valTitle");
        String valTitle = getIntent().getStringExtra("valTitle").toUpperCase();
        detailsTitle.setText(valueTitle);

        String valSport = getIntent().getStringExtra("valSport");
        detailsSportInput.setText(valSport);

        String valPlayers = getIntent().getStringExtra("valPlayers");
        detailsPlayersInput.setText(valPlayers);

        String valLoc = getIntent().getStringExtra("valLoc");
        getLocationByNameFromDatabase(valLoc, valSport);
        //detailsLocInput.setText(valLoc);

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

        Button popupButtonp = findViewById(R.id.detailsParticipantsButton);
        /*if(mEvent.areRequestsEmpty()) {
            popupButton.setText("No requests pending");
            popupButton.setEnabled(false);
            popupButton.setClickable(false);
            popupButton.setBackgroundColor(getResources().getColor(R.color.grey));
        }*/
        popupButtonp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EventDetailsAdminActivity.this, ParticipantActivity.class);
                intent.putExtra("eventul actual", mEvent);
                startActivity(intent);

            }
        });

        detailsBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailsAdminActivity.this, EditEventDetails.class);

                intent.putExtra("valName",valueTitle);
                intent.putExtra("valSport",valSport);
                intent.putExtra("valPlayers",valPlayers);
                intent.putExtra("valLoc",valLoc);
                intent.putExtra("valDate",valDate);
                intent.putExtra("valTime",valTime);
                intent.putExtra("valDesc",valDesc);
                intent.putExtra("activity","EventDetailsAdminActivity");
                intent.putExtra("eventul", mEvent);
                startActivity(intent);
            }
        });

        detailsBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(EventDetailsAdminActivity.this, MapsActivity.class);
                intent2.putExtra("selectedLoc", valLoc);
                intent2.putExtra("selectedSport", valSport);
                intent2.putExtra("Activity", "EventDetailsAdmin");
                startActivityForResult(intent2, REQUEST_CODE_MAPS_ACTIVITY);
            }
        });



        // Get reference to the "see requests" button
        Button popupButton = findViewById(R.id.see_requests_button);
        if(mEvent.areRequestsEmpty()) {
            popupButton.setText("No requests pending");
            popupButton.setEnabled(false);
            popupButton.setClickable(false);
            popupButton.setBackgroundColor(getResources().getColor(R.color.grey));
        }
        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EventDetailsAdminActivity.this, RequestActivity.class);
                intent.putExtra("eventul actual", mEvent);
                startActivity(intent);

            }
        });
/*        YourDialog dialog = new YourDialog(this, requestAdapter);
        dialog.setContentView(R.layout.activity_requests);

        //dialog.getWindow().setBackgroundDrawableResource();

        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        ImageView btnClose = dialog.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });*/

        backhomeA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailsAdminActivity.this, AdminEventsActivity.class));
            }
        });




        ////inceput meniu

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_admin_events);

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


}
