package com.bigtech.abc.follow;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FollowDto {
    @NotNull
    private Long followerId;

    @NotNull
    private Long followingId;
}
