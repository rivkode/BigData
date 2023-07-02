package com.bigtech.abc.post;

import com.bigtech.abc.exception.AppException;
import com.bigtech.abc.exception.ErrorCode;
import com.bigtech.abc.like.JPALikeRepository;
import com.bigtech.abc.member.JPAMemberRepository;
import com.bigtech.abc.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

    public void save(String subject, String content) {
        Post post = new Post();

        String input = "2022-01-01 11:22:33";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdDate = LocalDateTime.parse(input, formatter);

        post.setSubject(subject);
        post.setContent(content);
        post.setCreatedDate(createdDate);
        post.setModifiedDate(LocalDateTime.now());
        this.jpaPostRepository.save(post);
    }

    public Integer findLastId() {
        return this.jpaPostRepository.LastPostid();
    }

    public Page<Post> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("modifiedDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.jpaPostRepository.findAll(pageable);
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

}
