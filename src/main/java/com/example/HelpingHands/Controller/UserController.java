package com.example.HelpingHands.Controller;

import com.example.HelpingHands.DTO.NotificationDTO;
import com.example.HelpingHands.DTO.SearchResultWithFollowStatusDTO;
import com.example.HelpingHands.DTO.UserDTO;
import com.example.HelpingHands.Entity.Notification;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.NotificationRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.FollowService;
import com.example.HelpingHands.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("api/users")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final FollowService followService;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        UserDTO userDTO = new UserDTO();
        userDTO.setUser(user);
        userDTO.setNumberOfFollowers(user.getFollowers().size() - 1);
        userDTO.setNumberOfFollowing(user.getFollowing().size() - 1);

        Set<Notification> notifications = notificationRepository.findAllByRecipientOrderByCreatedAtDesc(user.getId());
        Set<NotificationDTO> notificationDTOs = new HashSet<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setNotification(notification);
            notificationDTO.setUser(notification.getUser());

            notificationDTOs.add(notificationDTO);
        }

        userDTO.setNotifications(notificationDTOs);

        return ResponseEntity.ok(userDTO);
    }


    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String keyword, Principal principal) {
        UserEntity user = userService.findByEmail(principal.getName());
        List<UserEntity> searchResults = userService.searchUsers(keyword,user.getName());
        List<SearchResultWithFollowStatusDTO> resultsWithFollowStatus = new ArrayList<>();

        for (UserEntity searchResult : searchResults) {
            boolean isFollowing = followService.isFollowing(user, searchResult);

            SearchResultWithFollowStatusDTO resultDTO = new SearchResultWithFollowStatusDTO();
            resultDTO.setUser(searchResult);
            resultDTO.setFollowed(isFollowing);

            resultsWithFollowStatus.add(resultDTO);
        }
        return ResponseEntity.ok(resultsWithFollowStatus);
    }

}
