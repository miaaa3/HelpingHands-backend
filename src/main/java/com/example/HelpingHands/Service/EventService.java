package com.example.HelpingHands.Service;

import com.example.HelpingHands.Entity.Event;
import com.example.HelpingHands.Entity.EventParticipant;
import com.example.HelpingHands.Entity.UserEntity;

import java.util.List;
import java.util.Set;

public interface EventService {
    Event createEvent(Event event, Long organizationId);

    Event getEventById(Long eventId);

    Event updateEvent(Long eventId, Event event);

    void deleteEvent(Long eventId);

    List<Event> getEventsForFollowed(Long user);
}