package com.example.HelpingHands.Entity;

import com.example.HelpingHands.Listener.EntityCreatedTimestampListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@EntityListeners(EntityCreatedTimestampListener.class)
@EqualsAndHashCode(exclude = {"notification","follower","following"})
@Data
@NoArgsConstructor
@Entity
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private Date followedAt;

    @JsonBackReference(value = "followers")
    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "id")
    private UserEntity follower;

    @JsonBackReference(value = "following")
    @ManyToOne
    @JoinColumn(name = "following_id", referencedColumnName = "id")
    private UserEntity following;

    @JsonManagedReference(value = "follow-notification")
    @OneToOne(mappedBy = "follow", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Notification notification;


    public Follow(UserEntity follower, UserEntity following) {
        this.follower = follower;
        this.following = following;
    }
}
