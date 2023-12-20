package com.example.sportmatch;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParticipantActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParticipantAdapter adapter;
    private List<User> userList;
    private List<String> userList2string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");

        TextView titlu = findViewById(R.id.txttite);
        titlu.setText("List of Players");



        // Initialize the close button
        View closeButton = findViewById(R.id.btn_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the activity
                finish();
            }
        });

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //facem rost de user list, o trimitem la adapter
        Intent intent = getIntent();
        Event currentEvent = (Event) intent.getSerializableExtra("eventul actual");
        List<String> userListstring = currentEvent.getParticipants();
        Log.e("userliststring", userListstring.toString());
        if(userListstring!=null) {
            userList = new ArrayList<>();
            getUsersFromIds((List<String>) userListstring, new OnUsersLoadedListener() {
                @Override
                public void onUsersLoaded(List<User> users) {
                    // Handle the loaded user list
                    for(User user: users){
                        userList.add(user);

                    }
                    // Create and set the adapter
                    adapter = new ParticipantAdapter(users, currentEvent, (List<String>) userListstring);
                    recyclerView.setAdapter(adapter);
                }
            });
        }
        else {
            finish();
        }


    }

    interface OnUsersLoadedListener {
        void onUsersLoaded(List<User> users);
    }

    // Callback interface for retrieving a single user
    interface OnUserLoadedListener {
        void onUserLoaded(User user);
    }

    private void getUsersFromIds(List<String> userIds, final OnUsersLoadedListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();

                for (String userId : userIds) {
                    DataSnapshot userSnapshot = dataSnapshot.child(userId);
                    if (userSnapshot.exists()) {
                        User user = userSnapshot.getValue(User.class);
                        if (user != null) {
                            userList.add(user);
                        }
                    }
                }

                listener.onUsersLoaded(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error
                // Notifying the listener with an empty user list
                listener.onUsersLoaded(new ArrayList<>());
            }
        });
    }}
