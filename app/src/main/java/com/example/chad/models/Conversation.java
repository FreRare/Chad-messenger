package com.example.chad.models;

import java.util.ArrayList;

//Class of a conversation, includes participants and messages
public class Conversation {
    //Stores contact id-s
    private ArrayList<String> participantsList;
    private ArrayList<Message> messages;
    private String title;

    public Conversation(){
        this.participantsList = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.title = "Somebody";
    }

    public  Conversation(String title){
        this.title = title;
        this.participantsList = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public Conversation(String title, ArrayList<String> contacts, ArrayList<Message> messages){
        this.title =title;
        this.messages = messages;
        this.participantsList = contacts;
    }

    public ArrayList<String> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(ArrayList<String> participantsList) {
        this.participantsList = participantsList;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
