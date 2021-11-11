package com.example.testgymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GymMemberPage extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private TextView welcomeText;
    private TextView noClassesMessage;
    private ListView menuOptions;
    private ImageView showMenu;
    private ImageView showProfile;
    private NavigationView sideNav;
    private ScrollView registeredClassesView;
    private ListView classList;
    private LinearLayout enrolledClassLayout;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mRef;
    private LinearLayout enrolledClassInfoLayout;
    private TextView classNameText;
    private TextView classDayText;
    private TextView classTimeText;
    private TextView classInstructorText;
    private TextView classMaxCapText;
    private Button unenrollButton;
    private ImageView profileIcon;
    private ListView profileOptions;
    private NavigationView profileOptionsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_member_page);

        welcomeText = findViewById(R.id.welcomeMessageMember);
        noClassesMessage = findViewById(R.id.noClassesMessage);
        menuOptions = findViewById(R.id.navMenuOptions);
        showMenu = findViewById(R.id.memberToolbarLeft);
        showProfile = findViewById(R.id.memberToolbarRight);
        sideNav = findViewById(R.id.sideNav);
        registeredClassesView = findViewById(R.id.registeredClasses);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference();
        classList = findViewById(R.id.enrolledClasses);
        classNameText = findViewById(R.id.registeredClassName);
        classDayText = findViewById(R.id.registeredClassDay);
        classInstructorText = findViewById(R.id.registeredClassinstructor);
        classTimeText = findViewById(R.id.registeredClassTime);
        classMaxCapText = findViewById(R.id.registeredClassMaxCap);
        unenrollButton = findViewById(R.id.unenrollButton);
        enrolledClassInfoLayout = findViewById(R.id.enrolledClassInfo);
        enrolledClassLayout = findViewById(R.id.enrolledClassesLayout);
        profileIcon = findViewById(R.id.memberToolbarRight);
        profileOptions = findViewById(R.id.profileOptionsList);
        profileOptionsLayout = findViewById(R.id.profileOptionsMenu);
        ScheduledClass.here[0] = false;

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

        ArrayList<String> options = new ArrayList<>();
        ArrayList<String> classID= new ArrayList<>();
        final String[] selectedClassID = new String[1];
        options.add("Available Classes");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(GymMemberPage.this, R.layout.nav_menu_options, options);
        menuOptions.setAdapter(arrayAdapter);

        menuOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    Intent classesIntent = new Intent(getApplicationContext(), GymClasses.class);
                    classesIntent.putExtra("role", role);
                    classesIntent.putExtra("name", userName);
                    startActivity(classesIntent);
                    sideNav.setVisibility(View.GONE);
                }
            }
        });

        showMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int state = sideNav.getVisibility()==View.VISIBLE? View.GONE:View.VISIBLE;
                sideNav.setVisibility(state);
                profileOptionsLayout.setVisibility(View.GONE);
            }
        });

        //Profile Menu Options
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> profileMenuAdapter = new ArrayAdapter<>(GymMemberPage.this, R.layout.nav_menu_options, arrayList);
        profileOptions.setAdapter(profileMenuAdapter);
        profileMenuAdapter.add("Profile");
        profileMenuAdapter.add("Sign out");

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sideNav.setVisibility(View.GONE);
                int state = profileOptionsLayout.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE;
                profileOptionsLayout.setVisibility(state);
            }
        });
        profileOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){

                }
                else{
                    mAuth.signOut();
                    Intent loginIntent = new Intent(GymMemberPage.this, MainActivity.class);
                    startActivity(loginIntent);
                }
            }
        });

        DatabaseReference classesRef = mRef.child("users").child(mUser.getUid()).child("gymClasses").getRef();

        classesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    noClassesMessage.setVisibility(View.VISIBLE);
                    registeredClassesView.setVisibility(View.GONE);
                }
                else {
                    ArrayList<String> classNames = new ArrayList<>();
                    ArrayList<String> instructorNames = new ArrayList<>();
                    ArrayList<String> datesAndTimes = new ArrayList<>();

                    String className;
                    GymClass myGymClass;
                    int i=0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        classID.add(i, dataSnapshot.getKey().toString());
                        i += 1;
                        if (dataSnapshot.hasChild("startTime")&&dataSnapshot.hasChild("endTime")
                            && dataSnapshot.hasChild("maximumCapacity") && dataSnapshot.hasChild("day")
                        && dataSnapshot.hasChild("instructor") && dataSnapshot.hasChild("classType")) {
                            long startTime = (long) dataSnapshot.child("startTime").getValue();

                            long endTime = (long) dataSnapshot.child("endTime").getValue();
                            long maxCap = (long) dataSnapshot.child("maximumCapacity").getValue();
                            String day = dataSnapshot.child("day").getValue().toString();
                            String instructorName = dataSnapshot.child("instructor").getChildren().iterator().next().getValue().toString();
                            //String instructorName = dataSnapshot.child("instructor").child(instructorID).getValue().toString();
                            className = dataSnapshot.child("classType").getValue().toString();

                            classNames.add(className);
                            instructorNames.add(instructorName);

                            String startTimeString = startTime >= 12 ? Long.toString((12 + startTime) % 12) + " PM" : Long.toString((12 + startTime) % 12) + " AM";
                            String endTimeString = endTime >= 12 ? Long.toString((12 + endTime) % 12) + " PM" : Long.toString((12 + endTime) % 12) + " AM";

                            String period = day.substring(0, 3) + ". " + startTimeString + " - " + endTimeString;
                            datesAndTimes.add(period);
                        }
                        CustomClassList customClassList = new CustomClassList(GymMemberPage.this, classNames, datesAndTimes, instructorNames);
                        classList.setAdapter(customClassList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedClassID[0] = classID.get(position);
                mRef.child("gymClass").child(parent.getItemAtPosition(position).toString()).child(selectedClassID[0]).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        classNameText.setText(snapshot.child("classType").getValue().toString());

                        long startTime = (long) snapshot.child("startTime").getValue();
                        long endTime = (long) snapshot.child("endTime").getValue();
                        String startTimeString = startTime>=12?Long.toString((12+startTime)%12)+" PM":Long.toString((12+startTime)%12)+" AM";
                        String endTimeString = endTime>=12?Long.toString((12+endTime)%12)+" PM":Long.toString((12+endTime)%12)+" AM";
                        classDayText.setText(snapshot.child("day").getValue().toString());
                        classTimeText.setText(startTimeString+" - "+endTimeString);
                        classMaxCapText.setText(snapshot.child("maximumCapacity").getValue().toString());
                        String instructorName = snapshot.child("instructor").getChildren().iterator().next().getValue().toString();
                        classInstructorText.setText(instructorName);

                        enrolledClassInfoLayout.setVisibility(View.VISIBLE);
                        enrolledClassLayout.setAlpha(0.3F);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        unenrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] clicked = {true};
                classesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (clicked[0]) {
                            snapshot.child(selectedClassID[0]).getRef().removeValue();
                            mRef.child("gymClass").child(classNameText.getText().toString()).child(selectedClassID[0]).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (clicked[0]) {
                                        long currentUsers = Integer.parseInt(snapshot.child("numberOfUsers").getValue().toString());
                                        snapshot.child("numberOfUsers").getRef().setValue(currentUsers - 1);
                                        clicked[0] = false;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}