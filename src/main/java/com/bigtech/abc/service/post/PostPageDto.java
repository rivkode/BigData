package com.bigtech.abc.service.post;

import com.bigtech.abc.domain.member.Member;
import com.querydsl.core.annotations.QueryEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@QueryEntity
@Getter
public class PostPageDto {
    private Long postId;
    private String subject;
    private String content;
    private Member member;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
