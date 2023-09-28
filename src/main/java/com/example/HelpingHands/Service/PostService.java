package com.example.HelpingHands.Service;

import com.example.HelpingHands.Entity.MediaPost;
import com.example.HelpingHands.Entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface PostService {
    Post createPost(String content,Long userId);
    Post createPost(String content, Long userId,Set<MultipartFile> files) throws IOException;
    Post getPostById(Long id);
    List<Post> getAllPosts();
    List<MediaPost> getMediaForPost(Long postId);
    Post updatePost(Long id, Post updatedPost);
    void deletePost(Long id);
     void deleteMediaPostsByPostId(Long postId);

     List<Post> getPostsOfFollowedUsers(Long userId);
}
