package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByNameContaining(String name);
    Optional<UserEntity> findByName(String name);
    List<UserEntity> findByFollowing(UserEntity following);
    @Query("SELECT u " +
            "FROM UserEntity u JOIN Follow f ON u.id = f.following.id " +
            "WHERE f.follower.id = :userId AND u.id <> :userId")
    List<?> findUsersFollowingById(@Param("userId") Long userId);

    @Query(value = "select count(*) from follows f , users u where u.id=f.follower_id and following_id= :userId and follower_id <> :userId", nativeQuery = true)
    int numberOfFollowers(@Param("userId") Long userId);

    @Query(value = "select count(*) from follows f , users u where u.id=f.following_id and follower_id= :userId and  following_id <> :userId", nativeQuery = true)
    int numberOfFollowing(@Param("userId") Long userId);

    List<UserEntity> findByFollowers(UserEntity follower);
}
