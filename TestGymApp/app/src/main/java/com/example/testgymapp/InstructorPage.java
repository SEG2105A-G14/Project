package com.example.testgymapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class InstructorPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_page);

        TextView welcomeText = findViewById(R.id.welcomeMessageInstructor);

        String userName = getIntent().getStringExtra("name");
        String message = "Welcome "+userName;

        welcomeText.setText(message);
    }
}