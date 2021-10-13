package com.example.testgymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    EditText email;
    EditText password;
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mdb = FirebaseDatabase.getInstance();

        email = (EditText) findViewById(R.id.emailText);
        password = (EditText) findViewById(R.id.loginPassword);
        loginButton = (Button) findViewById(R.id.loginButton);

        TextView createAccountlabel = (TextView) findViewById(R.id.createAccountText);
        createAccountlabel.setTextColor(Color.parseColor("#5323cc"));

        final DatabaseReference[] myRef = new DatabaseReference[1];

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean usernameEntered = verifyUsername();
                boolean passwordVerified = verifyPassword();
                final String[] tempName = new String[1];
                if (usernameEntered&&passwordVerified) {
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(
                            MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        FirebaseUser mUser = mAuth.getCurrentUser();
                                        final String[] role = new String[1];

                                        if (mUser!=null){
                                            String uid = mUser.getUid();
                                            myRef[0] = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                                            myRef[0].addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    User user =  snapshot.getValue(GymMember.class);
                                                    Log.d(TAG, snapshot.toString());
                                                    assert user != null;

                                                    role[0] = user.getRole();
                                                    tempName[0] = user.getName();

                                                    if (role[0].equals("Member")){
                                                        Intent welcomeIntent = new Intent(MainActivity.this, GymMemberPage.class);
                                                        welcomeIntent.putExtra("name", tempName[0]);
                                                        startActivity(welcomeIntent);
                                                    }
                                                    else {
                                                        Intent welcomeIntent = new Intent(MainActivity.this, Instructor.class);
                                                        welcomeIntent.putExtra("name", tempName[0]);
                                                        startActivity(welcomeIntent);
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }
                                }
                            });

                }
            }
        });

        createAccountlabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCreate();
            }
        });
    }

    public void onClickCreate (){
        Intent createAccIntent = new Intent(this, CreateAccount.class);
        startActivity(createAccIntent);
    }
    public boolean verifyUsername(){
        EditText email = (EditText) findViewById(R.id.emailText);

        if (TextUtils.isEmpty(email.getText())) {
            email.setError("Username is required!");
            return false;
        }
        return true;
    }
    public boolean verifyPassword(){
        EditText password = (EditText) findViewById(R.id.loginPassword);

        if(TextUtils.isEmpty(password.getText())){
            password.setError("Password is required !");
            return false;
        }
        return true;
    }
}