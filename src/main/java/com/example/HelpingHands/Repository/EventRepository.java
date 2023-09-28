package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Event;
import com.example.HelpingHands.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    Set<Event> findByOrganization(UserEntity user);
}
