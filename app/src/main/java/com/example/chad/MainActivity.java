package com.example.chad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.auth.Token;

public class MainActivity
        extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int KEY = 42;

    private EditText emailET;
    private EditText passwordET;
    private TextView errors;

    private SharedPreferences prefs;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        errors = findViewById(R.id.errors);

        firebaseAuth = FirebaseAuth.getInstance();

        Log.i(LOG_TAG, "onCreate");
    }

    /**
     * Log in the user with firebase auth (email and password)
     * @param view the view
     */
    @SuppressLint("SetTextI18n")
    public void login(View view) {
        String username = emailET.getText().toString();
        String password = passwordET.getText().toString();

        //Input validation
        if(username.length() <= 0){
            errors.setText("No username given!");
            return;
        }else if(password.length() <= 0){
            errors.setText("Password is too short!");
            return;
        }

        Log.i(LOG_TAG, "Logged in username: " + username + ", password: " + password);
        errors.setText("");

        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i(LOG_TAG, "User login was successful!");
                    startMessenger();
                }else{
                    Log.e(LOG_TAG, "User login failed!");
                    Toast.makeText(MainActivity.this, "Something went wrong with the login: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Change to the registration activity
     * Changes the activity to the registration with a little extra..
     * @param view the view
     */
    public void change_to_registration_activity(View view) {
        Intent registrationIntent = new Intent(this, RegistrationActivity.class);
        registrationIntent.putExtra("SECRET_KEY", KEY);
        startActivity(registrationIntent);
    }

    public void login_as_guest(View view){
        //TODO.
        firebaseAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i(LOG_TAG, "Anonymous user login was successful!");
                    startMessenger();
                }else{
                    Log.i(LOG_TAG, "Anonymous user login failed!");
                    Toast.makeText(MainActivity.this, "Something went wrong with the login: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    protected void startMessenger(){
        Intent intent = new Intent(this, ContactListActivity.class);
        intent.putExtra("SECRET_KEY", KEY);
        startActivity(intent);
    }

}