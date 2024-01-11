package com.example.sportmatch;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class OtherProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        String userId = intent.getStringExtra("user");
        DatabaseReference userRef = database.getReference("Users").child(userId);




        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("fullName").getValue(String.class);
                TextView nameTextView = findViewById(R.id.fullname_field);
                nameTextView.setText(name);
                TextInputLayout nameTextInputLayout = findViewById(R.id.full_name_profile);
                TextInputEditText nameEditText = (TextInputEditText) nameTextInputLayout.getEditText();
                nameEditText.setText(name);


                String email = dataSnapshot.child("username").getValue(String.class);
                TextInputLayout emailTextInputLayout = findViewById(R.id.email_profile);
                TextInputEditText emailEditText = (TextInputEditText) emailTextInputLayout.getEditText();
                emailEditText.setText(email);

                String bio = dataSnapshot.child("bio").getValue(String.class);
                TextInputLayout bioTextInputLayout = findViewById(R.id.bio_profile);
                TextInputEditText bioEditText = (TextInputEditText) bioTextInputLayout.getEditText();
                bioEditText.setText(bio);

                String birthdate = dataSnapshot.child("birthDate").getValue(String.class);
                LocalDate birthdateObj = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    birthdateObj = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }
                LocalDate now = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    now = LocalDate.now();
                }
                int varsta = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    varsta = Period.between(birthdateObj, now).getYears();
                }
                TextInputLayout birthdateTextInputLayout = findViewById(R.id.age_profile);
                TextInputEditText birthdateEditText = (TextInputEditText) birthdateTextInputLayout.getEditText();
                birthdateEditText.setText(String.valueOf(varsta));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        ViewGroup parentView = findViewById(R.id.relativeLayout);
        Button button_editprofile = (Button)findViewById(R.id.button_editprofile);
        parentView.removeView(button_editprofile);
        Button logoutButton = findViewById(R.id.button_logout);
        parentView.removeView(logoutButton);

        countEventsForUser(userId, new ViewProfileActivity.OnEventsCountedListener() {
            @Override
            public void onEventsCounted(int eventCount) {
                TextView view = findViewById(R.id.payment_label);
                view.setText(String.valueOf(eventCount));
                String message = "Number of events: " + eventCount;
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });



        ////inceput meniu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_view_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_view_profile:
                    return true;
                case R.id.bottom_admin_events:
                    startActivity(new Intent(getApplicationContext(), AdminEventsActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
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
                case R.id.bottom_home:///bottom_home corespunde clasei BottomNavActivity
                    startActivity(new Intent(getApplicationContext(), BottomNavActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

        ////final meniu
    }

    interface OnEventsCountedListener {
        void onEventsCounted(int eventCount);
    }
    public void countEventsForUser(String userId, ViewProfileActivity.OnEventsCountedListener listener) {
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("Events");
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int eventCount = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);

                    if (event.getCreator()!=null && event.getCreator().equals(userId)) {
                        eventCount++;
                    }
                }

                listener.onEventsCounted(eventCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur during the database query
                listener.onEventsCounted(0);
            }
        });
    }
}