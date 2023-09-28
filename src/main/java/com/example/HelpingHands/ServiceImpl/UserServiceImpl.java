package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Entity.Volunteer;
import com.example.HelpingHands.Repository.OrganizationRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Repository.VolunteerRepository;
import com.example.HelpingHands.Service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final VolunteerRepository volunteerRepository;

    @Override
    public UserEntity createVolunteer(Volunteer volunteer) {
        volunteer.setPassword(passwordEncoder.encode(volunteer.getPassword()));
        return volunteerRepository.save(volunteer);
    }

    @Override
    public UserEntity createOrganization(Organization organization) {
        organization.setPassword(passwordEncoder.encode(organization.getPassword()));
        return userRepository.save(organization);
    }

    @Override
    public UserEntity getUser(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("User not found"));
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("User not found"));
    }

    public List<UserEntity> searchUsers(String keyword, String authenticatedUsername) {
        List<UserEntity> users = userRepository.findByNameContaining(keyword);
        users = users.stream()
                .filter(user -> !user.getName().equals(authenticatedUsername))
                .collect(Collectors.toList());
        return users;
    }

}
