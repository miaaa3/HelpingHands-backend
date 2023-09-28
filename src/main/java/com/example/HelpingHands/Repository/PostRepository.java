package com.example.HelpingHands.Repository;

import com.example.HelpingHands.Entity.Comment;
import com.example.HelpingHands.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    @Query(value = "SELECT p.* " +
            "FROM posts p " +
            "INNER JOIN follows f ON p.user_id = f.following_id " +
            "WHERE f.follower_id = :followerId " +
            "ORDER BY p.created_at DESC",
            nativeQuery = true)
    List<Post> findPostsOfFollowedUser(@Param("followerId") Long followerId);

    @Query(value = "SELECT COUNT(*) FROM likes WHERE post_id = :postId",nativeQuery = true)
    int numberOfLikes(@Param("postId") Long postId);

    @Query(value = "SELECT COUNT(*) from comments WHERE post_id= :postId",nativeQuery = true)
    int numberOfComments(@Param("postId") Long postId);
}
