package com.example.testgymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

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

public class GymClasses extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase mdb;
    ListView myGymClasses;
    FirebaseUser mUser;
    DatabaseReference mRef;
    Button completeEdit;
    EditText className;
    EditText classDescription;
    DatabaseReference myRef;
    LinearLayout editClassWin;
    Button deleteButton;

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

        myRef = FirebaseDatabase.getInstance().getReference().child("gymClasses");
        final String[] actualName = new String[1];

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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value = adapterView.getItemAtPosition(i).toString().toLowerCase();

                myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()){
                            actualName[0] = value;
                            String actualDescription = task.getResult().child(value).child("description").getValue().toString();

                            className.setText(actualName[0]);
                            classDescription.setText(actualDescription);
                            editClassWin.setVisibility(View.VISIBLE);
                            editClassWin.setClickable(true);
                        }
                    }
                });


            }
        });
        completeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GymClass gymClass = new GymClass(className.getText().toString(), classDescription.getText().toString());
                className.setText("");
                classDescription.setText("");
                editClassWin.setVisibility(View.GONE);
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
}