package com.example.chad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final int KEY = 42;

    private EditText nameText;
    private EditText usernameText;
    private EditText emailText;
    private EditText passwordText;
    private EditText passwordAgainText;
    private TextView errors;

    private FirebaseAuth fbAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Getting the extra data from the intent
        Bundle extras = getIntent().getExtras();
        //If keys do not match terminate process
        if (extras.getInt("SECRET_KEY") != KEY) {
            Log.e(LOG_TAG, "Invalid key in intent!");
            finish();
        }

        //Setting up inputs
        nameText = findViewById(R.id.name);
        usernameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        passwordAgainText = findViewById(R.id.password_again);
        errors = findViewById(R.id.errors);

        fbAuth = FirebaseAuth.getInstance();
    }

    public void register_user(View view) {

        List<String> errors = new ArrayList<>();

        String name = nameText.getText().toString();
        String username = usernameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordAgain = passwordAgainText.getText().toString();

        //Invalid input handling
        if(name.length() <= 0){
            errors.add("Name is mandatory!");
        }else if(username.length() <= 0){
            errors.add("Username is mandatory!");
        }else if(email.length() <= 0){
            errors.add("Email address is mandatory!");
        }else if(password.length() <= 0){
            errors.add("Password is mandatory!");
        }
        //Password match
        if(!password.equals(passwordAgain)){
            errors.add("Passwords do not match!");
        }
        //If there were errors log and set the errors text to the first one
        if(errors.size() > 0){
            for(String e: errors) {
                Log.e(LOG_TAG, e);
            }
            this.errors.setText(errors.get(0));
            return;
        }

        fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.i(LOG_TAG, "User registered successfully! => " + username);
                    cancel(new View(RegistrationActivity.this));
                }else{
                    Log.i(LOG_TAG, "User registration was unsuccessful!");
                    Toast.makeText(RegistrationActivity.this, "Registration error => " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}