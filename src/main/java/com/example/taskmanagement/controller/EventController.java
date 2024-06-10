package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Event;
import com.example.taskmanagement.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/")
    public String getAllEvents(Model model) {
        List<Event> events = eventService.getAllEvents();
        model.addAttribute("events", events);
        return "event-list";
    }

    @GetMapping("/add")
    public String showAddEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "event-form";
    }

    @PostMapping("/add")
    public String addEvent(@ModelAttribute("event") Event event) throws GeneralSecurityException, IOException {
        eventService.createEvent(event);
        return "redirect:/events/";
    }

    @GetMapping("/edit/{id}")
    public String showEditEventForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEvent(id);
        model.addAttribute("event", event);
        return "event-form";
    }

    @PostMapping("/edit/{id}")
    public String editEvent(@PathVariable Long id, @ModelAttribute("event") Event event) {
        eventService.updateEvent(id, event);
        return "redirect:/events/";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/events/";
    }
}
