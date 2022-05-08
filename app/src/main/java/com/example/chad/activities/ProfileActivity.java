package com.example.chad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.chad.R;
import com.example.chad.models.Contact;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = ProfileActivity.class.getName();
    //Key to start activity safely
    private static final int KEY = 42;

    //Active firebase user
    private FirebaseUser mActiveFirebaseUser;
    private Contact mActiveUser;    //The active user
    //Firestore reference
    private FirebaseFirestore mFirestore;
    //Collection get from the cloud
    private CollectionReference mContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.i(TAG, "onCreate!");

        //Getting the extra data from the intent
        Bundle extras = getIntent().getExtras();
        //If keys do not match terminate process
        if (extras.getInt("SECRET_KEY") != KEY) {
            Log.e(TAG, "Invalid key in intent!");
            finish();
        }

        //Getting active user
        mActiveFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //Checking if there is user, if not than terminate process
        if(mActiveFirebaseUser != null){
            Log.i(TAG, "Active user is set up!");
        }else{
            Log.e(TAG, "No active user logged in!");
            finish();
        }
    }

    public void deleteProfile(View view) {
        mFirestore.collection("contacts").document(mActiveFirebaseUser.getUid()).delete();
        Log.i(TAG, "deleteProfile: profile deleted!");
    }
}