package com.example.sportmatch;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

}
