package com.example.sportmatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private List<User> userList;
    private List<String> userList2string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");

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
        List <String> userListstring = currentEvent.getRequests();
        if(userListstring!=null) {
            Log.e("RequestActivity", "userList iduri: " + userListstring);
            userList = new ArrayList<>();
            getUsersFromIds(userListstring, new OnUsersLoadedListener() {
                @Override
                public void onUsersLoaded(List<User> users) {
                    // Handle the loaded user list
                    for(User user: users){
                        userList.add(user);
                        Log.d("RequestActivity", "userList in getbyidsssss: " + user);
                        Log.d("RequestActivity", "userList in getbyidsssss: " + user.getUsername());
                    }
                    // Create and set the adapter
                    adapter = new RequestAdapter(users, currentEvent, userListstring);
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

    // Example usage


/*

//    private List<User> getUsersFromIds(List<String> userIds) {
//        Log.e("RequestActivity", "userid: " + "am ajuns in getUserssssById");
//
//        List<User> userList = new ArrayList<>();
//
//        for (String userIdd : userIds) {
//
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference usersRef = database.getReference("Users");
//            DatabaseReference userRef = usersRef.child("username");
//            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // Retrieve the user information
//                    User user = dataSnapshot.getValue(User.class);
//                    if (user != null) {
//                        userList.add(user);
//                        Log.e("bla", "User id: " + user.getUsername());
//                        //Log.e("RequestActivity", "userList final: " + userList);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.e("RequestActivity", "getUserById onCancelled: " + databaseError.getMessage());
//                }
//
//                // Callback interface for notifying when users are loaded
//
//            });
//
//        }
//        Log.d("RequestActivity", "userList in getbyidsssss: " + userList);
//        return userList;
//    }


    public void getUserById(String userIdd) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        DatabaseReference userRef = usersRef.child("username");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve the user information
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    userList.add(user);
                    Log.e("bla", "User id: " + user.getUsername());
                    //Log.e("RequestActivity", "userList final: " + userList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("RequestActivity", "getUserById onCancelled: " + databaseError.getMessage());
            }
        });

    }


//    private User getUserById(String userIdd) {
//        // Implement your logic to retrieve the user object from the user ID
//        // Here's an example assuming you have a Firebase Realtime Database reference
//        Log.e("RequestActivity", "userid: " + "am ajuns in getUserById");
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("Users");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                    String userId = userSnapshot.getKey();
//                    User user = userSnapshot.getValue(User.class);
//                    if(userId.equals(userIdd) && user!=null){
//                        userList.add(user);
//                        Log.e("bla", "User id: " + userId);
//                        Log.e("bla", "User name: " + user.getUsername() + ", nume full " + user.getFullName());
//                    }
//                    // Add the user to your list or perform any other operations
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("req", "The read failed: " + databaseError.getCode());
//            }
//        });
//
//        // Return null as a placeholder, the actual user object will be retrieved asynchronously
//        return null;
    }





//s ar putea sa trebuiasca mai tarziu







package com.example.sportmatch;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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

public class RequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private List<User> userList;
    private List<String> userListstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a list of users (replace this with your own data retrieval logic)
        //userList = getUsers();

        Intent intent = getIntent();
        Event currentEvent = (Event) intent.getSerializableExtra("eventul actual");
        Log.e("RequestActivity", "currentEvent: " + currentEvent.getEventName());
        //useri care deja participa la eveniment
        //List <String> puserListstring = currentEvent.getParticipants();
        //idurile userilor care au facut request
        userListstring = new ArrayList<>();
        //aici e problema
        getRequestsbyEvent(currentEvent);
        Log.e("RequestActivity", "userList cu iduri: " + userListstring);
        userList = new ArrayList<>();
        if(userListstring!=null)
            userList=getUsersFromIds(userListstring);

        Log.e("RequestActivity", "userList de useri in sine: " + userList);
        // Create and set the adapter
        adapter = new RequestAdapter(userList);
        recyclerView.setAdapter(adapter);
    }

    private List<User> getUsersFromIds(List<String> userIds) {
        Log.e("RequestActivity", "userid: " + "am ajuns in getUserssssById");
        if(userIds==null){
            Log.e("RequestActivity", "userid: " + "userIds e null");
            return null;
        }
        for (String userId : userIds) {
            getUserById(userId);
        }
        Log.d("RequestActivity", "userList: " + userList);
        return userList;
    }

    private void getUserById(String userIdd) {
        // Implement your logic to retrieve the user object from the user ID
        // Here's an example assuming you have a Firebase Realtime Database reference
        Log.e("RequestActivity", "userid: " + "am ajuns in getUserById");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    User user = userSnapshot.getValue(User.class);
                    if(userId.equals(userIdd) && user!=null){
                        //userList.add(user);
                        Log.e("bla", "User id: " + userId);
                        Log.e("bla", "User name: " + user.getUsername() + ", nume full " + user.getFullName());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("req", "The read failed: " + databaseError.getCode());
            }
        });

    }


    private void getRequestsbyEvent(Event event) {
        Log.e("RequestActivity", "am ajuns in getRequestsbyEvent");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Requests");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("RequestActivity", "am ajuns in getRequestsbyEvent onDataChange");
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Log.e("blabla", "Matching Event: in for");

                    Event requestEvent = requestSnapshot.getValue(Event.class);
                    if (requestEvent != null ) {
                        // Match found, perform desired operations
                        Log.e("blabla", "Matching Event: " + requestEvent.getEventName());
                    }}}
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("req", "The read failed: " + databaseError.getCode());
            }
        });
    }

}
*/
