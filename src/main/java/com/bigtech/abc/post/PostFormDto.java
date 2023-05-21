package com.bigtech.abc.post;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class PostFormDto {

    @NotEmpty(message = "제목은 필수입니다")
    private String subject;

    @NotEmpty(message = "내용은 필수입니다")
    private String content;

    private Integer likes;

    private LocalDateTime modifiedDate;
}
