package com.example.chad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {
    private Contact sender;
    private ArrayList<Contact> receivers;
    private String message;
    private long timeStamp;

    public Message(Contact sender, String msg){
        this.sender = sender;
        this.message = msg;
        this.receivers = new ArrayList<>();
        this.timeStamp = new Date().getTime();
    }


    public Contact getSender() {
        return sender;
    }

    public void setSender(Contact sender) {
        this.sender = sender;
    }

    public ArrayList<Contact> getReceivers() {
        return receivers;
    }

    public void setReceivers(ArrayList<Contact> receivers) {
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
