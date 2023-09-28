package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Notification;
import com.example.HelpingHands.Entity.UserEntity;
import lombok.Data;

@Data
public class NotificationDTO {
    private Notification notification;
    private UserEntity user;
}
