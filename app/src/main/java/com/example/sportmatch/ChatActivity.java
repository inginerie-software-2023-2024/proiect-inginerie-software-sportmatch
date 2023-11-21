package com.example.sportmatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference messagedb;
    MessageAdapter messageAdapter;
    User user;
    List <Message> messages;
    RecyclerView recyclerViewMessage;
    EditText editTextMessage;
    ImageButton buttonSendMessage;
    String chatId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();

        recyclerViewMessage = findViewById(R.id.recyclerView);
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(this, messages, messagedb);
        recyclerViewMessage.setAdapter(messageAdapter);
    }
    private void init(){
        Log.d("init" ,"init");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = new User();
        recyclerViewMessage = findViewById(R.id.recyclerView);
        messagedb = database.getReference("Messages");
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.sendButton);
        buttonSendMessage.setOnClickListener(this);
        messages = new ArrayList<>();
        Log.d("init" ,"init");
        String eventId = getIntent().getStringExtra("eventId");


        Log.d("init" ,"init");
        DatabaseReference eventRef = database.getReference("Events").child(eventId);

        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("chatId")) {
                    chatId = snapshot.child("chatId").getValue(String.class);
                    // do something with the chatId
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle error
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(!TextUtils.isEmpty(editTextMessage.getText().toString())){
            String key = database.getReference("Messages").push().getKey();
            Message message = new Message(chatId,editTextMessage.getText().toString(), user.getFullName(), key);
            editTextMessage.setText("");
            messagedb.push().setValue(message);
        }
        else {
            Toast.makeText(getApplicationContext(), "You can't send empty message", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        Log.d("on" ,"start");
        super.onStart();
        Log.d("on" ,"aftersuperstart");
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        user.setUsername(currentUser.getEmail());

        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                user.setUsername(currentUser.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagedb = database.getReference("Messages");
        Log.d("on" ,"messagedb");
        messagedb.addChildEventListener (new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @NonNull String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                messages.add(message);
                displayMessages(messages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @NonNull String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                List<Message> newMessages = new ArrayList<Message>();
                for (Message m : messages) {
                    if (m.getKey().equals(message.getKey())) {
                        newMessages.add(message);
                    } else {
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                List<Message> newMessages = new ArrayList<Message>();
                for (Message m : messages) {
                    if (!m.getKey().equals(message.getKey())) {
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e( "", "onCancelled: " + databaseError.getMessage());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @NonNull String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                List<Message> newMessages = new ArrayList<Message>();
                for (Message m : messages) {
                    if (m.getKey().equals(message.getKey())) {
                        newMessages.add(message);
                    } else {
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();

    }
    private void displayMessages(List<Message> messages){

            messageAdapter = new MessageAdapter(ChatActivity.this, messages, messagedb);
            recyclerViewMessage.setAdapter(messageAdapter);
            Log.d("display" ,"display");

            // scroll the recycler view to the bottom
            recyclerViewMessage.scrollToPosition(messageAdapter.getItemCount() - 1);
        }

}