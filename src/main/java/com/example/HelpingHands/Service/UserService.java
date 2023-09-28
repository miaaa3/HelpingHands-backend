package com.example.HelpingHands.Service;

import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Entity.Volunteer;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserEntity createVolunteer(Volunteer volunteer);

    UserEntity createOrganization(Organization organization);

    UserEntity getUser(Long id);

    UserEntity findByEmail(String email);
    List<UserEntity> searchUsers(String keyword, String authenticatedUsername);
}
