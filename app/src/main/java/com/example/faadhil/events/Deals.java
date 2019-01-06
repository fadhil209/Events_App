package com.example.faadhil.events;

/**
 * Created by Faadhil on 12/26/2018.
 */

public class Deals {
    private String dealId;
    private String dealName;
    private String description;
    private String location;
    private String uri;
    private String link;
    private String startdate;
    private String enddate;
    private String username;

    public Deals() {
    }

    public Deals(String dealId, String dealName, String description, String enddate, String link, String location, String startdate, String uri, String username) {
        this.dealId = dealId;
        this.dealName = dealName;
        this.description = description;
        this.enddate = enddate;
        this.link = link;
        this.location = location;
        this.startdate = startdate;
        this.uri = uri;
        this.username = username;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
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
