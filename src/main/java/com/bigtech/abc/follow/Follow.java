package com.bigtech.abc.follow;

import com.bigtech.abc.member.Member;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follow")
@Getter
@Setter
@Builder
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private Member follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private Member following;

    private FollowerStatus status;

    public static Follow create(Member member1, Member member2) {
        return Follow.builder()
                .follower(member1)
                .following(member2)
                .build()
                ;
    }
}
