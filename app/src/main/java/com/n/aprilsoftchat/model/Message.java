package com.n.aprilsoftchat.model;

import java.util.List;

public class Message {

    private List<Message> messages;
    private Integer id;
    private String time;
    private String user;
    private String text;

    public Message(Integer id, String time, String user, String text) {
        this.id = id;
        this.time = time;
        this.user = user;
        this.text = text;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Integer getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

}
