package com.example.testgymapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class GymMemberPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_member_page);

        TextView welcomeText = findViewById(R.id.welcomeMessage);

        String userName = getIntent().getStringExtra("name");
        String message = "Welcome "+userName;

        welcomeText.setText(message);

    }

    public void searchforClass(){

    }

    public void enrollToClass(){

    }

}