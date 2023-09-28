package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    void deleteByLikeId(Long likeId);

    @Query(value = "select  * from notifications where recipient = :userId order by created_at desc",nativeQuery = true)
    Set<Notification> findAllByRecipientOrderByCreatedAtDesc(Long userId);
}
