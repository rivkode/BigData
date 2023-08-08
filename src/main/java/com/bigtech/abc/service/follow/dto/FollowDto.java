package com.bigtech.abc.service.follow.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FollowDto {
    private Long followerId;

    private Long followingId;
}
