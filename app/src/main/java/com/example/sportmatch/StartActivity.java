package com.example.sportmatch;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

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
                    startActivity(new Intent(StartActivity.this, MainActivity.class));

                    finish();
                }catch(Exception e){

                }
            }
        };
        thread.start();

    }
}