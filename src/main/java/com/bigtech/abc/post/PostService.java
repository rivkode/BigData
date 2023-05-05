package com.bigtech.abc.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class PostService {
    private final JPAPostRepository jpaPostRepository;

    public void save(String content, Integer likes) {
        Post post = new Post();
        long currentTimeMillis = System.currentTimeMillis();
        Timestamp currentTimestamp = new Timestamp(currentTimeMillis);

        String s1 = "2022-01-01 00:00:00.000";
        Timestamp createdDate = Timestamp.valueOf(s1);

        post.setContent(content);
        post.setLikes(likes);
        post.setCreatedDate(createdDate);
        post.setModifiedDate(currentTimestamp);
        this.jpaPostRepository.save(post);
    }


}
