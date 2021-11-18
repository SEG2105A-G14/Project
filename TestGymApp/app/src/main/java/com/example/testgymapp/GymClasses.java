package com.example.testgymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GymClasses extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase mdb;
    ListView myGymClasses;
    FirebaseUser mUser;
    DatabaseReference mRef;
    Button completeEdit;
    EditText className;
    EditText classDescription;
    EditText maxCap;
    DatabaseReference myRef;
    LinearLayout editClassWin, instructorClass;
    Button deleteButton, endClassCreate, exit;
    Spinner day, startTime, endTime, difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_classes);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        myGymClasses = findViewById(R.id.classList);
        className = findViewById(R.id.editClassName);
        classDescription = findViewById(R.id.editClassDescription);
        editClassWin = findViewById(R.id.editClassWindow);
        completeEdit = findViewById(R.id.completeClassEdit);
        deleteButton = findViewById(R.id.deleteClassButton);
        endClassCreate = findViewById(R.id.endClassCreate);
        instructorClass = findViewById(R.id.instructorClass);
        maxCap = findViewById(R.id.maxCap);
        exit = findViewById(R.id.exit);


        myRef = FirebaseDatabase.getInstance().getReference().child("gymClassType");
        mRef = FirebaseDatabase.getInstance().getReference().child("users");
        final String[] actualName = new String[1];
        final String[] desc = new String[1];
        String role = getIntent().getStringExtra("role");
        String userName = getIntent().getStringExtra("name");

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


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<String> nameList = new ArrayList<>();
                final ArrayAdapter adapter = new ArrayAdapter<String>(GymClasses.this, R.layout.gym_classs_item, nameList);
                myGymClasses.setAdapter(adapter);

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    nameList.add(dataSnapshot.child("className").getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myGymClasses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long  l) {
                String value = adapterView.getItemAtPosition(i).toString().toLowerCase();

                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            if(role.equals("Administrator")) {
                                actualName[0] = value;
                                String actualDescription = task.getResult().child(value).child("description").getValue().toString();
                                className.setText(actualName[0]);
                                classDescription.setText(actualDescription);
                                editClassWin.setVisibility(View.VISIBLE);
                                myGymClasses.setVisibility(View.INVISIBLE);
                                editClassWin.setClickable(true);
                            }
                            if(role.equals("Instructor")){
                                actualName[0] = value;
                                instructorClass.setVisibility(View.VISIBLE);
                                myGymClasses.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });


            }
        });

        endClassCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mUser.getUid();
                final boolean[] clicked = {true};

                DatabaseReference myRef2 = FirebaseDatabase.getInstance().getReference();

                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                         if(!clicked[0]){

                         }
                         else {
                             String start = startTime.getSelectedItem().toString();
                             String end = endTime.getSelectedItem().toString();
                             String days = day.getSelectedItem().toString();
                             String diff = difficulty.getSelectedItem().toString();

                             int s1 = timeConv(start);
                             int e1 = timeConv(end);


                             if (s1 >= e1) {
                                 Toast.makeText(getApplicationContext(), "You have entered an invalid time frame, please select a valid time frame", Toast.LENGTH_LONG).show();
                                 return;
                             }
                             String name = snapshot.child("users").child(id).child("name").getValue().toString();
                             String email = snapshot.child("users").child(id).child("email").getValue().toString();
                             Instructor tmp = new Instructor(name, email);
                             if (snapshot.hasChild("gymClass")) {
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
                                                     if (actualName[0].equals(classes.child("className").getValue().toString())) {
                                                         noNameConf[0] = false;
                                                         break;
                                                     } else if ((s >= s2 && s < e2) || (e > s2 && e <= e2)) {
                                                         noTimeConf[0] = false;
                                                         break;
                                                     }
                                                 }

                                             }
                                         }
                                         if (noTimeConf[0] && noNameConf[0] && verifyMaxCap()) {
                                             int cap = Integer.parseInt(maxCap.getText().toString());
                                             DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("gymClass");
                                             GymClass gymClass = new GymClass(actualName[0], desc[0], start, end, cap, days, diff, tmp);
                                             mRef.child(id).child("gymClasses").child(actualName[0] + "_" + name + "_" + days + "_" + start + "_" + end).setValue(gymClass);
                                             newRef.child(actualName[0]).child(actualName[0] + "_" + name + "_" + days + "_" + start + "_" + end).setValue(gymClass);
                                             maxCap.setText("");
                                             instructorClass.setVisibility(View.INVISIBLE);
                                             myGymClasses.setVisibility(View.VISIBLE);
                                             clicked[0] = false;
                                             Intent back = new Intent(getApplicationContext(), InstructorPage.class);
                                             back.putExtra("name", userName);
                                             back.putExtra("role", role);
                                             startActivity(back);
                                         } else if (!noTimeConf[0]) {
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

                             } else {
                                 if (verifyMaxCap()) {
                                     int cap = Integer.parseInt(maxCap.getText().toString());
                                     DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("gymClass");
                                     GymClass gymClass = new GymClass(actualName[0], desc[0], start, end, cap, days, diff, tmp);
                                     mRef.child(id).child("gymClasses").child(actualName[0] + "_" + name + "_" + days + "_" + start + "_" + end).setValue(gymClass);
                                     newRef.child(actualName[0]).child(actualName[0] + "_" + name + "_" + days + "_" + start + "_" + end).setValue(gymClass);
                                     maxCap.setText("");
                                     instructorClass.setVisibility(View.INVISIBLE);
                                     myGymClasses.setVisibility(View.VISIBLE);
                                     Intent back = new Intent(getApplicationContext(), InstructorPage.class);
                                     back.putExtra("name", userName);
                                     back.putExtra("role", role);
                                     startActivity(back);
                                 }
                             }
                         }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxCap.setText("");
                instructorClass.setVisibility(View.INVISIBLE);
                myGymClasses.setVisibility(View.VISIBLE);
            }
        });

        completeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GymClass gymClass = new GymClass(className.getText().toString(), classDescription.getText().toString());
                className.setText("");
                classDescription.setText("");
                editClassWin.setVisibility(View.GONE);
                myGymClasses.setVisibility(View.VISIBLE);
                editClassWin.setClickable(false);
                myRef.child(actualName[0]).removeValue();
                myRef.child(gymClass.getClassName().toLowerCase()).setValue(gymClass);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(GymClasses.this);
        builder.setMessage("Are you sure you want to delete this class ?")
                .setTitle("Delete class").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRef.child(actualName[0]).removeValue();
                        className.setText("");
                        classDescription.setText("");
                        editClassWin.setVisibility(View.GONE);
                        myGymClasses.setVisibility(View.VISIBLE);
                        editClassWin.setClickable(false);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog = builder.create();
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });



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

}