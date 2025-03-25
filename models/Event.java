package com.even.gestion.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private String location;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    // Add other fields as necessary

    // Relationship to Invitations (One-to-Many)
    @OneToMany(mappedBy = "event")
    private List<Invitation> invitations;

    // Constructors, getters, and setters
    public Event() {
    }

    public Event(String title, String description, LocalDateTime date, LocalDateTime dateEnd, String location, AppUser user) {
        this.title = title;
        this.description = description;
        this.dateStart = date;
        this.location=location;
        this.user=user;
        this.dateEnd=dateEnd;
    }


    public AppUser getUser() {
        return user;
    }

    public void setUser( AppUser user) {
        this.user= user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }

    public List<Invitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<Invitation> invitations) {
        this.invitations = invitations;
    }
}


