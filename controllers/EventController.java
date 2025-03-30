package com.even.gestion.controllers;

import com.even.gestion.models.AppUser;
import com.even.gestion.models.Event;
import com.even.gestion.models.GlobalEvent;
import com.even.gestion.repositories.AppUserRepository;
import com.even.gestion.repositories.EventRepository;
import com.even.gestion.services.EventService;
import com.even.gestion.services.TicketmasterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private TicketmasterService openEventService;
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
    public String searchEvents(@RequestParam(required = false) String city, Model model) {
        if (city == null || city.trim().isEmpty()) {
            return "redirect:/search";  // Handle case where city is empty or null
        }

        try {
            // Événements locaux depuis la base de données
            List<Event> localEvents = eventService.getEventsByCity(city);
            // Événements globaux depuis Open Event API
            List<GlobalEvent> globalEvents = openEventService.getEventsByCity(city);

            model.addAttribute("city", city);
            model.addAttribute("localEvents", localEvents);
            model.addAttribute("globalEvents", globalEvents);

            // Add flags if no events found
            model.addAttribute("noLocalEvents", localEvents.isEmpty());
            model.addAttribute("noGlobalEvents", globalEvents.isEmpty());

            return "results"; // Affiche results.html
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching events. Please try again later.");
            return "results"; // Show results page with error message
        }
    }


}

