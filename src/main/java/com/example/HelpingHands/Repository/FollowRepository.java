package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Follow;
import com.example.HelpingHands.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    boolean existsByFollowerAndFollowing(UserEntity follower, UserEntity following);
    void deleteByFollowerAndFollowing(UserEntity follower, UserEntity following);

    Follow findByFollowerAndFollowing(UserEntity follower, UserEntity following);
}
