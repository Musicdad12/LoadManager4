package com.jrschugel.loadmanager;

/**
 * Created by seanm on 8/5/2017.
 * Copyright 2017. All rights reserved.
 */


class NotificationData {

    static final String TEXT = "TEXT";

    private int id; // identificador da notificação
    private String title;
    private String textMessage;
    private String sound;

    NotificationData(int id, String title, String textMessage, String sound) {
        this.id = id;
        this.title = title;
        this.textMessage = textMessage;
        this.sound = sound;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }


}