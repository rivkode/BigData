package com.bigtech.abc.service.member.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class MemberCreateForm {
    @Size(min = 3, max = 25)
    @NotEmpty(message = "사용자 ID는 필수항목입니다")
    private String name;

    @NotEmpty(message = "비밀번호는 필수항목입니다")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다")
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다")
    @Email
    private String email;

    private String birth;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;
}
