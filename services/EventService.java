package com.even.gestion.services;

import com.even.gestion.models.AppUser;
import com.even.gestion.models.Event;
import com.even.gestion.models.Invitation;
import com.even.gestion.repositories.AppUserRepository;
import com.even.gestion.repositories.EventRepository;
import com.even.gestion.repositories.InvitationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private AppUserRepository appUserRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public void deleteEvent(Long id, String email) {
        Event event = eventRepository.findById(id).orElseThrow();
        if (event.getUser().getEmail().equals(email)) {
            eventRepository.delete(event);
        } else {
            throw new SecurityException("Vous n'avez pas l'autorisation de supprimer cet événement.");
        }
    }

    public void addEvent(Event event) {
        try {
            eventRepository.save(event);
            logger.info("Event added successfully: {}", event.getTitle());
        } catch (Exception e) {
            logger.error("Error saving event: {}", event.getTitle(), e);
        }
    }
    public List<Event> getEventsByUser(String email) {
        AppUser user=appUserRepository.findByEmail(email);
        return eventRepository.findByUser(user);
    }
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow();
    }

    public void sendInvitation(Long eventId, String guestEmail) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        Invitation invitation = new Invitation(guestEmail, event);
        invitationRepository.save(invitation);

        // Send Email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(guestEmail);
        message.setSubject("Invitation to: " + event.getTitle());
        message.setText("You are invited to the event: " + event.getDescription() + " start on  " + event.getDateStart() + "end on" + event.getDateEnd() + " in " + event.getLocation());
        mailSender.send(message);
    }
    public Event updateEvent(Long id, Event updatedEvent) {
        Event existingEvent = getEventById(id);

        // Update fields
        existingEvent.setTitle(updatedEvent.getTitle());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setDateStart(updatedEvent.getDateStart());
        existingEvent.setDateEnd(updatedEvent.getDateEnd());
        existingEvent.setLocation(updatedEvent.getLocation());

        return eventRepository.save(existingEvent);
    }
    public List<Event> getEventsByCity(String city) {
        return eventRepository.findByLocation(city); // This will fetch events based on the city
    }
}

