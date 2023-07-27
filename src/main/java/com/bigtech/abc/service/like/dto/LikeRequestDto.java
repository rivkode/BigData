package com.bigtech.abc.service.like.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LikeRequestDto {
    private Long memberId;
    private Long postId;

    public LikeRequestDto(Long memberId, Long postId) {
        this.memberId = memberId;
        this.postId = postId;
    }
}
