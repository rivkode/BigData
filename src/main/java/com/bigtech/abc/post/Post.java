package com.bigtech.abc.post;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String subject;

    private String content;

    private Integer likes;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public Post() {

    }

    public Post(Long id, String subject ,String content, Integer likes, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.likes = likes;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }


}
