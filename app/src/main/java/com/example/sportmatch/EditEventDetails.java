package com.example.sportmatch;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
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

public class EditEventDetails extends AppCompatActivity {

    public static final int REQUEST_CODE_MAPS_ACTIVITY = 1001;
    TextView title;
    TextInputLayout editEventName;
    TextInputEditText editEventNameEdt;
    TextInputLayout editEventSport;
    AutoCompleteTextView autocomplete_sport;
    TextInputLayout editEventPlayers;
    AutoCompleteTextView autocomplete_players;
    TextInputLayout editEventLoc;
    AutoCompleteTextView autocomplete_loc;
    TextInputLayout editEventDate;
    TextInputEditText editEventDateEdt;
    TextInputLayout editEventTime;
    TextInputEditText editEventTimeEdt;
    TextInputLayout editEventDesc;
    TextInputEditText editEventDescEdt;
    Button buttonEEvent;
    ImageView mapImage;
    List<String> locations;
    ScrollView scrollViewE;
    Event mEvent;
    DatabaseReference reference;

    String valTitle, valueSport, valuePlayers, valueLoc, valueDate, valueTime, valueDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editevent);

        reference = FirebaseDatabase.getInstance().getReference("Events");

        title = findViewById(R.id.editTitle);
        editEventName = findViewById(R.id.editEventName);
        editEventNameEdt = findViewById(R.id.editEventNameEdt);
        editEventSport = findViewById(R.id.editEventSport);
        autocomplete_sport = findViewById(R.id.autocomplete_sport);
        editEventPlayers = findViewById(R.id.editEventPlayers);
        autocomplete_players = findViewById(R.id.autocomplete_players);
        editEventLoc = findViewById(R.id.editEventLoc);
        autocomplete_loc = findViewById(R.id.autocomplete_loc);
        editEventDate = findViewById(R.id.editEventDate);
        editEventDateEdt = findViewById(R.id.editEventDateEdt);
        editEventTime = findViewById(R.id.editEventTime);
        editEventTimeEdt = findViewById(R.id.editEventTimeEdt);
        editEventDesc = findViewById(R.id.editEventDesc);
        editEventDescEdt = findViewById(R.id.editEventDescEdt);
        buttonEEvent = findViewById(R.id.buttonEEvent);
        mapImage = findViewById(R.id.mapImageEdt);
        scrollViewE = findViewById(R.id.scrollViewE);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference sportsRef = database.getReference("Sports");
        DatabaseReference locRef = database.getReference("SportLocations");
        List<Sport> allSports = new ArrayList<>();
        List<String> sports = new ArrayList<>();
        List<SportLocation> allLocations = new ArrayList<>();
        locations = new ArrayList<>();
        List<Integer> players = new ArrayList<>();


        valTitle = getIntent().getStringExtra("valName");
        editEventNameEdt.setText(valTitle);

        valueSport = getIntent().getStringExtra("valSport");
        autocomplete_sport.setText(valueSport, false);
        ArrayAdapter<String> adapterSports = new ArrayAdapter<String>(this, R.layout.list_sport, sports);
        autocomplete_sport.setAdapter(adapterSports);

        valuePlayers = getIntent().getStringExtra("valPlayers");
        autocomplete_players.setText(valuePlayers, false);
        ArrayAdapter<Integer> adapterPlayers = new ArrayAdapter<Integer>(this, R.layout.list_player, players);
        autocomplete_players.setAdapter(adapterPlayers);

        valueLoc = getIntent().getStringExtra("valLoc");
        autocomplete_loc.setText(valueLoc, false);
        ArrayAdapter<String> adapterLoc = new ArrayAdapter<String>(this, R.layout.list_sportfields, locations);
        autocomplete_loc.setAdapter(adapterLoc);

        valueDate = getIntent().getStringExtra("valDate");
        editEventDateEdt.setText(valueDate);

        valueTime = getIntent().getStringExtra("valTime");
        editEventTimeEdt.setText(valueTime);

        valueDesc = getIntent().getStringExtra("valDesc");
        editEventDescEdt.setText(valueDesc);

        String activity = getIntent().getStringExtra("activity");

        if(activity.equals("EventDetailsAdminActivity")){
            mEvent = (Event) getIntent().getSerializableExtra("eventul");
            if(mEvent == null) {
                Log.d(TAG, "onCreate la details: event is null");
            } else {
                Log.d(TAG, "onCreate la details: event is not null");
            }
        }

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

                    if(valueSport!=null){
                        //adaug lista locatii
                        for(SportLocation sploc: allLocations){
                            if(sploc.getSport().getSportName().equals(valueSport)){
                                locations.add(sploc.getLocationName());
                            }
                        }
                        adapterLoc.notifyDataSetChanged();

                        //adaug lista jucatori
                        Sport sp = null;
                        for(Sport sport: allSports){
                            if(Objects.equals(sport.getSportName(), valueSport)){
                                sp = sport;
                                break;
                            }
                        }

                        if(sp!=null){
                            int mxP = sp.getMaxParticipants();
                            int index = sp.getMinParticipants();
                            if(Objects.equals(valueSport, "Bowling")){
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
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        autocomplete_sport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                editEventLoc.setError(null);
                editEventPlayers.setError(null);
                autocomplete_loc.setText("", false);
                autocomplete_players.setText("",false);

                // Obține sportul selectat
                String SelectedSport = (String) parent.getItemAtPosition(position);

                //actualizez lista locatii
                locations.clear();
                for(SportLocation sploc: allLocations){
                    if(sploc.getSport().getSportName().equals(SelectedSport)){
                        locations.add(sploc.getLocationName());
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


            }
        });


        autocomplete_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(autocomplete_sport.getText().toString().trim())){
                    editEventLoc.setError(getString(R.string.errorCEsportFirst));
                }
                else{
                    editEventLoc.setError(null);
                }
            }
        });

        autocomplete_players.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(autocomplete_sport.getText().toString().trim())){
                    editEventPlayers.setError(getString(R.string.errorCEsportFirst));
                }
                else{
                    editEventPlayers.setError(null);
                }
            }
        });


        //DATEPICKER

        Calendar calendar = Calendar.getInstance();
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date").setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        editEventDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePicker.show(getSupportFragmentManager(), "Material_Date_Picker");

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String dateString = formatter.format(selection);
                        editEventDateEdt.setText(dateString);
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

        editEventTimeEdt.setOnClickListener(new View.OnClickListener() {
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
                        editEventTimeEdt.setText(H+":"+M);
                    }
                });


            }
        });

        buttonEEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputTitle = editEventNameEdt.getText().toString().trim();
                String selectedSport = autocomplete_sport.getText().toString().trim();
                String selectedPlayers = autocomplete_players.getText().toString().trim();
                String selectedLoc = autocomplete_loc.getText().toString().trim();
                String selectedDate = editEventDateEdt.getText().toString().trim();
                String selectedTime = editEventTimeEdt.getText().toString().trim();
                String inputDesc = editEventDescEdt.getText().toString().trim();

                int error = 0;
                TextInputLayout firstErrorField = null;

                if (TextUtils.isEmpty(selectedSport)) {
                    editEventSport.setError(getString(R.string.errorCEsport));
                    error = 1;
                    firstErrorField = editEventSport;
                } else {
                    editEventSport.setError(null);
                }


                if (TextUtils.isEmpty(selectedPlayers)) {
                    editEventPlayers.setError(getString(R.string.errorCEsport));
                    error = 1;
                    if (firstErrorField == null) {
                        firstErrorField = editEventPlayers; // Actualizează referința către primul câmp cu eroare
                    }
                } else {
                    editEventPlayers.setError(null);
                }


                if (TextUtils.isEmpty(selectedLoc)) {
                    editEventLoc.setError(getString(R.string.errorCEloc));
                    error = 1;
                    if (firstErrorField == null) {
                        firstErrorField = editEventLoc; // Actualizează referința către primul câmp cu eroare
                    }
                } else {
                    editEventLoc.setError(null);
                }

                if(error == 0){



                    if(Objects.equals(activity, "EventPreview")){
                        Intent intent = new Intent(EditEventDetails.this, EventPreview.class);
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


                        startActivity(intent);
                    }
                    else if(Objects.equals(activity, "EventDetailsAdminActivity")){

                        updateTitle(inputTitle);
                        updateSport(selectedSport);
                        updatePlayers(selectedPlayers);
                        updateLoc(selectedLoc);
                        updateDate(selectedDate);
                        updateTime(selectedTime);
                        updateDesc(inputDesc);


                        Intent intent = new Intent(EditEventDetails.this, EventDetailsAdminActivity.class);
                        intent.putExtra("valTitle",inputTitle);
                        intent.putExtra("valSport",selectedSport);
                        intent.putExtra("valPlayers",selectedPlayers);
                        intent.putExtra("valLoc",selectedLoc);
                        intent.putExtra("valDate",selectedDate);
                        intent.putExtra("valTime",selectedTime);
                        intent.putExtra("valDesc",inputDesc);
                        intent.putExtra("eventul", mEvent);



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


                        startActivity(intent);
                    }

                }
                else if(firstErrorField != null){
                    // Redirecționează utilizatorul la primul câmp cu eroare
                    firstErrorField.requestFocus();

                    final View finalFirstErrorField = firstErrorField;
                    scrollViewE.post(new Runnable() {
                        @Override
                        public void run() {
                            int scrollToY = finalFirstErrorField.getTop() - 100; // Ajustează valoarea 100 pentru a obține poziția dorită
                            scrollViewE.scrollTo(0, scrollToY);
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

                    Intent intent2 = new Intent(EditEventDetails.this, MapsActivity.class);
                    intent2.putExtra("selectedLoc", SelectedLoc);
                    intent2.putExtra("selectedSport", SelectedSport);
                    intent2.putExtra("Activity", "EditEventDetails");
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

    private void updateTitle(String inputTitle){
        if(!valTitle.equals(inputTitle))
        {
            reference.child(mEvent.getKey()).child("eventName").setValue(inputTitle);

        }
        }
    private void updateSport(String selectedSport){
        if(!valueSport.equals(selectedSport))
        {
            reference.child(mEvent.getKey()).child("sport").setValue(selectedSport);

        }
        }

    private void updatePlayers(String selectedPlayers){
        if(!valuePlayers.equals(selectedPlayers))
        {
            reference.child(mEvent.getKey()).child("nrPlayers").setValue(selectedPlayers);
        }
    }

    private void updateLoc(String selectedLoc){
        if(!valueLoc.equals(selectedLoc))
        {
            reference.child(mEvent.getKey()).child("location").setValue(selectedLoc);
        }
        }

    private void updateDate(String selectedDate){
        if(!valueDate.equals(selectedDate) && !selectedDate.isEmpty())
        {
            reference.child(mEvent.getKey()).child("date").setValue(selectedDate);
        }}

    private void updateTime(String selectedTime){
        if(!valueTime.equals(selectedTime) && !selectedTime.isEmpty())
        {
            reference.child(mEvent.getKey()).child("time").setValue(selectedTime);
        }}

    private void updateDesc(String inputDesc){
        if(!valueDesc.equals(inputDesc) && !inputDesc.isEmpty())
        {
            reference.child(mEvent.getKey()).child("description").setValue(inputDesc);
        }}

}
