//package com.example.sportmatch;
//
//package com.example.sportmatch;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RequestActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private RequestAdapter adapter;
//    private List<User> userList;
//    private List<String> userList2string;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_requests);
//
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("Users");
//
//        // Initialize RecyclerView
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Create a list of users (replace this with your own data retrieval logic)
//        //userList = getUsers();
//
//        Intent intent = getIntent();
//        Event currentEvent = (Event) intent.getSerializableExtra("eventul actual");
//        List <String> userListstring = currentEvent.getParticipants();
//        Log.e("RequestActivity", "userList: " + userListstring);
//        userList = new ArrayList<>();
//        userList=getUsersFromIds(userListstring);
//
//        Log.e("RequestActivity", "userList: " + userList);
//        // Create and set the adapter
//        adapter = new RequestAdapter(userList);
//        recyclerView.setAdapter(adapter);
//    }
//
//    private List<User> getUsersFromIds(List<String> userIds) {
//        Log.e("RequestActivity", "userid: " + "am ajuns in getUserssssById");
//
//        List<User> userList = new ArrayList<>();
//
//        for (String userId : userIds) {
//            User user = getUserById(userId);
//        }
//        Log.d("RequestActivity", "userList: " + userList);
//        return userList;
//    }
//
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
//                        //userList.add(user);
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
//    }
//
//
//
//}

//s ar putea sa trebuiasca mai tarziu