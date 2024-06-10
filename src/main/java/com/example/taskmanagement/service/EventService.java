package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Event;
import com.example.taskmanagement.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final GoogleCalendarService googleCalendarService;
    private final Map<Long, Event> eventCache = new ConcurrentHashMap<>();

    public EventService(EventRepository eventRepository, GoogleCalendarService googleCalendarService) {
        this.eventRepository = eventRepository;
        this.googleCalendarService = googleCalendarService;
    }

    public Event createEvent(Event event) throws GeneralSecurityException, IOException {
        Event savedEvent = eventRepository.save(event);
        eventCache.put(savedEvent.getId(), savedEvent);
        googleCalendarService.addEventToCalendar(savedEvent);
        return savedEvent;
    }

    public Event getEvent(Long id) {
        return eventCache.getOrDefault(id, eventRepository.findById(id).orElse(null));
    }

    public Event updateEvent(Long id, Event event) {
        event.setId(id);
        Event updatedEvent = eventRepository.save(event);
        eventCache.put(id, updatedEvent);
        return updatedEvent;
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
        eventCache.remove(id);
    }

    public List<Event> getAllEvents() {
        return null;
    }
}

