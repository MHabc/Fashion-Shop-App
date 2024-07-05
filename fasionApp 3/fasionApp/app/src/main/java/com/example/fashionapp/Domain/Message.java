package com.example.fashionapp.Domain;

public class Message {
    private String content;
    private long timestamp;
    private String sender;

    public Message() {
        // Required empty public constructor for Firebase
    }

    public Message(String content, long timestamp, String sender) {
        this.content = content;
        this.timestamp = timestamp;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

