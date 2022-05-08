package com.example.chad.models;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * Class that represents a contact
 *
 */
public class Contact {

    private static final String TAG = Contact.class.getName();

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String emailAddress;
    //A contact can have contacts
    private ArrayList<String> contacts; //only user ids are stored here, if user is needed we get it from the database but id is enough for writing a message
    private ArrayList<Conversation> conversations; //The conversations the user is currently involved

    public Contact(){
        Random rand = new Random(10000);
        try {
            this.id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getProviderId();
        }catch (Error error){
            Log.w(TAG, "Contact: Error during getting id of the current user!", error);
            this.id = "Guest" + rand.nextLong();
        }
        this.firstName = "Guest";
        this.lastName = "Contact";
        this.username = "Guest" + rand.nextLong();
        this.emailAddress = null;
        this.contacts = new ArrayList<>();
        this.conversations = new ArrayList<>();
    }

    public Contact(String id, String mFirstName, String mLastName, String mUsername, String emailAddress){
        this.id = id;
        this.firstName = mFirstName;
        this.lastName = mLastName;
        this.username = mUsername;
        this.emailAddress = emailAddress;
        this.contacts = new ArrayList<>();
        this.conversations = new ArrayList<>();
    }

    public Contact(String id, String mFirstName, String mLastName, String mUsername, String emailAddress, ArrayList<String> contacts, ArrayList<Conversation> conversations){
        this.id = id;
        this.firstName = mFirstName;
        this.lastName = mLastName;
        this.username = mUsername;
        this.emailAddress = emailAddress;
        this.contacts = contacts;
        this.conversations = conversations;
    }

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public  String getName(){ return this.firstName + " " + this.lastName;}

    @Override
    public String toString(){
        return this.firstName + " " + this.lastName + " as " + this.username;
    }

    public ArrayList<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(ArrayList<Conversation> conversations) {
        this.conversations = conversations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
