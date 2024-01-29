package com.example.sportmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class EventPreview extends AppCompatActivity {
    public static final int REQUEST_CODE_MAPS_ACTIVITY = 1001;
    Button previewBtnEdit;
    ImageView sportImage;
    TextView previewTitle;
    TextView previewSport;
    TextView previewSportInput;
    TextView previewPlayers;
    TextView previewPlayersInput;
    TextView previewLoc;
    TextView previewLocInput;
    Button previewBtnMap;
    TextView previewDate;
    TextView previewDateInput;
    TextView previewTime;
    TextView previewTimeInput;
    TextView previewDesc;
    TextView previewDescInput;
    Button previewBtnAddEv;

    ArrayList<AllCategory> allCategoryList;

    private FirebaseDatabase database;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventpreview);

        database = FirebaseDatabase.getInstance();

        previewBtnEdit = findViewById(R.id.previewBtnEdit);
        sportImage = findViewById(R.id.sportImage);
        previewTitle = findViewById(R.id.previewTitle);
        previewSport = findViewById(R.id.previewSport);
        previewSportInput = findViewById(R.id.previewSportInput);
        previewPlayers = findViewById(R.id.previewPlayers);
        previewPlayersInput = findViewById(R.id.previewPlayersInput);
        previewLoc = findViewById(R.id.previewLoc);
        previewLocInput = findViewById(R.id.previewLocInput);
        previewBtnMap = findViewById(R.id.previewBtnMap);
        previewDate = findViewById(R.id.previewDate);
        previewDateInput = findViewById(R.id.previewDateInput);
        previewTime = findViewById(R.id.previewTime);
        previewTimeInput = findViewById(R.id.previewTimeInput);
        previewDesc = findViewById(R.id.previewDesc);
        previewDescInput = findViewById(R.id.previewDescInput);
        previewBtnAddEv = findViewById(R.id.previewBtnAddEv);


        String valTitle = getIntent().getStringExtra("valTitle");
        previewTitle.setText(valTitle);

        String valueSport = getIntent().getStringExtra("valSport");
        previewSportInput.setText(valueSport);

        String valuePlayers = getIntent().getStringExtra("valPlayers");
        previewPlayersInput.setText(valuePlayers);

        String valueLoc = getIntent().getStringExtra("valLoc");
        previewLocInput.setText(valueLoc);

        String valueDate = getIntent().getStringExtra("valDate");
        previewDateInput.setText(valueDate);

        String valueTime = getIntent().getStringExtra("valTime");
        previewTimeInput.setText(valueTime);

        String valueDesc = getIntent().getStringExtra("valDesc");
        previewDescInput.setText(valueDesc);



        switch (valueSport) {
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


        previewBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventPreview.this, EditEventDetails.class);

                intent.putExtra("valName",valTitle);
                intent.putExtra("valSport",valueSport);
                intent.putExtra("valPlayers",valuePlayers);
                intent.putExtra("valLoc",valueLoc);
                intent.putExtra("valDate",valueDate);
                intent.putExtra("valTime",valueTime);
                intent.putExtra("valDesc",valueDesc);
                intent.putExtra("activity","EventPreview");

                startActivity(intent);
            }

        });

        previewBtnAddEv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creează o cheie unică pentru noul eveniment
                String eventId = database.getReference("Events").push().getKey();

                // Creează un obiect Event cu datele evenimentului
                Random random = new Random();
                String chatId = String.format("%04d", random.nextInt(10000));
                ArrayList registeredPlayers = new ArrayList<>();
                registeredPlayers.add(FirebaseAuth.getInstance().getCurrentUser().getUid());

                String uid;
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                     uid = currentUser.getUid();
                } else {
                    uid="07LqiiinqgcO6AkA6bVYkeob34s1";
                }
                Event event = new Event(valTitle, valueSport, valuePlayers, valueLoc, valueDate, valueTime, valueDate,uid, registeredPlayers);
                event.setUid(eventId);

                // Adaugă evenimentul la tabelul "events" folosind cheia unică generată
                database.getReference("Events").child(eventId).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Event added successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), BottomNavActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to add event", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), CreateEventActivity.class));
                            finish();
                        }
                    }
                });
            }
        });



        ////inceput meniu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_create_event);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_create_event:
                    return true;
                case R.id.bottom_admin_events:
                    startActivity(new Intent(getApplicationContext(), AdminEventsActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_view_profile:
                    startActivity(new Intent(getApplicationContext(), ViewProfileActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_events_participates:
                    startActivity(new Intent(getApplicationContext(), OnlyParticipatesEvents.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_home:///bottom_home corespunde clasei BottomNavActivity
                    startActivity(new Intent(getApplicationContext(), BottomNavActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        ////final meniu

        //MAP
        previewBtnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(EventPreview.this, MapsActivity.class);
                intent2.putExtra("selectedLoc", valueLoc);
                intent2.putExtra("selectedSport", valueSport);
                intent2.putExtra("Activity", "EventPreview");
                startActivityForResult(intent2, REQUEST_CODE_MAPS_ACTIVITY);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void setAuth(FirebaseAuth auth) {
        this.mAuth = auth;
    }
    public void setDatabase(FirebaseDatabase database) {
        this.database = database;
    }

}
