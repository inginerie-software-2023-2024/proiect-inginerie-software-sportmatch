package com.example.sportmatch;


import static android.content.ContentValues.TAG;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private static String base_url = "https://fcm.googleapis.com/fcm/send";
    private static String server_key ="key=AAAA1-nufZQ:APA91bEaBFalr9EqVKTgwGHr5r4pkERiVrERW05tPqWNn_9t2NSMNjFYqMh1gBFbpEGEswY9FODzbPRqm-Pf9PvmqHiAcOrWAH351wgXQ9S2J9FiPZhaaKM5Rd8eBsOo_yS72Rhw2p0b";

    public static void pushNotification(Context context, String token, String title, String message, Intent intent) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);
        try{


            JSONObject json = new JSONObject();
            json.put("to", token);

            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title);
            notificationObj.put("body", message);

            // Set content intent if provided
            if (intent != null) {
                String targetActivityClassName = intent.getComponent().getClassName();
                String action = intent.getAction();
                //print the action
                Log.d(TAG, "pushNotification: " + action);
                Log.d(TAG, "pushNotification: " + targetActivityClassName);

                JSONObject pendingIntentData = new JSONObject();
                pendingIntentData.put("target_activity", targetActivityClassName);
                pendingIntentData.put("action", action);

                Bundle extras = intent.getExtras();
                if (extras != null) {
                    // Convert Bundle to JSON object
                    JSONObject extrasJson = new JSONObject();
                    for (String key : extras.keySet()) {
                        extrasJson.put(key, extras.get(key));
                    }

                    pendingIntentData.put("data", extrasJson.toString());
                } else {
                    pendingIntentData.put("data", "{}"); // Empty JSON object if no extras
                }
                Log.d(TAG, "pushNotification: " + pendingIntentData.toString());


                json.put("data", pendingIntentData);
            }


            json.put("notification", notificationObj);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, base_url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Notification sent successfully");
                    Log.d(TAG, String.valueOf(json));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Notification sending failed: " + error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders()  {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization",  server_key);
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            queue.add(jsonObjectRequest);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
