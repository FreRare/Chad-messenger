package com.example.chad;

import com.google.firebase.firestore.SnapshotMetadata;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a contact
 *
 */
public class Contact {

    private String firstName;
    private String lastName;
    private String username;
    private String emailAddress;
    private ArrayList<Contact> contacts;

    public Contact(String firstName, String lastName, String username, String emailAddress){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.emailAddress = emailAddress;
        this.contacts = new ArrayList<>();
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
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

    public Message getLastMessage(){
        return new Message(this, "Some text");
    }

    public String getName(){ return this.firstName + " " + this.lastName;}
}
