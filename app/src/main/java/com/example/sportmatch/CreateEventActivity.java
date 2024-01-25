package com.example.sportmatch;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class CreateEventActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_MAPS_ACTIVITY = 1001;
    TextView title;
    TextInputLayout newEventName;
    TextInputEditText newEventNameEdt;
    TextInputLayout newEventSport;
    AutoCompleteTextView autocomplete_sport;
    TextInputLayout newEventPlayers;
    AutoCompleteTextView autocomplete_players;
    TextInputLayout newEventLoc;
    AutoCompleteTextView autocomplete_loc;
    TextInputLayout newEventDate;
    TextInputEditText newEventDateEdt;
    TextInputLayout newEventTime;
    TextInputEditText newEventTimeEdt;
    TextInputLayout newEventDesc;
    TextInputEditText newEventDescEdt;
    Button buttonCEvent;
    ImageView mapImage;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ScrollView scrollView;
    List<String> locations;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newevent);



        //TODO: RATING locatii

        String userId;

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        title = findViewById(R.id.title);
        newEventName = findViewById(R.id.newEventName);
        newEventNameEdt = findViewById(R.id.newEventNameEdt);
        newEventSport = findViewById(R.id.newEventSport);
        autocomplete_sport = findViewById(R.id.autocomplete_sport);
        newEventPlayers = findViewById(R.id.newEventPlayers);
        autocomplete_players = findViewById(R.id.autocomplete_players);
        newEventLoc = findViewById(R.id.newEventLoc);
        autocomplete_loc = findViewById(R.id.autocomplete_loc);
        newEventDate = findViewById(R.id.newEventDate);
        newEventDateEdt = findViewById(R.id.newEventDateEdt);
        newEventTime = findViewById(R.id.newEventTime);
        newEventTimeEdt = findViewById(R.id.newEventTimeEdt);
        newEventDesc = findViewById(R.id.newEventDesc);
        newEventDescEdt = findViewById(R.id.newEventDescEdt);
        buttonCEvent = findViewById(R.id.buttonCEvent);
        mapImage = findViewById(R.id.mapImage);
        scrollView = findViewById(R.id.scrollView);


        //Referinte catre tabelele de sporturi si locatii din baza de date


        DatabaseReference sportsRef = database.getReference("Sports");

        DatabaseReference locRef = database.getReference("SportLocations");
        List<Sport> allSports = new ArrayList<>();
        List<String> sports = new ArrayList<>();
        List<SportLocation> allLocations = new ArrayList<>();
        locations = new ArrayList<>();
        List<Integer> players = new ArrayList<>();

        ArrayAdapter<String> adapterSports = new ArrayAdapter<String>(this, R.layout.list_sport, sports);
        autocomplete_sport.setAdapter(adapterSports);

        ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(this, R.layout.list_sportfields, locations);
        autocomplete_loc.setAdapter(adapterLoc);

        ArrayAdapter<Integer> adapterPlayers = new ArrayAdapter<Integer>(this, R.layout.list_player, players);
        autocomplete_players.setAdapter(adapterPlayers);

        sportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot sportSnapshot : snapshot.getChildren()) {
                    Sport sport = sportSnapshot.getValue(Sport.class);
                    allSports.add(sport);
                    sports.add(sport.getSportName());
                }

                adapterSports.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        locRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot locSnapshot : snapshot.getChildren()) {
                    SportLocation sportLocation = locSnapshot.getValue(SportLocation.class);
                    allLocations.add(sportLocation);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //Setare liste autocomplete
        autocomplete_sport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                newEventLoc.setError(null);
                newEventPlayers.setError(null);
                autocomplete_loc.setText("", false);
                autocomplete_players.setText("",false);

                // Obține sportul selectat
                String SelectedSport = (String) parent.getItemAtPosition(position);

                //actualizez lista locatii
                locations.clear();
                for(SportLocation sploc: allLocations){
                    if(sploc.getSport().getSportName().equals(SelectedSport)){

                        if (sploc.getReview() == 0)
                            locations.add(sploc.getLocationName() + " (-/5)");
                        else
                            locations.add(sploc.getLocationName() + " (" + sploc.getReview() + "/5)");
                    }
                }
                adapterLoc.notifyDataSetChanged();

                //actualizez lista jucatori
                players.clear();
                Sport sp = null;
                for(Sport sport: allSports){
                    if(Objects.equals(sport.getSportName(), SelectedSport)){
                        sp = sport;
                        break;
                    }
                }
                int mxP = sp.getMaxParticipants();
                int index = sp.getMinParticipants();
                if(Objects.equals(SelectedSport, "Bowling")){
                    while(index<=mxP){
                        players.add(index);
                        index += 1;
                    }
                }
                else{
                    while(index<=mxP){
                        players.add(index);
                        index += 2;
                    }
                }
                adapterPlayers.notifyDataSetChanged();

                mapImage.setImageResource(R.drawable.map_active);

            }
        });


        //TODO: Eroare cand apas sageata
        autocomplete_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(autocomplete_sport.getText().toString().trim())){
                    newEventLoc.setError(getString(R.string.errorCEsportFirst));
                }
                else{
                    newEventLoc.setError(null);
                }
            }
        });


        autocomplete_players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(autocomplete_sport.getText().toString().trim())){
                    newEventPlayers.setError(getString(R.string.errorCEsportFirst));
                }
                else{
                    newEventPlayers.setError(null);
                }
            }
        });


        //DATEPICKER

        Calendar calendar = Calendar.getInstance();
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .build();

        newEventDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePicker.show(getSupportFragmentManager(), "Material_Date_Picker");

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String dateString = formatter.format(selection);
                        newEventDateEdt.setText(dateString);
                    }
                });

            }
        });


        //TIMEPICKER

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTitleText("Select Time")
                .build();

        newEventTimeEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(getSupportFragmentManager(), "Material_Time_Picker" );

                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int Hour = timePicker.getHour();
                        int Minute = timePicker.getMinute();
                        String H = "";
                        String M = "";
                        if(Hour/10==0){
                            H = "0" + Hour;
                        }
                        else{
                            H = String.valueOf(Hour);
                        }
                        if(Minute/10==0){
                            M = "0" + Minute;
                        }
                        else{
                            M = String.valueOf(Minute);
                        }
                        newEventTimeEdt.setText(H+":"+M);
                    }
                });


            }
        });


        //Button for Create Event
        buttonCEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int error = 0;
                TextInputLayout firstErrorField = null; // Variabila pentru a stoca referința către primul câmp cu eroare(chat gpt)

                String inputTitle = newEventNameEdt.getText().toString().trim();
                if (TextUtils.isEmpty(inputTitle)) {
                    newEventName.setError(getString(R.string.errorCEname));
                    error = 1;
                    firstErrorField = newEventName; // Actualizează referința către primul câmp cu eroare
                } else {
                    newEventName.setError(null);
                }

                String selectedSport = autocomplete_sport.getText().toString().trim();
                if (TextUtils.isEmpty(selectedSport)) {
                    newEventSport.setError(getString(R.string.errorCEsport));
                    error = 1;
                    if (firstErrorField == null) {
                        firstErrorField = newEventSport; // Actualizează referința către primul câmp cu eroare
                    }
                } else {
                    newEventSport.setError(null);
                }

                String selectedPlayers = autocomplete_players.getText().toString().trim();
                if (TextUtils.isEmpty(selectedPlayers)) {
                    newEventPlayers.setError(getString(R.string.errorCEsport));
                    error = 1;
                    if (firstErrorField == null) {
                        firstErrorField = newEventPlayers; // Actualizează referința către primul câmp cu eroare
                    }
                } else {
                    newEventPlayers.setError(null);
                }

                String selectedLoc = autocomplete_loc.getText().toString().trim();
                if (TextUtils.isEmpty(selectedLoc)) {
                    newEventLoc.setError(getString(R.string.errorCEloc));
                    error = 1;
                    if (firstErrorField == null) {
                        firstErrorField = newEventLoc; // Actualizează referința către primul câmp cu eroare
                    }
                } else {
                    newEventLoc.setError(null);
                }

                String selectedDate = newEventDateEdt.getText().toString().trim();
                String selectedTime = newEventTimeEdt.getText().toString().trim();
                String inputDesc = newEventDescEdt.getText().toString().trim();

                if(error == 0){

                    Intent intent = new Intent(CreateEventActivity.this, EventPreview .class);

                    intent.putExtra("valTitle",inputTitle);
                    intent.putExtra("valSport",selectedSport);
                    intent.putExtra("valPlayers",selectedPlayers);
                    intent.putExtra("valLoc",selectedLoc);
                    intent.putExtra("valDate",selectedDate);
                    intent.putExtra("valTime",selectedTime);
                    intent.putExtra("valDesc",inputDesc);

                    if(TextUtils.isEmpty(selectedDate)){
                        intent.putExtra("valDate","To be discussed");
                    }
                    else{
                        intent.putExtra("valDate",selectedDate);
                    }

                    if(TextUtils.isEmpty(selectedTime)){
                        intent.putExtra("valTime","To be discussed");
                    }
                    else{
                        intent.putExtra("valTime",selectedTime);
                    }

                    if(TextUtils.isEmpty(inputDesc)){
                        intent.putExtra("valDesc","None");
                    }
                    else{
                        intent.putExtra("valDesc",inputDesc);
                    }

                    intent.putExtra("creatorId", userId);
                    startActivity(intent);
                }
                else if(firstErrorField != null){
                    // Redirecționează utilizatorul la primul câmp cu eroare
                    firstErrorField.requestFocus();

                    final View finalFirstErrorField = firstErrorField;
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            int scrollToY = finalFirstErrorField.getTop() - 100; // Ajustează valoarea 100 pentru a obține poziția dorită
                            scrollView.scrollTo(0, scrollToY);
                        }
                    });

                }
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
        mapImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(autocomplete_sport.getText().toString().trim())){
                    String SelectedSport = autocomplete_sport.getText().toString().trim();
                    String SelectedLoc;
                    if(TextUtils.isEmpty(autocomplete_loc.getText().toString().trim())){
                        SelectedLoc = locations.get(0).toString().trim();
                    }
                    else{
                        SelectedLoc = autocomplete_loc.getText().toString().trim();
                    }

                    Intent intent2 = new Intent(CreateEventActivity.this, MapsActivity.class);
                    intent2.putExtra("selectedLoc", SelectedLoc);
                    intent2.putExtra("selectedSport", SelectedSport);
                    intent2.putExtra("Activity", "CreateEvent");
                    startActivityForResult(intent2, REQUEST_CODE_MAPS_ACTIVITY);
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MAPS_ACTIVITY && resultCode == RESULT_OK) {
            String selectedLocation = data.getStringExtra("selectedLocation");
            autocomplete_loc.setText(selectedLocation);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_sportfields, locations);
            autocomplete_loc.setAdapter(adapter);
        }

    }

}
