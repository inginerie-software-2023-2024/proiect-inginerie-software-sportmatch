package com.example.sportmatch;

import static com.example.sportmatch.FCMSend.pushNotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//to do
//afisarea cu username
//pune ora la final
//sa se alinieze la final cand e mesajul meu

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference messagedb;
    MessageAdapter messageAdapter;
    User user;
    List<Message> messages;
    RecyclerView recyclerViewMessage;
    EditText editTextMessage;
    ImageButton buttonSendMessage;
    String chatId;
    String CHANNEL_ID = "MYCHANNEL";
    TextView textViewChatName;
    FirebaseMessaging firebaseMessaging;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();

        createNotificationChannel();

        recyclerViewMessage = findViewById(R.id.recyclerView);
        recyclerViewMessage.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter = new MessageAdapter(this, messages, messagedb);
        recyclerViewMessage.setAdapter(messageAdapter);
        firebaseMessaging = FirebaseMessaging.getInstance();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "My Channel"; // Name of the notification channel
            String channelDescription = "My Channel Description"; // Description of the notification channel
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String channelID = CHANNEL_ID + "_" + chatId;
            NotificationChannel channel = new NotificationChannel(channelID, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void init() {
        Log.d("init", "init");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = new User();
        recyclerViewMessage = findViewById(R.id.recyclerView);
        messagedb = database.getReference("Messages");
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSendMessage = findViewById(R.id.sendButton);
        buttonSendMessage.setOnClickListener(this);
        messages = new ArrayList<>();
        Log.d("init", "init");
        String eventId = getIntent().getStringExtra("eventId");
        if (eventId == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                // Retrieve values using key names
                eventId = extras.getString("eventId");

                // Now you can use the retrieved values as needed
                // For example, log the eventId
                Log.d("ChatActivity", "Event ID: " + eventId);
            }

        }

        Log.d("init", "init");
        DatabaseReference eventRef = database.getReference("Events").child(eventId);
        chatId = eventId;
        textViewChatName = findViewById(R.id.eventTitleInChat);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Event event = snapshot.getValue(Event.class);
                textViewChatName.setText(event.getEventName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(editTextMessage.getText().toString())) {
            Toast.makeText(getApplicationContext(), "You can't send an empty message", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!hasNotificationPermission()) {
            Toast.makeText(getApplicationContext(), "Please grant notification permission to send messages", Toast.LENGTH_SHORT).show();
            return;
        }

        String key = database.getReference("Messages").push().getKey();
        Message message = new Message(chatId, editTextMessage.getText().toString(), user.getFullName(), key);
        editTextMessage.setText("");
        messagedb.child(key).setValue(message);
        sendNotificationToChatMembers(chatId, textViewChatName.getText().toString(), message);
        // Send notification to other users in the chat
    }


    private void sendNotificationToChatMembers(String chatId, String eventTitle,Message message){
        DatabaseReference chatMembersRef = database.getReference("Events").child(chatId).child("participants");

        chatMembersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                    String memberId = memberSnapshot.getValue(String.class);

                    if (!memberId.equals(currentUser.getUid())) {
                        // Retrieve the user's device token
                        DatabaseReference userRef = database.getReference("Users").child(memberId);
                        Log.d("ChatActivity", "User id: " + memberId);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user = snapshot.getValue(User.class);
                                String deviceToken = user.getDeviceToken();
                                Log.d("ChatActivity", "Device token: " + deviceToken);

                                if (deviceToken != null) {
                                    // Get the Intent for opening ChatActivity
                                    Intent chatIntent = openChatActivityIntent(chatId);

                                    // Send push notification using FCM
                                    pushNotification(getApplicationContext(), deviceToken, eventTitle, message.getSender() + ": " + '\n' + message.getMessage(), chatIntent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("ChatActivity", "Failed to retrieve user information: " + error.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ChatActivity", "Failed to retrieve chat participants: " + error.getMessage());
            }
        });
    }
    private Intent openChatActivityIntent(String chatId) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("eventId", chatId); // Pass the chat ID to the ChatActivity
        intent.setAction("OPEN_CHAT");
        return intent;
    }


    private boolean hasNotificationPermission() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        return notificationManager.areNotificationsEnabled();
    }

    private void sendPushNotification(String deviceToken, String messageText, String channelID) {
        // Create the notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("New Message")
                .setContentText(messageText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Generate a unique notification ID
        int notificationId = (int) System.currentTimeMillis();

        try {
            // Show the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, builder.build());
        } catch (SecurityException e) {
            // Handle the SecurityException
            Log.e("ChatActivity", "Failed to show notification: " + e.getMessage());
        }
    }





    private void subscribeToChatTopic() {
        // Create a topic based on the chat ID
        String chatTopic = "chat_" + chatId;

        // Subscribe to the chat topic
        firebaseMessaging.subscribeToTopic(chatTopic)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("ChatActivity", "Subscribed to chat topic: " + chatTopic);
                    } else {
                        Log.e("ChatActivity", "Failed to subscribe to chat topic: " + chatTopic);
                    }
                });
    }

    @Override
    protected void onStart() {
        Log.d("on" ,"start");
        super.onStart();
        Log.d("on" ,"aftersuperstart");
        this.currentUser = mAuth.getCurrentUser();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        user.setUsername(currentUser.getDisplayName());

        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                user.setUsername(currentUser.getDisplayName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagedb = database.getReference("Messages");
        Query x = messagedb.orderByChild("chatId");
        Query query = messagedb.orderByChild("chatId").equalTo(chatId);
        Log.d("on" ,"messagedb");

        query.addChildEventListener (new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @NonNull String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                message.setChatId(chatId);
                message.setSender(snapshot.child("sender").getValue().toString());
                message.setCreatedAt(snapshot.child("createdAt").getValue().toString());
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
        subscribeToChatTopic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();

    }
    private void displayMessages(List<Message> messages){

        messageAdapter = new MessageAdapter(ChatActivity.this, messages, messagedb);
        recyclerViewMessage.setAdapter(messageAdapter);
        messageAdapter.notifyDataSetChanged();
        Log.d("display" ,"display");

        // scroll the recycler view to the bottom
        recyclerViewMessage.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

}