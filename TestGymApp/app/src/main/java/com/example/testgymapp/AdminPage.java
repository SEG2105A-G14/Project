package com.example.testgymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminPage extends AppCompatActivity {
    TextView welcomeText;
    Button createClass;
    LinearLayout mainContent;
    LinearLayout createClassOverlay;
    EditText className;
    EditText classDescription;
    Button endClassCreate;
    ImageView topLeft;
    NavigationView drawer;
    TextView showGymClasses;
    FirebaseAuth mAuth;
    FirebaseUser mUser ;
    DatabaseReference myRef;
    TextView showUsers;

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        createClass = (Button) findViewById(R.id.createClassButton);
        createClassOverlay = (LinearLayout) findViewById(R.id.createClassOverlay);
        mainContent = (LinearLayout) findViewById(R.id.mainContent);
        welcomeText = findViewById(R.id.welcomeMessageAdmin);
        endClassCreate = findViewById(R.id.completeClass);
        className = findViewById(R.id.classNameField);
        classDescription = findViewById(R.id.classDescriptionField);
        topLeft = findViewById(R.id.topLefticon);
        drawer = findViewById(R.id.drawer);
        showGymClasses = findViewById(R.id.showGymClasses);
        showUsers = findViewById(R.id.showUsers);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference();

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

        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int state = drawer.getVisibility()==View.VISIBLE? View.GONE:View.VISIBLE;
                drawer.setVisibility(state);
            }
        });

        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createClassOverlay.setVisibility(View.VISIBLE);
            }
        });

        endClassCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser!=null){
                    String name = className.getText().toString().trim().toLowerCase();
                    String description = classDescription.getText().toString().trim();
                    GymClass aGymClass= new GymClass(name, description);
                    if (name.length()!=0 && description.length()!=0) {
                        addGymClass(aGymClass);
                        myRef.child("gymClasses").child(name).getRef().setValue(aGymClass);
                        createClassOverlay.setVisibility(View.GONE);
                        className.setText("");
                        classDescription.setText("");
                        mainContent.setClickable(true);
                        createClassOverlay.setClickable(false);
                    }
                    else {
                        Toast.makeText(AdminPage.this, "Class name/Description should not be empty", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        showGymClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent classes = new Intent(AdminPage.this, GymClasses.class);
                classes.putExtra("name", userName);
                classes.putExtra("role", role);
                startActivity(classes);
            }
        });
        showUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent users = new Intent(AdminPage.this, UserAccountPage.class);
                users.putExtra("name", userName);
                users.putExtra("role", role);
                startActivity(users);
            }
        });
    }

    public void addGymClass(GymClass gymClass){
        final String[] name = {gymClass.getClassName()};
        final boolean[] success = {false};

        myRef.child("gymClasses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(name[0]).exists() && className.getText().toString().length()!=0){
                    Toast.makeText(AdminPage.this, "Class already exist", Toast.LENGTH_SHORT).show();
                }
                else {
                    //snapshot.child(name[0]).getRef().setValue(gymClass);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}