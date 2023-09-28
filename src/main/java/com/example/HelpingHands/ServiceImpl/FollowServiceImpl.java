package com.example.HelpingHands.ServiceImpl;

import com.example.HelpingHands.Entity.Follow;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Repository.FollowRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
    public Follow createFollow(UserEntity follower, UserEntity following) {
        Follow follow = new Follow(follower, following);
        return followRepository.save(follow);
    }

    @Override
    public boolean isFollowing(UserEntity follower, UserEntity following) {
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    //get followers for one user
    @Override
    public List<UserEntity> getFollowers(UserEntity user) {
        return userRepository.findByFollowing(user);
    }

    //get following for one user
    @Override
    public List<UserEntity> getFollowing(Long userId) {
        Optional<UserEntity> user=userRepository.findById(userId);
        return userRepository.findByFollowers(user.get());
    }

    @Override
    public void unfollow(UserEntity follower, UserEntity following) {
        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    @Override
    public void deleteFollow(Follow follow) {
            followRepository.delete(follow);
    }
    @Override
    public Follow findFollowByFollowerAndFollowing(UserEntity follower, UserEntity following) {
        return followRepository.findByFollowerAndFollowing(follower, following);
    }


}
