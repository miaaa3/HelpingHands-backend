package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.Entity.*;
import com.example.HelpingHands.Repository.NotificationRepository;
import com.example.HelpingHands.Service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public Notification markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        return notificationRepository.save(notification);
    }

    @Override
    public void createLikeNotification(UserEntity user, Like like) {
        if (!user.getId().equals(like.getPost().getUser().getId())) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setMessage(" liked your post.");
            notification.setNotificationType("LIKE");
            notification.setLike(like);
            notification.setRecipient(like.getPost().getUser().getId());
           notificationRepository.save(notification);
        }
    }

    @Override
    public void createFollowNotification(UserEntity user, Follow follow) {

    }

    @Override
    public void createCommentNotification(UserEntity user, Comment comment) {
        if (!user.getId().equals(comment.getPost().getUser().getId())) {
            Notification notification = new Notification();
            notification.setUser(user);
            notification.setMessage(" commented on your post.");
            notification.setNotificationType("COMMENT");
            notification.setComment(comment);
            notification.setRecipient(comment.getPost().getUser().getId());
            notificationRepository.save(notification);
        }
    }

    @Override
    public void deleteNotificationByLikeId(Long likeId) {
        notificationRepository.deleteByLikeId(likeId);
    }

    @Override
    public Set<Notification> getAllNotificationsForUser(Long userId) {
        return notificationRepository.findAllByRecipientOrderByCreatedAtDesc(userId);
    }
}
