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
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference mRef;
    TextView welcomeText;
    EditText classNameField;
    ListView classList;
    Button classCreate, myClasses, searchClass, searchButton, backButton, doneButton;
    LinearLayout classSearchView;
    ConstraintLayout buttonLayout, searchLayout, instructorSearch;
    ArrayList<String> className;
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

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference();

        String userName = getIntent().getStringExtra("name");
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

                    System.out.println(clssName);

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
}