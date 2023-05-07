package com.bigtech.abc.post;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
public class PostFormDto {

    private String subject;

    private String content;

    private Integer likes;

    private LocalDateTime modifiedDate;
}
