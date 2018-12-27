package com.example.faadhil.events;

/**
 * Created by Faadhil on 12/26/2018.
 */

public class Deals {
    private String eventId;
    private String eventName;
    private String description;
    private String location;
    private String uri;
    private String startdate;
    private String enddate;
    private String username;

    public Deals() {
    }

    public Deals(String description, String enddate, String eventId, String eventName, String location, String startdate, String uri, String username) {
        this.description = description;
        this.enddate = enddate;
        this.eventId = eventId;
        this.eventName = eventName;
        this.location = location;
        this.startdate = startdate;
        this.uri = uri;
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
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

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
