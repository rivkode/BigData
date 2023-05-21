package com.bigtech.abc.member;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberCreateForm {
    private String name;
    private String password1;
    private String password2;
    private String email;
    private String birth;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
}
