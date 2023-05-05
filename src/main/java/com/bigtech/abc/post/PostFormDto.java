package com.bigtech.abc.post;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class PostFormDto {

    private String content;

    private Integer likes;

    private Timestamp createdDate;

    private Timestamp modifiedDate;
}
