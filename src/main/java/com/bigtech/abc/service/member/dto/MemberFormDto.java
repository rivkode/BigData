package com.bigtech.abc.service.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberFormDto {
    private String name;

    private String email;

    private String birth;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
}
