package com.bigtech.abc.like;

import com.bigtech.abc.common.exception.AppException;
import com.bigtech.abc.common.exception.ErrorCode;
import com.bigtech.abc.domain.like.JPALikeRepository;
import com.bigtech.abc.domain.like.Like;
import com.bigtech.abc.domain.member.JPAMemberRepository;
import com.bigtech.abc.domain.member.Member;
import com.bigtech.abc.service.member.MemberService;
import com.bigtech.abc.domain.post.JPAPostRepository;
import com.bigtech.abc.domain.post.Post;
import com.bigtech.abc.service.like.LikeService;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class LikeTest {
    @Autowired
    private JPAPostRepository jpaPostRepository;

    @Autowired
    private JPAMemberRepository jpaMemberRepository;

    @Autowired
    private JPALikeRepository jpaLikeRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    LikeService likeService;

    @Test
    @DisplayName("좋아요 동시성 테스트")
    void test_like_concurrency() {
        //given

        int numberOfThreads = 2;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        Post post = jpaPostRepository.findById(7L)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Post가 없습니다"));

        Member member1 = jpaMemberRepository.findById(1L)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Member가 없습니다"));

        Member member2 = jpaMemberRepository.findById(2L)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Member가 없습니다"));

        //when
        int beforepostCount = jpaLikeRepository.getLikeCount();

        Like like1 = Like.create(member1, post);
        Like like2 = Like.create(member2, post);

        jpaLikeRepository.save(like1);
        jpaLikeRepository.save(like2);

        //then
        int afterpostCount = jpaLikeRepository.getLikeCount();

        Assert.assertEquals("Like객체 1개 생성", 1, afterpostCount - beforepostCount);
    }
}
