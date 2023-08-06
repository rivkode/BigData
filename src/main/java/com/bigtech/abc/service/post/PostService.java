package com.bigtech.abc.service.post;

import com.bigtech.abc.common.exception.AppException;
import com.bigtech.abc.common.exception.ErrorCode;
import com.bigtech.abc.domain.like.JPALikeRepository;
import com.bigtech.abc.domain.member.JPAMemberRepository;
import com.bigtech.abc.domain.member.Member;
import com.bigtech.abc.domain.post.JPAPostRepository;
import com.bigtech.abc.domain.post.Post;
import com.bigtech.abc.domain.post.PostRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final JPAPostRepository jpaPostRepository;

    private final JPALikeRepository jpaLikeRepository;

    private final JPAMemberRepository jpaMemberRepository;

    private final PostRepositoryImpl postRepositoryImpl;

    @Transactional
    public void save(String subject, String content, Member member) {
        Post post = new Post();

        String input = "2022-01-01 11:22:33";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdDate = LocalDateTime.parse(input, formatter);

        post.setMember(member);
        post.setSubject(subject);
        post.setContent(content);
        post.setCreatedDate(createdDate);
        post.setModifiedDate(LocalDateTime.now());
        this.jpaPostRepository.save(post);
    }

    public Integer findLastId() {
        return this.jpaPostRepository.LastPostid();
    }

    public Page<Post> getPostList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("modifiedDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.jpaPostRepository.findAll(pageable);
    }

    public Page<Post> getQuerydslPostList(String name, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("modifiedDate")));
        return postRepositoryImpl.findBySubject(name, pageable);
    }


    public List<Post> getPostTimelineList(Long id) {
        List<Long> postIdList = jpaPostRepository.findPostIdsByMemberId(id);
        List<Post> postList = new ArrayList<>();
        for (Long i : postIdList) {
            Post post = jpaPostRepository.findById(i)
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Post가 없습니다"));
            postList.add(post);
        }

        return postList;

    }

    public Post getPost(Long id) {
        Optional<Post> post = this.jpaPostRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new RuntimeException("Data not found");
        }
    }

    public List<Member> getMemberList(Long id) { // post id 기준 memberid list 반환
        List<Member> members = new ArrayList<>();
        System.out.println("members");
        List<Long> memberIdList = jpaLikeRepository.getMemberList(id);
        System.out.println("memberIDList");

        for (Long i : memberIdList) {
            Member member = jpaMemberRepository.findById(i)
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Member가 없습니다"));
            members.add(member);
        }

        return members;
    }

    public List<String> getMemberName(List<Member> members) {
        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            nameList.add(members.get(i).getName());
        }

        return nameList;
    }

    public List<Post> scrollNoOffset(Long postId) {
        return this.jpaPostRepository.NoOffsetPage(postId);
    }
}
