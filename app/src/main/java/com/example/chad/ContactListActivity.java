package com.example.chad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ContactListActivity extends AppCompatActivity {

    private static final String TAG = ContactListActivity.class.getName();
    private static final int KEY = 42;

    private FirebaseAuth fbAuth;
    private FirebaseUser activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Log.i(TAG, "onCreate!");

        //Getting the extra data from the intent
        Bundle extras = getIntent().getExtras();
        //If keys do not match terminate process
        if (extras.getInt("SECRET_KEY") != KEY) {
            Log.e(TAG, "Invalid key in intent!");
            finish();
        }


        fbAuth = FirebaseAuth.getInstance();
        activeUser = fbAuth.getCurrentUser();

        if(activeUser != null){
            Log.i(TAG, "Active user is set up!");
        }else{
            Log.e(TAG, "No active user logged in!");
            finish();
        }


    }
}