package com.example.sportmatch;

public class Message {
    String chatId;
    String message;
    String sender;
    String key;

    public Message(String chatId, String message, String sender, String key) {
        this.chatId = chatId;
        this.message = message;
        this.sender = sender;
        this.key = key;
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

}
