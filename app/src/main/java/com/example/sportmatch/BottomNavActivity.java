package com.example.sportmatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class BottomNavActivity extends AppCompatActivity {

    ///recyclerview
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    ParentAdapterBottom parentAdapter;
    ArrayList<AllCategory> allCategoryList;
    ArrayList<Event> expiredEvents;
    ArrayList<Event> volleyballList;
    ArrayList<Event> footballList;
    ArrayList<Event> handballList;
    ArrayList<Event> tennisList;
    ArrayList<Event> badmintonList;
    ArrayList<Event> pingpongList;
    ArrayList<Event> basketballList;
    ArrayList<Event> bowlingList;
    String[] item={"2 players","less than 4","less than 6","All"};
    String[] cronologically={"Most recent","Least recent"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String>  adapterItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);


        AlertDialog.Builder builder=new AlertDialog.Builder(BottomNavActivity.this);
        builder.setCancelable(false);
//        builder.setView(R.layout.progress_layout)??????
        AlertDialog dialog=builder.create();
        dialog.show();


        ///start of retrieve data from firebase
        allCategoryList =new ArrayList<>();
        volleyballList =new ArrayList<>();
        footballList =new ArrayList<>();
        tennisList =new ArrayList<>();
        handballList =new ArrayList<>();
        basketballList =new ArrayList<>();
        pingpongList =new ArrayList<>();
        bowlingList = new ArrayList<>();
        badmintonList=new ArrayList<>();
        expiredEvents=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("Events");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnapshot:snapshot.getChildren())
                {
                    Event event = itemSnapshot.getValue(Event.class);

                    if(event.getParticipants() != null && !event.getParticipants().contains(userId) && !userId.equals(event.getCreator())) {///checking if the event is finished
                        if (event.getDate().contains("/") && event.getTime().contains(":"))
                        {
                            Calendar currentCalendar = Calendar.getInstance();
                        Date currentDate = currentCalendar.getTime();

                        // Convert event date string to Date object
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date eventDate;
                        try {
                            eventDate = dateFormat.parse(event.getDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return;
                        }

                        // Convert event time string to Date object
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        Date eventTime;
                        try {
                            eventTime = timeFormat.parse(event.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return;
                        }

                        // Combine event date and time
                        Calendar eventCalendar = Calendar.getInstance();
                        eventCalendar.setTime(eventDate);

                        Calendar eventTimeCalendar = Calendar.getInstance();
                        eventTimeCalendar.setTime(eventTime);

                        eventCalendar.set(Calendar.HOUR_OF_DAY, eventTimeCalendar.get(Calendar.HOUR_OF_DAY));
                        eventCalendar.set(Calendar.MINUTE, eventTimeCalendar.get(Calendar.MINUTE));
                        eventCalendar.set(Calendar.SECOND, 0);

                        if (eventCalendar.getTime().before(currentDate)) {
                            expiredEvents.add(event);
                        } else switch (event.getSport()) {
                            case "Volleyball":
                                volleyballList.add(event);
                                break;
                            case "Football":
                                footballList.add(event);
                                break;
                            case "Handball":
                                handballList.add(event);
                                break;
                            case "Tennis":
                                tennisList.add(event);
                                break;
                            case "Badminton":
                                badmintonList.add(event);
                                break;
                            case "Ping-Pong":
                                pingpongList.add(event);
                                break;
                            case "Basketball":
                                basketballList.add(event);
                                break;
                            case "Bowling":
                                bowlingList.add(event);
                                break;
                        }
                    }
                        else{
                            switch (event.getSport()) {
                                case "Volleyball":
                                    volleyballList.add(event);
                                    break;
                                case "Football":
                                    footballList.add(event);
                                    break;
                                case "Handball":
                                    handballList.add(event);
                                    break;
                                case "Tennis":
                                    tennisList.add(event);
                                    break;
                                case "Badminton":
                                    badmintonList.add(event);
                                    break;
                                case "Ping-Pong":
                                    pingpongList.add(event);
                                    break;
                                case "Basketball":
                                    basketballList.add(event);
                                    break;
                                case "Bowling":
                                    bowlingList.add(event);
                                    break;
                            }
                        }
                    }
                }
                if(!pingpongList.isEmpty()) allCategoryList.add(new AllCategory("Ping Pong Events",pingpongList));
                if(!volleyballList.isEmpty())allCategoryList.add(new AllCategory("Volleyball Events",volleyballList));
                if(!basketballList.isEmpty())allCategoryList.add(new AllCategory("Basketball Events",basketballList));
                if(!bowlingList.isEmpty())allCategoryList.add(new AllCategory("Bowling Events",bowlingList));
                if(!handballList.isEmpty())allCategoryList.add(new AllCategory("Handball Events",handballList));
                if(!footballList.isEmpty())allCategoryList.add(new AllCategory("Football Events",footballList));
                if(!badmintonList.isEmpty())allCategoryList.add(new AllCategory("Badminton Events",badmintonList));
                if(!tennisList.isEmpty())allCategoryList.add(new AllCategory("Tennis Events",tennisList));
                setParentRecycler(allCategoryList);
                parentAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        ///end of retrieve data from firebase


        ////inceput meniu

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
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




        ///beginning filter
        autoCompleteTextView =findViewById(R.id.auto_complete_txt);
        adapterItems= new ArrayAdapter<String>(this,R.layout.list_item,item);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(BottomNavActivity.this,"Filter: " + item,Toast.LENGTH_SHORT).show();

                ArrayList<AllCategory> filteredList = new ArrayList<>();
                if (item.isEmpty() || item.equals("All")) {
                    filteredList.addAll(allCategoryList);
                } else {
                    for (AllCategory category : allCategoryList) {
                        ArrayList<Event> filteredEvents = new ArrayList<>();
                        for (Event event : category.getEventList()) {
                            if (item.equals("2 players") && event.getNrPlayers().contains("2")) {
                                filteredEvents.add(event);
                            }
                            else if (item.equals("less than 4") && (event.getNrPlayers().contains("1") || event.getNrPlayers().contains("2") ||
                                    event.getNrPlayers().contains("3")||event.getNrPlayers().contains("4"))) {
                                filteredEvents.add(event);
                            }
                            else if (item.equals("less than 6") && (!event.getNrPlayers().contains("7")  && !event.getNrPlayers().contains("8"))) {
                                filteredEvents.add(event);
                            }
                        }
                        if (!filteredEvents.isEmpty()) {
                            filteredList.add(new AllCategory(category.getTitle(), filteredEvents));
                        }
                    }
                }
                setParentRecycler(filteredList);
            }
        });

        ///end filter

        ///begin filter by date
        autoCompleteTextView =findViewById(R.id.auto_complete_txt2);
        adapterItems= new ArrayAdapter<String>(this,R.layout.date_item,cronologically);

        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(BottomNavActivity.this, item,Toast.LENGTH_SHORT).show();

                ArrayList<AllCategory> orderedList = new ArrayList<>();
                for (AllCategory category : allCategoryList)
                {
                    List<Event> orderedEvents = new ArrayList<>();

                    if (item.equals("Most recent"))
                    {
                        orderedEvents=category.getEventList().stream().filter(e->!e.getDate().equals("To be discussed")).collect(Collectors.toList());
                        Collections.sort(orderedEvents, new Comparator<Event>() {
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                            @Override
                            public int compare(Event event1, Event event2) {
                                try {
                                    return dateFormat.parse(event1.getDate()).compareTo(dateFormat.parse(event2.getDate()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });
                        orderedEvents.addAll(category.getEventList().stream().filter(e->e.getDate().equals("To be discussed")).collect(Collectors.toList()));


                    } else {
                        orderedEvents=category.getEventList().stream().filter(e->!e.getDate().equals("To be discussed")).collect(Collectors.toList());
                        Collections.sort(orderedEvents, new Comparator<Event>() {
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            @Override
                            public int compare(Event event1, Event event2) {
                                try {
                                    return dateFormat.parse(event2.getDate()).compareTo(dateFormat.parse(event1.getDate()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    return 0;
                                }
                            }
                        });
                        orderedEvents.addAll(category.getEventList().stream().filter(e->e.getDate().equals("To be discussed")).collect(Collectors.toList()));
                    }
                    ArrayList<Event> ev=new ArrayList<>();
                    ev.addAll(orderedEvents);

                    orderedList.add(new AllCategory(category.getTitle(), ev));
                }
                setParentRecycler(orderedList);
            }
        });

        ///end filter

    }


    ///recyclerview
    private void setParentRecycler(ArrayList<AllCategory> allCategoryList)
    {
        recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        parentAdapter = new ParentAdapterBottom(allCategoryList,this);
        recyclerView.setAdapter(parentAdapter);
    }

    ///end recyclerview


}