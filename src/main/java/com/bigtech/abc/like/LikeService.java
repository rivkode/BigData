package com.bigtech.abc.like;

import com.bigtech.abc.exception.AppException;
import com.bigtech.abc.exception.ErrorCode;
import com.bigtech.abc.member.JPAMemberRepository;
import com.bigtech.abc.member.Member;
import com.bigtech.abc.post.JPAPostRepository;
import com.bigtech.abc.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final JPALikeRepository JPALikeRepository;
    private final JPAMemberRepository jpaMemberRepository;
    private final JPAPostRepository jpaPostRepository;

    @Transactional
    public void insert(LikeRequestDto likeRequestDto) throws Exception {
        Member member = jpaMemberRepository.findById(likeRequestDto.getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Member가 없습니다"));

        Post post = jpaPostRepository.findById(likeRequestDto.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Post가 없습니다"));

        // 중복 제거
        /**
         * 만약 postId, memberId가 동일한 like가 존재하면
         * 수행하지 않고 이미 좋아요한 상태를 반환
         * 만약 존재하지 않는다면
         * createPpso
         */

//        boolean b = JPALikeRepository.existsByMemberIdAndPostId(member.getId(), post.getId());
//        if (b) {
//            // 좋아요 수행 하지 않음
//        } else {
//            Like like = Like.create(member, post);
//
//            JPALikeRepository.save(like);
//        }

        likeSave(member, post);
        Thread.sleep(10000);
        System.out.println("hi");
    }

//    @Transactional
    public synchronized void likeSave(Member member, Post post) {
        var b = JPALikeRepository.findByMemberIdAndPostId(member.getId(), post.getId());
        if (b.isPresent()) {
            // 좋아요 수행 하지 않음
        } else {
            Like like = Like.create(member, post);

            JPALikeRepository.saveAndFlush(like);
        }
    }

    @Transactional
    public void cancelLike(LikeRequestDto likeRequestDto) {
        Like like = JPALikeRepository.findByMemberIdAndPostId(likeRequestDto.getMemberId(), likeRequestDto.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Like가 없습니다"));

        System.out.println("Like 탐색");
        like.cancelLike();
        JPALikeRepository.deleteById(like.getId());
    }

}
