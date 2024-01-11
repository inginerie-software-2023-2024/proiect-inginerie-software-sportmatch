package com.example.sportmatch;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {
    String _oldname, _oldpasss, _newpasss, userId, newName, _passdb, _bio, newbio;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        } else {
            Log.e(TAG, "User is null");
            return;
        }

        reference.child(userId).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fullName = dataSnapshot.getValue(String.class);
                Log.d("EditProfileActivity", "oldname: " + fullName);
                TextInputLayout textInputLayout_newName = (TextInputLayout) findViewById(R.id.full_name_editprofile);
                TextInputEditText oldname = (TextInputEditText) textInputLayout_newName.getEditText();
                oldname.setText(fullName);
                _oldname = oldname.getText().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Probleme cu numele vechi");
            }
        });

        reference.child(userId).child("bio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bio = dataSnapshot.getValue(String.class);
                TextInputLayout textInputLayout_newName2 = (TextInputLayout) findViewById(R.id.bio_profile_edit);
                TextInputEditText bioo = (TextInputEditText) textInputLayout_newName2.getEditText();
                bioo.setText(bio);
                _bio = bioo.getText().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Probleme cu schimbarea bioului");
            }
        });

        Button button_savechanges = (Button)findViewById(R.id.button_savechanges);
        button_savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout textInputLayout_newName = (TextInputLayout) findViewById(R.id.full_name_editprofile);
                TextInputEditText editText_newName = (TextInputEditText) textInputLayout_newName.getEditText();
                newName = editText_newName.getText().toString();

                TextInputLayout textInputLayout_newbio = (TextInputLayout) findViewById(R.id.bio_profile_edit);
                TextInputEditText editText_newbio = (TextInputEditText) textInputLayout_newbio.getEditText();
                newbio = editText_newbio.getText().toString();

                Log.d("EditProfileActivity", "newName: " + newName);

                reference.child(userId).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //String fullName = dataSnapshot.getValue(String.class);
                        TextInputLayout oldpass = (TextInputLayout) findViewById(R.id.oldpass);
                        TextInputEditText oldpasss = (TextInputEditText) oldpass.getEditText();
                        //old pass pus de user
                        _oldpasss = oldpasss.getText().toString();
                        Log.e(TAG, "oldpasss1: " + oldpasss);
                        Log.e(TAG, "oldpasss: " + _oldpasss);

                        //old pass din db
                        String passdb = dataSnapshot.getValue(String.class);
                        _passdb = passdb;

                        //new pass pus de user
                        TextInputLayout newpass = (TextInputLayout) findViewById(R.id.newpass);
                        TextInputEditText newpasss = (TextInputEditText) newpass.getEditText();
                        //old pass pus de user
                        _newpasss = newpasss.getText().toString();

                        Log.e(TAG, "vboldpasss: " + _oldpasss);
                        Log.e(TAG, "vbnewpasss: " + _newpasss);
                        Log.e(TAG, "vbpassdb: " + _passdb);

                        //ca sa putem da update la toate in acelasi timp daca vrem
                        Boolean unu = updateNume();
                        Boolean doi = updateParola();
                        Boolean trei = updateBio();
                        if(unu || doi || trei) {
                            Toast.makeText(EditProfileActivity.this, "Data has been updated", Toast.LENGTH_LONG).show();}
                        else Toast.makeText(EditProfileActivity.this, "Data cannot be updated", Toast.LENGTH_LONG).show();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Probleme cu citirea parolei");
                    }
                });


                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));

            }
        });
    }

    private boolean updateNume(){
        if(!_oldname.equals(newName) && !newName.isEmpty())
        {
            reference.child(userId).child("fullName").setValue(newName);
            return true;
        }
     else {
        Log.e(TAG, "Probleme cu schimbarea numelui");
        return false;
    }}

    private boolean updateBio(){
        if( !_bio.isEmpty())
        {
            reference.child(userId).child("bio").setValue(newbio);
            Log.e(TAG, "buni" + newbio   );
            return true;


        }
        else {
            Log.e(TAG, "Probleme cu schimbarea bioului");
            return false;
        }}

    private boolean updateParola(){
        //!_oldpasss.equals(_newpasss) &&
        if( _oldpasss.equals(_passdb))
        {
            reference.child(userId).child("password").setValue(_newpasss);
            Log.e(TAG, "bunaziua" + _newpasss);
            return true;
        }
        else {
            Log.e(TAG, "oldpasss: " + _oldpasss);
            Log.e(TAG, "newpasss: " + _newpasss);
            Log.e(TAG, "passdb: " + _passdb);
            Log.e(TAG, "Probleme cu schimbarea parolei");

            return false;
        }
    }
}