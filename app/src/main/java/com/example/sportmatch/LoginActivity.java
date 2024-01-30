package com.example.sportmatch;


import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.espresso.IdlingResource;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class  LoginActivity extends AppCompatActivity {
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;
    //todo: add a button to go to register activity
    //todo: add a button to go to forgot password activity
    private void saveDeviceToken(String userId, String deviceToken) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        DatabaseReference userRef = usersRef.child(userId);

        userRef.child("deviceToken").setValue(deviceToken)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "Device token saved in the database");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Failed to save device token in the database: " + e.getMessage());
                    }
                });
    }
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF = "pref";
    private static final String Username = "username";
    private static final String Password = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        usernameEditText = findViewById(R.id.activity_main_usernameEditText);
        passwordEditText = findViewById(R.id.activity_main_passwordEditText);
        loginButton = findViewById(R.id.button_login);
        DatabaseReference usersRef = database.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

//        String U = sharedPreferences.getString(Username, null);
//        if(U != null){
//            Intent intent = new Intent(LoginActivity.this, BottomNavActivity.class);
//            startActivity(intent);
//        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                    usernameEditText.setError("Please enter a valid email address");
                    usernameEditText.requestFocus();
                }
                if (password.isEmpty()) {
                    passwordEditText.setError("Please enter a password");
                    passwordEditText.requestFocus();
                }
                else {

                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(Username, username);
                                        editor.putString(Password, password);
                                        editor.apply();


                                        //Request.createRequestTable();
                                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            NotificationManager notificationManager = getSystemService(NotificationManager.class);

                                            if (!notificationManager.isNotificationPolicyAccessGranted() || !notificationManager.areNotificationsEnabled()) {

                                                Intent settingsIntent = new Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                                                settingsIntent.putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, getPackageName());
                                                startActivity(settingsIntent);
                                                Log.d("login", "Opened notification settings");
                                            }

                                        }
                                        FirebaseUser user =mAuth.getCurrentUser();
                                        if (user != null) {
                                            // Obtain the device token
                                            FirebaseMessaging.getInstance().getToken()
                                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<String> task) {
                                                            if (task.isSuccessful() && task.getResult() != null) {
                                                                String deviceToken = task.getResult();

                                                                // Save the device token in the database under the user's node
                                                                saveDeviceToken(user.getUid(), deviceToken);

                                                                // Start the ViewProfileActivity
                                        Intent intent = new Intent(LoginActivity.this, BottomNavActivity.class);
                                        startActivity(intent);
                                                            } else {
                                                                Log.e("TAG", "Failed to obtain device token: " + task.getException());
                                                            }
                                                        }
                                                    });
                                        }
                                    }  else {
                                        Log.d("Eroare",task.getException().getMessage());
                                        Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    }
                                }
                            });

                }

            }
        });
    }
}