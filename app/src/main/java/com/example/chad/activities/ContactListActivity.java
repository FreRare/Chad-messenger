package com.example.chad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private static final String TAG = ContactListActivity.class.getName();
    private static final int KEY = 42;

    private FirebaseAuth fbAuth;
    private FirebaseUser activeUser;

    private RecyclerView recyclerView;
    private ArrayList<Contact> contactList;
    private ContactAdapter contactAdapter;
    private int contactListGridNumber = 1;


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

        //Getting active user
        fbAuth = FirebaseAuth.getInstance();
        activeUser = fbAuth.getCurrentUser();
        //Checking if there is user, if not than terminate process
        if(activeUser != null){
            Log.i(TAG, "Active user is set up!");
        }else{
            Log.e(TAG, "No active user logged in!");
            finish();
        }

        //Setting up recycler view
        recyclerView = findViewById(R.id.contact_list_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, contactListGridNumber));
        //Init contact list and adapter
        contactList = new ArrayList<>();
        contactAdapter = new ContactAdapter(this, contactList);
        recyclerView.setAdapter(contactAdapter);

        initContacts();

    }

    private void initContacts() {
        //Get data form database based on the current user and initialize contact list from it
        //Now just for checking if it works
        contactList.add(new Contact("Test1", "Test", "Tester1", "Tester@gmail.com"));
        contactList.add(new Contact("Test2", "Test", "Tester2", "Tester@gmail.com"));
        contactList.add(new Contact("Test3", "Test", "Tester3", "Tester@gmail.com"));
        //Notifying adapter
        contactAdapter.notifyDataSetChanged();
    }

    public void messageContact(View view) {
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
                contactAdapter.getFilter().filter(s);
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