package com.example.HelpingHands.Service;

import com.example.HelpingHands.Entity.*;

import java.util.List;
import java.util.Set;


public interface NotificationService {
    Notification markNotificationAsRead(Long notificationId);
    void createCommentNotification(UserEntity user, Comment comment);
    void createLikeNotification(UserEntity user, Like like);
    void createFollowNotification(UserEntity user, Follow follow);
    void deleteNotificationByLikeId(Long notificationId);
    public Set<Notification> getAllNotificationsForUser(Long userId);
}
