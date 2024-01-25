package com.example.sportmatch;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout FullName;
    TextInputEditText FullNameInserted;

    TextInputLayout Username;
    TextInputEditText UsernameInserted;

    TextInputLayout Password;
    TextInputEditText PasswordInserted;

    TextInputLayout ConfirmPassword;
    TextInputEditText PasswordConfirmed;

    TextInputLayout BirthDate;
    TextInputEditText BirthDateInserted;

    TextInputLayout Gender;
    AutoCompleteTextView GenderInserted;

    DatePickerDialog.OnDateSetListener setListener;

    //String[] genders = {"Female", "Male"};
    //ArrayAdapter<String> adapterGender;



    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupButtonClicked(v);
            }
        });

        Username =  findViewById(R.id.Username);
        UsernameInserted =  findViewById(R.id.UsernameInserted);
        FullName =  findViewById(R.id.FullName);
        FullNameInserted =  findViewById(R.id.FullNameInserted);
        Password =  findViewById(R.id.Password);
        PasswordInserted =  findViewById(R.id.PasswordInserted);
        ConfirmPassword =  findViewById(R.id.ConfirmPassword);
        PasswordConfirmed =  findViewById(R.id.PasswordConfirmed);
        BirthDate =  findViewById(R.id.BirthDate);
        BirthDateInserted =  findViewById(R.id.BirthDateInserted);
        //Gender =  findViewById(R.id.Gender);
        //GenderInserted =  findViewById(R.id.GenderInserted);

        //ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this, R.layout.list_sport, genders);
        //GenderInserted.setAdapter(adapterGender);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        BirthDateInserted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = String.format("%02d/%02d/%d", dayOfMonth, month, year);


                BirthDateInserted.setText(date);
            }
        };

        mAuth = FirebaseAuth.getInstance();

    }


        //check if the username is already in use - copiat de pe stackoverflow
        //https://stackoverflow.com/questions/61523624/android-firebase-database-check-if-username-is-already-use

    public void isValidUsername(UserExistsCallback callback) {
        String Username = UsernameInserted.getText().toString();
        FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(Username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    callback.onCallback(false);
                    UsernameInserted.setError("This username already exists");
                } else {
                    callback.onCallback(true);
                    Log.d("createUsername", "true");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException(); // never ignore errors
            }
        });
    }

    public void signupButtonClicked(View v){
        String txtFullName = FullNameInserted.getText().toString().trim();
        String txtUserName = UsernameInserted.getText().toString().trim();
        String txtPassword = PasswordInserted.getText().toString().trim();
        String txtPasswordConfirmed = PasswordConfirmed.getText().toString().trim();
        String txtBirthDate = BirthDateInserted.getText().toString().trim();
        //String txtGender = GenderInserted.getText().toString().trim();


        if(txtUserName.isEmpty() ){
            UsernameInserted.setError("Please enter all the fields");
            UsernameInserted.requestFocus();
        }
        else if(txtPassword.isEmpty()){
            PasswordInserted.setError("Please enter all the fields");
            UsernameInserted.requestFocus();
        }
        else if(txtPasswordConfirmed.isEmpty()){
            PasswordConfirmed.setError("Please enter all the fields");
            UsernameInserted.requestFocus();
        }
        else if(txtBirthDate.isEmpty()){
            BirthDateInserted.setError("Please enter all the fields");
            UsernameInserted.requestFocus();
        }
//        else if(txtGender.isEmpty()){
//            GenderInserted.setError("Please enter all the fields");
//            UsernameInserted.requestFocus();
//        }
        else if(txtFullName.isEmpty()) {
            FullNameInserted.setError("Please enter all the fields");
            UsernameInserted.requestFocus();
        }
        else if(!txtPassword.equals(txtPasswordConfirmed)){
            PasswordInserted.setError("Passwords do not match");
            PasswordConfirmed.setError("Passwords do not match");
        }
        else{
            //check if the username is already in use
            isValidUsername(new UserExistsCallback() {
                @Override
                public void onCallback(boolean exists) {
                    System.out.println("User exists: "+exists);
                }
            });
            //create the user in the database - generated with copilot
            mAuth.createUserWithEmailAndPassword(txtUserName,txtPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                String hashedPassword = hashPassword(txtPassword);
                                User user = new User(txtUserName, hashedPassword, txtBirthDate, txtFullName);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "User failed to register", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "User failed to register", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private String hashPassword(String password) {
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

            byte[] hashedBytes = md.digest(passwordBytes);

            StringBuilder hexStringBuilder = new StringBuilder();
            for(byte b : hashedBytes){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1){
                    hexStringBuilder.append('0');
                }
                hexStringBuilder.append(hex);
            }

            return hexStringBuilder.toString();

        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
    }


}
