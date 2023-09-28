package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.Entity.Event;
import com.example.HelpingHands.Entity.EventParticipant;
import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.EventRepository;
import com.example.HelpingHands.Repository.OrganizationRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.EventService;
import com.example.HelpingHands.Service.FollowService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final   EventRepository eventRepository;
    private final  OrganizationRepository organizationRepository;
    private final  FollowService followService;

    @Override
    public Event createEvent(Event event , Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("Organization with ID " + organizationId + " not found"));
        event.setOrganization(organization);
        return eventRepository.save(event);
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow();
    }



    @Override
    public Event updateEvent(Long eventId, Event updatedEvent) {
        Event existingEvent = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));;
        if (existingEvent != null) {
            existingEvent.setDescription(updatedEvent.getDescription());
            existingEvent.setEventDate(updatedEvent.getEventDate());
            existingEvent.setLocation(updatedEvent.getLocation());
            return eventRepository.save(existingEvent);
        } else {
            throw new IllegalArgumentException("Event with id " + eventId + " not found");
        }
    }

    @Override
    public void deleteEvent(Long eventId) {
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
        } else {
            throw new IllegalArgumentException("Event with id " + eventId + " not found");
        }
    }

    @Override
    public List<Event> getEventsForFollowed(Long userId) {
        List<UserEntity> followedUsers = followService.getFollowing(userId);
        List<Event> events = new ArrayList<>();

        for (UserEntity user : followedUsers) {
            events.addAll(eventRepository.findByOrganization(user));
        }

        return events;
    }


}