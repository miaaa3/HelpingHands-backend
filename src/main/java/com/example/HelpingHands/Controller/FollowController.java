package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.FollowDTO;
import com.example.HelpingHands.Entity.Follow;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.FollowService;
import com.example.HelpingHands.Service.NotificationService;
import com.example.HelpingHands.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/follow")
public class FollowController {
    private final FollowService followService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @PostMapping("/follow")
    public ResponseEntity<?> toggleFollowUser(@RequestParam Long userId, Principal principal) {
        UserEntity follower = userService.findByEmail(principal.getName());
        UserEntity following = userService.getUser(userId);
        FollowDTO followDTO=new FollowDTO();

        if (follower == null || following == null) {
            return ResponseEntity.notFound().build();
        }

        Follow follow = followService.findFollowByFollowerAndFollowing(follower, following);

        if (follow != null) {
            followService.deleteFollow(follow);
            followDTO.setFollow(follow);
            followDTO.setFollowed(false);
            return ResponseEntity.ok(followDTO);
        } else {
            followDTO.setFollow(followService.createFollow(follower, following));
            followDTO.setFollowed(true);
            return ResponseEntity.ok(followDTO);
        }
    }


    @GetMapping("/getFollowers")
    public ResponseEntity<List<UserEntity>> getFollowers(Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<UserEntity> followers = followService.getFollowers(user);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/getFollowing")
    public ResponseEntity<?> getFollowing(Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        List<?> usersFollowing = userRepository.findUsersFollowingById(user.getId());

        if (usersFollowing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(usersFollowing);
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<?> unfollowUser(@RequestParam Long userId, Principal principal) {
        // Get the currently logged-in user
        UserEntity follower = userService.findByEmail(principal.getName());

        // Get the user to unfollow
        UserEntity following = userService.getUser(userId);

        if (follower == null || following == null) {
            return ResponseEntity.notFound().build();
        }

        // Unfollow the user
        followService.unfollow(follower, following);

        return ResponseEntity.ok().build();
    }
}
