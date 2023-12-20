package com.example.sportmatch;


import static android.content.ContentValues.TAG;

import android.content.Context;
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

    public static void pushNotification(Context context, String token, String title, String message) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);
        try{
            JSONObject json = new JSONObject();
            json.put("to", token);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title);
            notificationObj.put("body", message);
            json.put("notification", notificationObj);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, base_url, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Notification sent successfully");
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
