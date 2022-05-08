package com.example.chad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chad.R;
import com.example.chad.models.Contact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private final String CONTACTS_COLLECTION_NAME = "contacts";
    private final String CONVERSATIONS_COLLECTION_NAME = "conversations";
    private static final int KEY = 42;

    private EditText mNameText;
    private EditText mUsernameText;
    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mPasswordAgainText;
    private TextView mErrors;

    private FirebaseAuth mFbAuth;
    private Contact mRegisteredContact;


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
        mNameText = findViewById(R.id.name);
        mUsernameText = findViewById(R.id.username);
        mEmailText = findViewById(R.id.email);
        mPasswordText = findViewById(R.id.password);
        mPasswordAgainText = findViewById(R.id.password_again);
        mErrors = findViewById(R.id.errors);

        mFbAuth = FirebaseAuth.getInstance();
    }

    public void register_user(View view) {

        List<String> errors = new ArrayList<>();

        String name = mNameText.getText().toString();
        String username = mUsernameText.getText().toString();
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();
        String passwordAgain = mPasswordAgainText.getText().toString();

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
            this.mErrors.setText(errors.get(0));
            return;
        }

        mFbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {
                    if (task.isSuccessful()) {
                        Log.i(LOG_TAG, "User registered successfully to authentication! => " + username);
                        String firstName = name.split(" ")[0];
                        String lastName = name.split(" ")[1];
                        String userId = mFbAuth.getUid();
                        if (userId == null || userId.length() <= 0) {
                            Log.e(LOG_TAG, "onComplete: No user id provided from fireStore! User cannot be created into cloud! Registration failed!");
                        } else {
                            mRegisteredContact = new Contact(userId, firstName, lastName, username, email);
                            //Adding two basic contacts to every user
                            mRegisteredContact.getContacts().add("Jo3p1WRrjQdQH1Iffe3JU2aO9g93");
                            mRegisteredContact.getContacts().add("Acs51TtusiVprWZldiEKtenwcY92");
                            FirebaseFirestore.getInstance().collection(CONTACTS_COLLECTION_NAME).add(mRegisteredContact);
                            Log.i(LOG_TAG, "onComplete: User added to firebase! Starting messenger...");
                            startMessenger();
                        }
                    } else {
                        Log.i(LOG_TAG, "User registration was unsuccessful!");
                        Toast.makeText(RegistrationActivity.this, "Registration error => " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }catch (Error | Exception e){
                    Log.e(LOG_TAG, "onComplete: Registration error:", e);
                }
            }
        });
    }

    private void startMessenger(){
        Intent messenger = new Intent(this, ContactListActivity.class);
        messenger.putExtra("SECRET_KEY", KEY);
        startActivity(messenger);
    }

    public void cancel(View view){
        finish();
    }
}