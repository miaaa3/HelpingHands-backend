package com.example.HelpingHands.Controller;

import com.example.HelpingHands.Entity.Event;
import com.example.HelpingHands.Entity.EventParticipant;
import com.example.HelpingHands.Service.EventService;
import com.example.HelpingHands.Service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/events")
public class EventController {
    private final EventService eventService;
    private final JWTService jwtService;

    @PostMapping("/createEvent")
    public ResponseEntity<Event> createEvent(@RequestParam Long organizationId, @RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event,organizationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @GetMapping("/getEventById")
    public ResponseEntity<Event> getEventById(@RequestParam Long eventId) {
        Event event = eventService.getEventById(eventId);
        return ResponseEntity.ok(event);
    }



    @PutMapping("/updateEvent")
    public ResponseEntity<Event> updateEvent(@RequestParam Long eventId, @RequestBody Event event) {
        Event updatedEvent = eventService.updateEvent(eventId, event);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/deleteEvent")
    public ResponseEntity<Void> deleteEvent(@RequestParam Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/followed")
    public ResponseEntity<List<Event>> getEventsForFollowedUsers(@RequestParam Long userId) {
        List<Event> events = eventService.getEventsForFollowed(userId);
        return ResponseEntity.ok(events);
    }
}
