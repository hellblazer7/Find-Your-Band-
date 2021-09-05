package com.example.findyourband;

public class ChatHelper {
    String sender,reciever,message;

    public ChatHelper(String sender,String reciever,String message) {
        this.sender = sender;
        this.reciever=reciever;
        this.message=message;
    }
    public ChatHelper(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
