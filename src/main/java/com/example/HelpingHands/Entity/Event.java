package com.example.HelpingHands.Entity;

import com.example.HelpingHands.Listener.EntityCreatedTimestampListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@EntityListeners(EntityCreatedTimestampListener.class)
@EqualsAndHashCode(exclude = {"organization","participants"})
@Data
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 50)
    private String location;

    @JsonBackReference(value = "organization")
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @JsonManagedReference(value = "participants")
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventParticipant> participants ;


    public Event( String description, LocalDateTime eventDate, String location) {
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
    }
    public void addParticipant(EventParticipant participant) {
        participants.add(participant);
        participant.setEvent(this);
    }

}
