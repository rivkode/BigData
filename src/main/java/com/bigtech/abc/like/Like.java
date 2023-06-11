package com.bigtech.abc.like;

import com.bigtech.abc.member.Member;
import com.bigtech.abc.post.Post;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Entity
@Table(name = "likes")
@Getter @Setter
@Builder
public class Like {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Post post;

    @Column(name = "like_status")
    @Enumerated(EnumType.STRING)
    private LikeStatus status; // 좋아요 상태 [LIKE, UNLIKE]

    public static Like createPostMember(Member member, Post post) {
        return Like.builder()
                .member(member)
                .post(post)
                .status(LikeStatus.LIKE)
                .build();
    }

    public void cancelLike() {
        if (getStatus() == LikeStatus.UNLIKE) {
            throw new IllegalStateException("이미 좋아요 취소 상태입니다");
        } else {
            System.out.println("CANCEL");
            this.setStatus(LikeStatus.UNLIKE);
        }
    }
}
