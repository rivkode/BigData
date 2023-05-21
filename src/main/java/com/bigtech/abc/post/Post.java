package com.bigtech.abc.post;

import com.bigtech.abc.member.Member;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String subject;

    private String content;

    @ManyToMany
    private Set<Member> voter;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public Post() {

    }

    public Post(Long id, String subject ,String content, Set<Member> voter, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.voter = voter;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }


}
