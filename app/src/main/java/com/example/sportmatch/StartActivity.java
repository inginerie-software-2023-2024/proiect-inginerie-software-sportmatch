package com.example.sportmatch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF = "pref";
    private static final String Username = "username";
    private static final String Password = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //sursa: https://www.youtube.com/watch?v=To97nvWAgXc&ab_channel=CodeWithKhurshed
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(2690);//cat dureaza anumatia din Splash Screen

                    sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

                    String username = sharedPreferences.getString(Username, null);
                    String password = sharedPreferences.getString(Password, null);

                    if(username != null && password!=null){
                        startActivity(new Intent(StartActivity.this, BottomNavActivity.class));

                    }
                    else{
                        startActivity(new Intent(StartActivity.this, MainActivity.class));

                    }

                    finish();
                }catch(Exception e){

                }
            }
        };
        thread.start();

    }
}