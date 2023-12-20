package com.example.sportmatch;

import android.os.Build;

import java.time.LocalTime;

public class Message {
    String CreatedAt;
    String chatId;
    String message;
    String sender;
    String key;

    public Message(String chatId, String message, String sender, String key) {
        this.chatId = chatId;
        this.message = message;
        this.sender = sender;
        this.key = key;
        //set created at to HH:MM format
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.CreatedAt = LocalTime.now().toString().substring(0,5);
        }
    }


    public String getChatId() {
        return chatId;
    }
    public String getCreatedAt() {
        return CreatedAt;
    }

    public Message() {

    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}
