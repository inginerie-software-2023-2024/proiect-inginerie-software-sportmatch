package com.example.sportmatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OnlyParticipatesEvents extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    ParentAdapter parentAdapter;
    ArrayList<AllCategory> allCategoryList;
    ArrayList<Event> volleyballList;
    ArrayList<Event> footballList;
    ArrayList<Event> handballList;
    ArrayList<Event> expiredEvents;
    ArrayList<Event> tennisList;
    ArrayList<Event> badmintonList;
    ArrayList<Event> pingpongList;
    ArrayList<Event> basketballList;
    ArrayList<Event> bowlingList;
    ///end recyclerview


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_participates_events);


        AlertDialog.Builder builder=new AlertDialog.Builder(OnlyParticipatesEvents.this);
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
                    if (event.getParticipants() != null && event.getParticipants().contains(userId) && !userId.equals(event.getCreator()))
                    {if (event.getDate().contains("/") && event.getTime().contains(":"))
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
                if(!expiredEvents.isEmpty())allCategoryList.add(new AllCategory("Finished Events",expiredEvents));

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



    ///recyclerview
    private void setParentRecycler(ArrayList<AllCategory> allCategoryList)
    {
        recyclerView = findViewById(R.id.main_recycler2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        parentAdapter = new ParentAdapter(allCategoryList,this);
        parentAdapter.setOnChatClickListener(OnlyParticipatesEvents.this::onChatClick);
        recyclerView.setAdapter(parentAdapter);
    }

    ///end recyclerview
    public void onChatClick(String eventId)
    {
        if (eventId != null) {
            Log.d("eventId", eventId);
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
            /////aici crapa

        } else {
            Log.e("eventId", "Event ID is null");
        }
    }
    private void openChatActivity(Event event) {
        // Implement the logic to launch the ChatActivity when the user clicks on a button
        // associated with an event card.
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("event_title", event.getEventName());
        startActivity(intent);
    }

}