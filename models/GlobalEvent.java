package com.even.gestion.models;

import java.time.LocalDate;
import java.util.Objects;

public class GlobalEvent {
    private String title;
    private String location;
    private String dateStart;
    private String dateEnd;
    private boolean isLocal; // True if local, false if global
    private String url; // Optional URL for global events

    public GlobalEvent(String title, String location, String dateStart, String dateEnd, boolean isLocal, String url) {
        this.title = title;
        this.location = location;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.isLocal = isLocal;
        this.url = url;
    }

    // Getters and Setters for all fields
    // toString(), equals(), and hashCode() methods here...

    // Getters and Setters


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
