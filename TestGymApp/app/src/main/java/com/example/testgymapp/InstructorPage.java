package com.example.testgymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InstructorPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;
    private TextView welcomeText;
    private EditText classNameField, instNameField;
    private ListView classList, instList;
    private Button classCreate, myClasses, searchClass, searchButton, backButton, doneButton, searchButton2, backButton2, searchInstructor, doneButton2;
    private LinearLayout classSearchView, instView;
    private String userName;
    private ConstraintLayout buttonLayout, searchLayout, instructorSearch;
    private ArrayList<String> className;
    private static int SPLASH_TIME_OUT = 2000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_page);

        welcomeText = findViewById(R.id.welcomeMessageInstructor);

        classCreate = findViewById(R.id.createClass);
        myClasses = findViewById(R.id.myClasses);
        searchClass = findViewById(R.id.searchClass);
        searchButton = findViewById(R.id.searchButton);
        buttonLayout = findViewById(R.id.buttonLayout);
        searchLayout = findViewById(R.id.searchLayout);
        classNameField = findViewById(R.id.classNameField);
        classList = findViewById(R.id.classList);
        classSearchView = findViewById(R.id.classSearchView);
        backButton = findViewById(R.id.backButton);
        doneButton = findViewById(R.id.doneButton);
        instructorSearch = findViewById(R.id.instructorSearch);
        instNameField = findViewById(R.id.instNameField);
        backButton2 = findViewById(R.id.backButton2);
        searchButton2 = findViewById(R.id.searchButton2);
        searchInstructor = findViewById(R.id.searchInstructor);
        doneButton2 = findViewById(R.id.doneButton2);
        instList = findViewById(R.id.instList);
        instView = findViewById(R.id.instView);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference();

        userName = getIntent().getStringExtra("name");
        String role = getIntent().getStringExtra("role");
        String message = "Welcome "+userName+"! You are logged in as an "+role;

        className = new ArrayList<>();

        welcomeText.setText(message);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                welcomeText.setVisibility(View.INVISIBLE);
            }
        }, SPLASH_TIME_OUT);

        searchInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instructorSearch.setVisibility(View.VISIBLE);
                buttonLayout.setVisibility(View.INVISIBLE);
            }
        });

        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instructorSearch.setVisibility(View.INVISIBLE);
                buttonLayout.setVisibility(View.VISIBLE);
            }
        });

        searchButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = instNameField.getText().toString().toLowerCase().trim();
                setSearchInstructorAdapter(s);

                instView.setVisibility(View.VISIBLE);
                instructorSearch.setVisibility(View.INVISIBLE);

            }
        });

        doneButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instNameField.setText("");

                instView.setVisibility(View.INVISIBLE);
                instructorSearch.setVisibility(View.VISIBLE);
            }
        });

        classCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent classes = new Intent(InstructorPage.this, GymClasses.class);
                classes.putExtra("name", userName);
                classes.putExtra("role", role);
                startActivity(classes);

            }
        });
        myClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent classes = new Intent(InstructorPage.this, instructorClasses.class);
                classes.putExtra("name", userName);
                classes.putExtra("role", role);
                startActivity(classes);
            }
        });

        searchClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonLayout.setVisibility(View.INVISIBLE);
                searchLayout.setVisibility(View.VISIBLE);

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = classNameField.getText().toString().trim().toLowerCase();
                setAdapter(name);

                searchLayout.setVisibility(View.INVISIBLE);
                classSearchView.setVisibility(View.VISIBLE);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchLayout.setVisibility(View.VISIBLE);
                classSearchView.setVisibility(View.INVISIBLE);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classNameField.setText("");
                searchLayout.setVisibility(View.INVISIBLE);
                buttonLayout.setVisibility(View.VISIBLE);
            }
        });


    }

    private void setAdapter(String searchString){
        mRef.child("gymClass").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<String> nameList = new ArrayList<>();
                final ArrayAdapter adapter = new ArrayAdapter<String>(InstructorPage.this, R.layout.gym_classs_item, nameList);
                classList.setAdapter(adapter);
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    String clssName = snapshot1.getKey().toString();

                    if(clssName.contains(searchString)){
                        for(DataSnapshot classes: snapshot1.getChildren()){
                            String className = classes.child("className").getValue().toString();
                            String day = classes.child("day").getValue().toString();
                            String startTime = classes.child("startTime").getValue().toString();
                            String endTime = classes.child("endTime").getValue().toString();
                            nameList.add(className+" - "+day+" - "+startTime+" to "+endTime);
                        }
                        adapter.notifyDataSetChanged();
                    }else if(nameList.isEmpty()){
                        Toast.makeText(getApplicationContext(), "No such search inquiries exist", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setSearchInstructorAdapter(String searchString){
        mRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<String> nameList = new ArrayList<>();
                final ArrayAdapter adapter = new ArrayAdapter<String>(InstructorPage.this, R.layout.gym_classs_item, nameList);
                instList.setAdapter(adapter);
                for(DataSnapshot users: snapshot.getChildren()){
                    if(users.child("role").getValue().toString().toLowerCase().equals("instructor")){
                        String name = users.child("name").getValue().toString().toLowerCase();
                        if(name.equals(userName)){ }

                        else if(name.contains(searchString)){
                            String email = users.child("email").getValue().toString();
                            nameList.add("Instructor Name: "+name+"\n"+"email: "+email);
                        }
                        adapter.notifyDataSetChanged();
                    }else if(nameList.isEmpty()){
                        Toast.makeText(getApplicationContext(), "No such search inquiries exist", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}