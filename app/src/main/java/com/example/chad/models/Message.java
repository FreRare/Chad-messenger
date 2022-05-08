package com.example.chad.models;

import com.example.chad.models.Contact;

import java.util.ArrayList;
import java.util.Date;

public class Message {
    private String sender;
    private ArrayList<String> receivers;
    private String message;
    private long timeStamp;

    public Message(String sender, String msg){
        this.sender = sender;
        this.message = msg;
        this.receivers = new ArrayList<>();
        this.timeStamp = new Date().getTime();
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public ArrayList<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(ArrayList<String> receivers) {
        this.receivers = receivers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
