package com.bigtech.abc.post;

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
        Pageable pageable = PageRequest.of(page, 50, Sort.by(sorts));
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

    public void vote(Post post, Member member) {
        post.getVoter().add(member);
        this.jpaPostRepository.save(post);
    }

}
