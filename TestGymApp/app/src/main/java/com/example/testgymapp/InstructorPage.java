package com.example.testgymapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class InstructorPage extends AppCompatActivity {
    TextView welcomeText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_page);

        welcomeText = findViewById(R.id.welcomeMessageInstructor);

        String userName = getIntent().getStringExtra("name");
        String role = getIntent().getStringExtra("role");
        String message = "Welcome "+userName+"! You are logged in as an "+role;

        welcomeText.setText(message);
    }
}