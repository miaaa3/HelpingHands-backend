package com.example.HelpingHands.DTO;

import com.example.HelpingHands.Entity.Follow;
import lombok.Data;

@Data
public class FollowDTO {
    private Follow follow;
    private boolean isFollowed;
}
