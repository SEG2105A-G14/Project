package com.example.testgymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.*;

public class CreateAccount extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mdb;
    private EditText username;
    private EditText email1;
    private EditText email2;
    private EditText password1;
    private EditText password2;
    private Spinner  gymRoles;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mdb = FirebaseDatabase.getInstance();


        setContentView(R.layout.activity_create_account);

        username = (EditText) findViewById(R.id.usernameField);
        email1 = (EditText) findViewById(R.id.userEmail);
        password1 = (EditText) findViewById(R.id.passwordFirstEntry);
        password2 = (EditText) findViewById(R.id.passwordSecondEntry);
        gymRoles = (Spinner) findViewById(R.id.gymRoles);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gym_roles
        , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        gymRoles.setAdapter(adapter);

        Intent welcomeMember = new Intent(this, GymMemberPage.class);
        Intent welcomeInstructor = new Intent(this, InstructorPage.class);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean usernameVerification = verifyUsername();
                boolean emailVerification = verifyEmail();
                boolean email2Verif = confirmEmail();
                boolean passwordVerification1 = verifyPassword();
                boolean passwordVerification2 = confirmPassword();

                if (usernameVerification&&emailVerification&&passwordVerification1&&passwordVerification2&&email2Verif){
                    mAuth.createUserWithEmailAndPassword(email1.getText().toString(),password1.getText().toString()).
                            addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        DatabaseReference ref = mdb.getReference();

                                      if (gymRoles.getSelectedItem().toString().equals("Gym Member")){
                                          GymMember gymMember = new GymMember(username.getText().toString().trim(), email1.getText().toString().trim());
                                          FirebaseUser mUser = mAuth.getCurrentUser();
                                          assert mUser != null;
                                          String uid = mUser.getUid();
                                          ref.child("users").child(uid).setValue(gymMember);

                                          Intent welcomePageIntent = new Intent(CreateAccount.this, GymMemberPage.class);
                                          welcomePageIntent.putExtra("name", gymMember.getName());
                                          startActivity(welcomePageIntent);

                                      }
                                      else{
                                          Instructor gymInstructor = new Instructor(username.getText().toString().trim(), email1.getText().toString().trim());
                                          FirebaseUser mUser = mAuth.getCurrentUser();
                                          assert mUser != null;
                                          String uid = mUser.getUid();
                                          ref.child("users").child(uid).setValue(gymInstructor);

                                          Intent welcomePageIntent = new Intent(CreateAccount.this, InstructorPage.class);
                                          welcomePageIntent.putExtra("name", gymInstructor.getName());
                                          startActivity(welcomePageIntent);
                                      }

                                    }
                                }
                            });

                }
            }
        });
    }

    public boolean verifyEmail(){
        email1 = (EditText) findViewById(R.id.userEmail);
        //String text = email1.getText().toString().trim();
        //Pattern pattern = Pattern.compile("[a-zA-Z0-9]+\\\\.?[a-zA-Z0-9]+@[a-zA-Z]+\\\\.[a-zA-Z]+", Pattern.CASE_INSENSITIVE);
        return Patterns.EMAIL_ADDRESS.matcher(email1.getText()).matches();
    }

    public boolean confirmEmail(){
        email2 = (EditText) findViewById(R.id.emailSecondEntry);
        String t1 = email1.getText().toString().trim().toLowerCase();
        String t2 = email2.getText().toString().trim().toLowerCase();
        if(!t2.equals(t1)){
            email2.setError("Email entered does not match the previous entered email");
            return false;
        }  return true;
    }

    public boolean verifyUsername(){
        //No whitespace, 4-32 characters, letter or digit, begin with letter but can include digits
        username = (EditText) findViewById(R.id.usernameField);
        String text = username.getText().toString().trim();

        if (text.equals("")){
            username.setError("Username is required!");
            return false;
        }
        if (text.length()<4) {
            username.setError("Username must contain at least 4 characters");
            return false;
        }
        int letters=0;
        for (int i=0; i<text.length(); i++){
              if (!Character.isLetterOrDigit(text.charAt(i))){
                  username.setError("Username must contain only letters and digits");
                  return false;
              }
              if (Character.isLetter(text.charAt(i))) letters++;
        }
        if (letters<0){
            username.setError("Username must contain at least 1 letter");
            return false;
        }
        return true;
    }

    //This method asserts that the respects all the conditions
    public boolean verifyPassword(){
        EditText password = findViewById(R.id.passwordFirstEntry);
        String text = password.getText().toString();

        // Maybe prevent easy passwords

        int passwordLength = text.length();
        int numbers = 0;
        int upperCase =0;
        int symbols=0;

        if (passwordLength<8) {
            password1.setError("Password must contain at least 8 characters");
            return false;
        }

        for (int i=0; i<passwordLength; i++){
            if(Character.isLetter(text.charAt(i))){
                if (Character.isUpperCase(text.charAt(i))) upperCase++;
            }
            else if (Character.isDigit(text.charAt(i))){
                numbers++;
            }
            else {
                symbols++;
            }
        }
        if (upperCase<0&&numbers<0){
            password1.setError("Password must contain at least 1 upper case letter and 1 number");
            return false;
        }
        else if (upperCase<0){
            password1.setError("Password must contain at least 1 letter");
            return false;
        }
        else  if (numbers<0){
            password1.setError("Password must contain at least 1 digit");
            return false;
        }

        return upperCase>0 && numbers>0;
    }

    //This is to check that second password entry is equal to the first one
    public boolean confirmPassword(){
        EditText password1 = findViewById(R.id.passwordFirstEntry);
        EditText password2 = findViewById(R.id.passwordSecondEntry);
        String t1 = password1.getText().toString();
        String t2 = password2.getText().toString();
        return t1.equals(t2);
    }

    public void uodateUI(){


    }
}