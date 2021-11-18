package com.example.testgymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class instructorClasses extends AppCompatActivity {

    ListView classLst, allClassLst;
    DatabaseReference myRef, myRef2;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    Spinner day, startTime, endTime, difficulty;
    Button endClassEdit, deleteClassButton, viewMyClasses, viewAllClasses;
    LinearLayout instructorClass, viewClassButtons;
    EditText maxCap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_classes);

        final String[] dbName = new String[1];
        final String[] nameValue = new String[1];
        String role = getIntent().getStringExtra("role");
        String userName = getIntent().getStringExtra("name");
        
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        String id = mUser.getUid();

        classLst = findViewById(R.id.classLst);
        allClassLst = findViewById(R.id.allClassLst);
        endClassEdit = findViewById(R.id.endClassEdit);
        deleteClassButton = findViewById(R.id.deleteClassButton);
        instructorClass = findViewById(R.id.instructorClass);
        viewClassButtons = findViewById(R.id.viewClassButtons);
        viewAllClasses = findViewById(R.id.viewAllClasses);
        viewMyClasses = findViewById(R.id.viewMyClasses);
        maxCap = findViewById(R.id.maxCap);


        day = findViewById(R.id.classDay);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(adapter1);

        startTime = findViewById(R.id.startTime);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startTime.setAdapter(adapter2);

        endTime = findViewById(R.id.endTime);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.times, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        endTime.setAdapter(adapter3);

        difficulty = findViewById(R.id.classDifficulty);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.difficulty, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty.setAdapter(adapter4);




        myRef = FirebaseDatabase.getInstance().getReference().child("users");
        myRef2 = FirebaseDatabase.getInstance().getReference().child("gymClass");



        viewMyClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewClassButtons.setVisibility(View.INVISIBLE);
                classLst.setVisibility(View.VISIBLE);

            }
        });

        viewAllClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewClassButtons.setVisibility(View.INVISIBLE);
                allClassLst.setVisibility(View.VISIBLE);
            }
        });

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<String> nameList = new ArrayList<>();
                final ArrayAdapter adapter = new ArrayAdapter<String>(instructorClasses.this, R.layout.gym_classs_item, nameList);
                allClassLst.setAdapter(adapter);
                for(DataSnapshot names: snapshot.getChildren()){
                    for(DataSnapshot classes: names.getChildren() ){
                        String className = classes.child("className").getValue().toString();
                        String day = classes.child("day").getValue().toString();
                        String startTime = classes.child("startTime").getValue().toString();
                        String endTime = classes.child("endTime").getValue().toString();
                        nameList.add(className+" - "+day+" - "+startTime+" to "+endTime+" taught by "+userName);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        myRef.child(id).child("gymClasses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<String> nameList = new ArrayList<>();
                final ArrayAdapter adapter = new ArrayAdapter<String>(instructorClasses.this, R.layout.gym_classs_item, nameList);
                classLst.setAdapter(adapter);
                for(DataSnapshot classes:snapshot.getChildren()){
                    String className = classes.child("className").getValue().toString();
                    String day = classes.child("day").getValue().toString();
                    String startTime = classes.child("startTime").getValue().toString();
                    String endTime = classes.child("endTime").getValue().toString();
                    nameList.add(className+" - "+day+" - "+startTime+" to "+endTime);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        classLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString().toLowerCase();
                String[] values = value.split(" - ");
                System.out.println(values[1]);

                myRef.child(id).child("gymClasses").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            for (DataSnapshot classID: task.getResult().getChildren()){
                                if(values[0].equals(classID.child("className").getValue().toString().toLowerCase()) && values[1].equals(classID.child("day").getValue().toString().toLowerCase())){
                                    System.out.println(values[1]);
                                    nameValue[0] = values[0];
                                    String days = classID.child("day").getValue().toString();
                                    String start = classID.child("startTime").getValue().toString();
                                    String end = classID.child("endTime").getValue().toString();
                                    String diff = classID.child("difficulty").getValue().toString();
                                    String maxCapacity = classID.child("maximumCapacity").getValue().toString();
                                    dbName[0]= nameValue[0] + "_" + userName + "_" + days + "_" + start + "_" + end;

                                    ArrayAdapter daySpinner = (ArrayAdapter) day.getAdapter();
                                    int spinnerPos = daySpinner.getPosition(days);
                                    day.setSelection(spinnerPos);

                                    ArrayAdapter startSpinner = (ArrayAdapter) startTime.getAdapter();
                                    spinnerPos = startSpinner.getPosition(start);
                                    startTime.setSelection(spinnerPos);

                                    ArrayAdapter endSpinner = (ArrayAdapter) endTime.getAdapter();
                                    spinnerPos = endSpinner.getPosition(end);
                                    endTime.setSelection(spinnerPos);

                                    ArrayAdapter diffSpinner = (ArrayAdapter) difficulty.getAdapter();
                                    spinnerPos = diffSpinner.getPosition(diff);
                                    difficulty.setSelection(spinnerPos);

                                    maxCap.setText(maxCapacity);

                                    classLst.setVisibility(View.INVISIBLE);
                                    instructorClass.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                });
            }
        });

        deleteClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference gRef = FirebaseDatabase.getInstance().getReference().child("gymClass");
                gRef.child(nameValue[0]).child(dbName[0]).removeValue();
                myRef.child(id).child("gymClasses").child(dbName[0]).removeValue();
                classLst.setVisibility(View.VISIBLE);
                instructorClass.setVisibility(View.INVISIBLE);
            }
        });

        endClassEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final boolean[] clicked = {true};

                DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference();

                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!clicked[0]){

                        }
                        else{
                            String d = snapshot.child("gymClassType").child(nameValue[0]).child("description").getValue().toString();
                            String start = startTime.getSelectedItem().toString();
                            String end = endTime.getSelectedItem().toString();
                            String days = day.getSelectedItem().toString();
                            String diff = difficulty.getSelectedItem().toString();

                            int s1 = timeConv(start);
                            int e1 = timeConv(end);

                            String name = snapshot.child("users").child(id).child("name").getValue().toString();
                            String email = snapshot.child("users").child(id).child("email").getValue().toString();
                            Instructor tmp = new Instructor(name, email);

                            if (s1 >= e1) {
                                Toast.makeText(getApplicationContext(), "You have entered an invalid time frame, please select a valid time frame", Toast.LENGTH_LONG).show();
                                return;
                            }

                            myRef2.child("gymClass").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    final boolean[] noTimeConf = {true};
                                    final boolean[] noNameConf = {true};
                                    int s = timeConv(start);
                                    int e = timeConv(end);
                                    for (DataSnapshot classID : snapshot.getChildren()) {
                                        for (DataSnapshot classes : classID.getChildren()) {
                                            int s2 = timeConv(classes.child("startTime").getValue().toString());
                                            int e2 = timeConv(classes.child("endTime").getValue().toString());
                                            if (days.equals(classes.child("day").getValue().toString())) {
                                                if (nameValue[0].equals(classes.child("className").getValue().toString()) && !userName.equals(classes.child("instructor").child("name").getValue().toString())) {
                                                    noNameConf[0] = false;
                                                    break;
                                                } else if ((s >= s2 && s < e2) || (e > s2 && e <= e2)) {
                                                    noTimeConf[0] = false;
                                                    break;
                                                }
                                            }

                                        }
                                    }
                                    if(noTimeConf[0] && noNameConf[0] && verifyMaxCap()){
                                        int cap = Integer.parseInt(maxCap.getText().toString());


                                        DatabaseReference gRef = FirebaseDatabase.getInstance().getReference().child("gymClass");
                                        gRef.child(nameValue[0]).child(dbName[0]).removeValue();
                                        myRef.child(id).child("gymClasses").child(dbName[0]).removeValue();

                                        GymClass gymClass = new GymClass(nameValue[0], d, start, end, cap, days, diff, tmp);

                                        myRef2.child("gymClass").child(nameValue[0]).child(nameValue[0] + "_" + name + "_" + days + "_" + start + "_" + end).setValue(gymClass);
                                        myRef.child(id).child("gymClasses").child(nameValue[0] + "_" + name + "_" + days + "_" + start + "_" + end).setValue(gymClass);


                                        clicked[0] = false;

                                        classLst.setVisibility(View.VISIBLE);
                                        instructorClass.setVisibility(View.INVISIBLE);

                                        Intent classes = new Intent(instructorClasses.this, InstructorPage.class);
                                        classes.putExtra("name", userName);
                                        classes.putExtra("role", role);
                                        startActivity(classes);

                                    }else if (!noTimeConf[0]) {
                                        Toast.makeText(getApplicationContext(), "The day and time you have chosen conflict with another class, please enter another day or time slot", Toast.LENGTH_LONG).show();
                                        clicked[0] = false;
                                    } else if (!noNameConf[0]) {
                                        Toast.makeText(getApplicationContext(), "The day you have chosen conflict with another class of the same type, please enter another day", Toast.LENGTH_LONG).show();
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





    public boolean verifyMaxCap(){
        maxCap = (EditText) findViewById(R.id.maxCap);
        String cap = maxCap.getText().toString().trim();
        try{
            int max = Integer.parseInt(cap);
            if(max < 0){
                maxCap.setError("Entered invalid class capacity");
                return false;
            }
            if(max > 50){
                maxCap.setError("Class capacity must not exceed 50 members");
                return false;
            }

        }
        catch (NumberFormatException e){
            maxCap.setError("Entered invalid capacity");
            return false;
        }

        return true;
    }

    public int timeConv(String time){
        String[] times = time.split(" ");
        int hours = Integer.parseInt(times[0].replace(":",""));
        if(times[1].equals("am") || times[0].startsWith("12")){
            return hours;
        }else{
            return hours+1200;
        }
    }
}