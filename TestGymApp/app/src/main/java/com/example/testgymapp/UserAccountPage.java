package com.example.testgymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAccountPage extends AppCompatActivity {

    ListView instructorsList;
    ListView membersList;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_page);

        instructorsList = findViewById(R.id.listinst);
        membersList = findViewById(R.id.memList);
        myRef = FirebaseDatabase.getInstance().getReference().child("users");
        final String[] selectedUser = {""};
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final ArrayList<String> members = new ArrayList<>();
                final ArrayList<String> instructors = new ArrayList<>();
                final ArrayAdapter membersAdapter = new ArrayAdapter<String>(UserAccountPage.this, R.layout.gym_class_item, members);
                final ArrayAdapter instructorAdapter = new ArrayAdapter<String>(UserAccountPage.this, R.layout.gym_class_item, instructors);
                membersList.setAdapter(membersAdapter);
                instructorsList.setAdapter(instructorAdapter);

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                  if (!dataSnapshot.child("role").getValue().toString().equals("Administrator")){
                   if (dataSnapshot.child("role").getValue().toString().equals("Member")){

                       members.add(dataSnapshot.getKey().toString()+" - "+dataSnapshot.child("name").getValue().toString());
                   }
                   else {
                       instructors.add(dataSnapshot.getKey().toString()+" - "+dataSnapshot.child("name").getValue().toString());
                   }
                  }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(UserAccountPage.this);
        builder.setMessage("Are you sure you want to delete this user ?")
                .setTitle("Delete user").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRef.child(selectedUser[0]).removeValue();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog dialog = builder.create();

        membersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUser[0] = adapterView.getItemAtPosition(i).toString().split("-")[0].trim();
                dialog.show();
            }
        });


        instructorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedUser[0] = adapterView.getItemAtPosition(i).toString().split("-")[0].trim();
                dialog.show();
            }
        });

    }
}