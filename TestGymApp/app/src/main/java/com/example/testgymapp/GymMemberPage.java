package com.example.testgymapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class GymMemberPage extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_member_page);

        TextView welcomeText = findViewById(R.id.welcomeMessage);

        String userName = getIntent().getStringExtra("name");
        String role = getIntent().getStringExtra("role");
        String message = "Welcome "+userName+"! You are logged in as an "+role;

        welcomeText.setText(message);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                welcomeText.setVisibility(View.INVISIBLE);
            }
        }, SPLASH_TIME_OUT);

    }

    public void searchforClass(){

    }

    public void enrollToClass(){

    }

}