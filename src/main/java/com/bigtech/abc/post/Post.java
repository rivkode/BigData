package com.bigtech.abc.post;

import com.bigtech.abc.like.Like;
import com.bigtech.abc.member.Member;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likeList = new ArrayList<>();

    public Post() {

    }

    public Post(Long id, String subject ,String content, Member member, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.subject = subject;
        this.member = member;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }


}
