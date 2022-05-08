package com.example.chad.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chad.R;

public class MessengerActivity extends AppCompatActivity {

    private  static final String TAG = MessengerActivity.class.getName();
    private static final int KEY = 42;

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

        try{
//TODO: get username or title from extras and load the messages
        }catch (Error | Exception e){
            Log.e(TAG, "onCreate: Something went wrong while getting errors!", e);
        }
    }
}