package com.example.chad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.chad.models.Contact;
import com.example.chad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    private static final String TAG = ContactListActivity.class.getName();
    //Key to start activity safely
    private static final int KEY = 42;
    //Active firebase user
    private FirebaseUser mActiveFirebaseUser;
    private Contact mActiveUser;    //The active user
    //Firestore reference
    private FirebaseFirestore mFirestore;
    //Collection get from the cloud
    private CollectionReference mContacts;

    //flag for loading data with service
    private boolean isSomethingLoading = false;

    //Real members
    private RecyclerView mRecyclerView; //The view for the whole page
    private ArrayList<Contact> mContactList;    //Contacts listed
    private ContactListAdapter mContactAdapter; //Adapter to use recycle view
    private int mContactListGridNumber = 1;     //I do not need this but might be helpful sometimes when developing the app further


    @Override
    @SuppressLint("SourceLockedOrientationActivity")
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

        //Setting up recycler view
        mRecyclerView = findViewById(R.id.contact_list_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, mContactListGridNumber));
        //Init contact list and adapter
        mContactList = new ArrayList<>();
        mContactAdapter = new ContactListAdapter(this, mContactList);
        mRecyclerView.setAdapter(mContactAdapter);

        //If user is not logged in anonymously than load the user contacts form the cloud
        if(!mActiveFirebaseUser.isAnonymous()){
            Log.i(TAG, "onCreate: Logged in user is known. Getting data from cloud...");
            //firebase setup
            mFirestore = FirebaseFirestore.getInstance();
            mContacts = mFirestore.collection("contacts");
            findActiveUser();
        }else{
            this.mActiveUser = new Contact();
            this.mContactList = new ArrayList<>();
        }
    }

    private void findActiveUser(){
        try {
            //finding active user in database
            mContacts.whereEqualTo("id", mActiveFirebaseUser.getUid()).get().addOnSuccessListener(query -> {
                //If found set the user to it only one user should exist per id
                this.mActiveUser = query.toObjects(Contact.class).get(0);
                Log.d(TAG, "findActiveUser: User found:" + this.mActiveUser.toString());
                //If user is found than init contact list by its data
            }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    initContactsList();
                }
            });
        }catch (Error | Exception e){
            Log.e(TAG, "findActiveUser: Cannot find user with the given id: ", e);
        }
    }

    private void initContactsList() {
        try {
            ArrayList<String> mUserContactsList = mActiveUser.getContacts();
            if(mUserContactsList.size() == 0){
                Log.d(TAG, "initContactsList: User have no contacts. Initializing contact list and return.");
                this.mContactList = new ArrayList<>();
                return;
            }
            //Get data form database based on the current user and initialize contact list from it
            for (String userId : mUserContactsList) {
                //Because id is unique the returned query has only one element
                mContacts.whereEqualTo("id", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            mContactList.add(task.getResult().toObjects(Contact.class).get(0));
                            Log.i(TAG, "initContactsList: User with id " + userId + " successfully added to the contact list!");
                        }else{
                            Log.e(TAG, "initContactsList: Problem while getting contacts from the cloud!");
                        }
                        mContactAdapter.notifyDataSetChanged();
                    }
                });
            }
        }catch (Exception | Error e){
            Log.e(TAG, "initContactsList: Problem while getting contacts from the cloud => ", e);
        }
    }

    public void messageContact(View view) {
        Log.d(TAG, "messageContact: Messenger activity started without user to message.");
        Intent intent = new Intent(this, MessengerActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.messenger_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { return false; }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "onQueryTextChange: " + s);
                mContactAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_message:
                //Open compose message writer activity
                Log.d(TAG, "onOptionsItemSelected: new message clicked");
                Intent intent = new Intent(this, MessageComposeActivity.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                //Open profile
                Log.d(TAG, "onOptionsItemSelected: profile clicked");
                Intent profile = new Intent(this, ProfileActivity.class);
                startActivity(profile);
                return true;
            case R.id.settings:
                //Open settings
                Log.d(TAG, "onOptionsItemSelected: settings clicked");
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                return true;
            case R.id.logout:
                //Logging out user and return to login page
                Log.d(TAG, "onOptionsItemSelected: logout clicked");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}