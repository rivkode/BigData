package com.bigtech.abc.post;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String content;

    private Integer likes;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    public Post() {

    }

    public Post(Long id, String content, Integer likes, Timestamp createdDate, Timestamp modifiedDate) {
        this.id = id;
        this.content = content;
        this.likes = likes;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }


}
