package com.EventApplication.EventApplication.controller;

import com.EventApplication.EventApplication.model.Event;
import com.EventApplication.EventApplication.repositry.EventRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = {
        "http://localhost:4200", //  lokala Angular
        "https://seashell-app-tacu5.ondigitalocean.app" //  deployade Angular-app
})
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // hämta alla events
    @GetMapping
    public List<Event> getAllEvent() {
        return eventRepository.findAll();
    }

    // Hämta event via id
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id){

        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event with id " + id + " not founded"));
    }


}
