package com.example.sportmatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminEventsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;


    ParentAdapter parentAdapter;
    ArrayList<AllCategory> allCategoryList;
    ArrayList<Event> volleyballList;
    ArrayList<Event> footballList;
    ArrayList<Event> handballList;
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
        setContentView(R.layout.activity_admin_events);


        AlertDialog.Builder builder=new AlertDialog.Builder(AdminEventsActivity.this);
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

        databaseReference= FirebaseDatabase.getInstance().getReference("Events");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot itemSnapshot:snapshot.getChildren())
                {
                    Event event = itemSnapshot.getValue(Event.class);
                    if (userId.equals(event.getCreator()))
                    {
                        switch(event.getSport()) {
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
        bottomNavigationView.setSelectedItemId(R.id.bottom_admin_events);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), BottomNavActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_admin_events:
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



    ///recyclerview
    private void setParentRecycler(ArrayList<AllCategory> allCategoryList)
    {
        recyclerView = findViewById(R.id.main_recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        parentAdapter = new ParentAdapter(allCategoryList,this);
        parentAdapter.setOnChatClickListener(AdminEventsActivity.this::onChatClick);
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