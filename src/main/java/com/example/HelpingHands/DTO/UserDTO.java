package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Notification;
import com.example.HelpingHands.Entity.UserEntity;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private UserEntity user;
    private int numberOfFollowing;
    private int numberOfFollowers;
    private Set<NotificationDTO> notifications;


}
