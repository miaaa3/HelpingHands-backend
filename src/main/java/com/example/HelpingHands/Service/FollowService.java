package com.example.HelpingHands.Service;

import com.example.HelpingHands.Entity.Follow;
import com.example.HelpingHands.Entity.UserEntity;

import java.util.List;

public interface FollowService {

    public Follow createFollow(UserEntity follower, UserEntity following) ;
    public boolean isFollowing(UserEntity follower, UserEntity following);
    public List<UserEntity> getFollowers(UserEntity user) ;
    public List<UserEntity> getFollowing(Long userId) ;
    public void unfollow(UserEntity follower, UserEntity following) ;

    public void deleteFollow(Follow follow);
    public Follow findFollowByFollowerAndFollowing(UserEntity follower, UserEntity following);
    }
