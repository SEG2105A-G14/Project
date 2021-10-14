package com.example.testgymapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class GymMemberPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_member_page);

        TextView welcomeText = findViewById(R.id.welcomeMessage);

        String userName = getIntent().getStringExtra("name");
        String role = getIntent().getStringExtra("role");
        String message = "Welcome "+userName+" ! You are logged in as "+role;

        welcomeText.setText(message);

    }

    public void searchforClass(){

    }

    public void enrollToClass(){

    }

}