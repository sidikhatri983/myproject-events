package com.even.gestion.controllers;

import com.even.gestion.models.AppUser;
import com.even.gestion.models.Event;
import com.even.gestion.models.GlobalEvent;
import com.even.gestion.repositories.AppUserRepository;
import com.even.gestion.repositories.EventRepository;
import com.even.gestion.services.EventService;
import com.even.gestion.services.GlobalEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    private EventService eventService;

    private AppUser appUser ;

    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    GlobalEventService globalEventService;
    @Autowired
    EventRepository eventRepository;
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @GetMapping("/events")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        List<Event> events = eventService.getEventsByUser(email);
        if (events.isEmpty()) {
            logger.info("No events found.");
        } else {
            logger.info("Found {} events.", events.size());
        }
        model.addAttribute("events", events);
        return "index";
    }


    @PostMapping("/events/add")
    public String addEvent(@RequestParam String title,
                           @RequestParam String description,
                           @RequestParam LocalDateTime date,
                           @RequestParam String location,
                           @RequestParam LocalDateTime dateEnd) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        AppUser user = userRepository.findByEmail(email);
        Event event = new Event(title, description, date, dateEnd, location,user);
        eventService.addEvent(event);
        return "redirect:/events";
    }

    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        eventService.deleteEvent(id, username);
        return "redirect:/events";
    }

    @GetMapping("/events/invite/{id}")
    public String showInvitePage(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        return "invite";
    }

    @PostMapping("/events/invite/{id}")
    public String inviteGuest(@PathVariable Long id, @RequestParam String guestEmail) {
        eventService.sendInvitation(id, guestEmail);
        return "redirect:/events";
    }
    @GetMapping("/events/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id);
        model.addAttribute("event", event);
        return "update"; // returns update.html
    }

    @PostMapping("/events/update/{id}")
    public String updateEvent(@PathVariable Long id, @ModelAttribute("event") Event event) {
        eventService.updateEvent(id, event);
        return "redirect:/events";
    }
    @GetMapping("/search")
    public String searchEvents(@RequestParam String city, Model model) {
        // Fetch local events based on city
        List<Event> localEvents = eventService.getEventsByCity(city);

        // Fetch global events based on city (Eventbrite API)
        List<GlobalEvent> globalEvents = globalEventService.getEventsByCity(city);

        model.addAttribute("city", city);
        model.addAttribute("localEvents", localEvents);
        model.addAttribute("globalEvents", globalEvents);

        return "results"; // Display results.html
    }
}

