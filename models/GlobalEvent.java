package com.even.gestion.models;

public class GlobalEvent {
    private String title;
    private String location;
    private String dateStart;
    private String dateEnd;
    private boolean isLocal; // True if local, false if global

    public GlobalEvent(String title, String location, String dateStart, String dateEnd, boolean isLocal) {
        this.title = title;
        this.location = location;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.isLocal = isLocal;
    }

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
}
