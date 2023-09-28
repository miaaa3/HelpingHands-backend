package com.example.HelpingHands.Entity;

import com.example.HelpingHands.Listener.EntityCreatedTimestampListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@EntityListeners(EntityCreatedTimestampListener.class)
@EqualsAndHashCode(exclude = {"like","follow","comment","user"})
@NoArgsConstructor
@Data
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 30)
    private String message;

    @Column(length=30)
    private String notificationType;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isRead=false;

    @JsonBackReference(value = "like-notification")
    @OneToOne
    @JoinColumn(name = "like_id",referencedColumnName = "id")
    private Like like;

    @JsonBackReference(value = "follow-notification")
    @OneToOne
    @JoinColumn(name = "follow-id",referencedColumnName = "id")
    private Follow follow;

    @JsonBackReference(value = "comment-notification")
    @OneToOne
    @JoinColumn(name = "comment_id" ,referencedColumnName = "id")
    private Comment comment;

    @JsonBackReference(value = "user-notification")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private Long recipient;


}
