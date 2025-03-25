package com.even.gestion.models;

import jakarta.persistence.*;


@Entity
@Table(name = "invitation")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String guestEmail;

    // Relationship to Event (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    // Constructors, getters, and setters
    public Invitation() {
    }

    public Invitation(String guestEmail, Event event) {
        this.guestEmail = guestEmail;
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuestEmail() {
        return guestEmail;
    }

    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}

