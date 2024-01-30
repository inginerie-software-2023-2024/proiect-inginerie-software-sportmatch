package com.example.sportmatch;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.Manifest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "MY_CHANNEL_ID";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("Notification", "Message received");
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();

            String pendingIntentDataString = String.valueOf(remoteMessage.getData());
            if (pendingIntentDataString != null) {
                Log.d("Notification", "Pending intent data: " + pendingIntentDataString);
            }
            else{
                Log.d("Notification", "Pending intent data is null");
            }
            try {
                Log.d("Notification", "Pending intent data: " + pendingIntentDataString);
                JSONObject pendingIntentData = new JSONObject(pendingIntentDataString);
                Log.d("Notification", "Pending intent data: " + pendingIntentData.toString());

                String targetActivityClassName = pendingIntentData.getString("target_activity");
                Log.d("Notification", "Target activity class name: " +  Class.forName(targetActivityClassName).toString());

                String action = pendingIntentData.getString("action");


                // Construct the PendingIntent based on the received data
                Intent intent = new Intent(this, Class.forName(targetActivityClassName));
                intent.setAction(action);
                // Pass any other necessary data to the intent
                Bundle extras = new Bundle();
                JSONObject extrasData = new JSONObject(pendingIntentData.getString("data"));
                Log.d("Notification", "Extras data: " + extrasData.toString());
                Iterator<String> keys = extrasData.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = extrasData.getString(key);
                    extras.putString(key, value);
                }
                intent.putExtras(extras);
                for (String key : intent.getExtras().keySet()) {
                    Log.d("Notification", "Extras key: " + key);
                    Log.d("Notification", "Extras value: " +  intent.getExtras().get(key));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Log.d("intent extras", "onMessageReceived: " + intent.toString());
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Log.d("Notification", "Pending intent: " + pendingIntent.toString());
                // Use the PendingIntent to launch the specified activity
                showNotification(title, message, pendingIntent);
            } catch (JSONException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean hasNotificationPermission() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        return notificationManager.areNotificationsEnabled();
    }

    private void showNotification(String title, String messageBody, PendingIntent pendingIntent) {
        // Create a notification channel (required for Android 8.0 and above)

        // Create the notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the content intent here
                .setAutoCancel(true);

        // Show the notification if permission is granted
        if (hasNotificationPermission()) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Request the necessary permission here
                return;
            }
            notificationManager.notify(0, builder.build());
            Log.d("Notification", "Notification sent");
        }
    }



    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "SportMatch";
            String channelDescription = "Notifications from SportMatch";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // Save the new token to your server or preferences
        Log.d("FCM Token", token);
    }
}

