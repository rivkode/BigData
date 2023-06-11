package com.bigtech.abc.like;

import com.bigtech.abc.exception.AppException;
import com.bigtech.abc.exception.ErrorCode;
import com.bigtech.abc.member.JPAMemberRepository;
import com.bigtech.abc.member.Member;
import com.bigtech.abc.post.JPAPostRepository;
import com.bigtech.abc.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final JPAMemberRepository jpaMemberRepository;
    private final JPAPostRepository jpaPostRepository;

    @Transactional
    public void insert(LikeRequestDto likeRequestDto) throws Exception {
        Member member = jpaMemberRepository.findById(likeRequestDto.getMemberId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Member가 없습니다"));

        Post post = jpaPostRepository.findById(likeRequestDto.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Post가 없습니다"));

        Like like = Like.createPostMember(member, post);

        likeRepository.save(like);
    }

    @Transactional
    public void cancelLike(LikeRequestDto likeRequestDto) {
        Like like = likeRepository.findByMemberAndPost(likeRequestDto.getMemberId(), likeRequestDto.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Like가 없습니다"));

        like.cancelLike();
    }

}
