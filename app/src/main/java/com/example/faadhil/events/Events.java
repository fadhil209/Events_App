package com.example.faadhil.events;

/**
 * Created by Faadhil on 12/3/2018.
 */

public class Events {
    private String eventId;
    private String eventName;
    private String description;
    private String location;
    private String uri;
    private String date;
    private String time;
    private String category;
    private String dealsId;


    public Events(String dealsId, String category, String date, String description, String eventId, String eventName, String location, String time, String uri) {
        this.dealsId = dealsId;
        this.category = category;
        this.date = date;
        this.description = description;
        this.eventId = eventId;
        this.eventName = eventName;
        this.location = location;
        this.time = time;
        this.uri = uri;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
