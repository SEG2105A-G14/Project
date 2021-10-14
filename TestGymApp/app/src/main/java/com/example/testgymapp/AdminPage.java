package com.example.testgymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        String userName = getIntent().getStringExtra("name");
        String role = getIntent().getStringExtra("role");
        String message = "Welcome "+userName+" ! You are logged in as "+role;

        welcomeText.setText(message);

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
                    GymClass aGymClass= createGymClass(name, description);
                    addGymClass(aGymClass);



                }
            }
        });
    }
    public GymClass createGymClass(String className, String description){
        return new GymClass(className, description);
    }
    public void addGymClass(GymClass gymClass){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        String name = gymClass.getClassName();


        myRef.child("gymClasses").child(gymClass.getClassName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(AdminPage.this, "Class already exist", Toast.LENGTH_SHORT).show();
                }
                else {
                    myRef.child("gymClasses").child(gymClass.getClassName()).setValue(gymClass);
                    createClassOverlay.setVisibility(View.GONE);
                    className.setText("");
                    classDescription.setText("");
                    mainContent.setClickable(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}