package com.bigtech.abc.post;

import com.bigtech.abc.domain.member.Member;
import com.bigtech.abc.domain.post.JPAPostRepository;
import com.bigtech.abc.domain.post.Post;
import com.bigtech.abc.domain.post.PostRepositoryImpl;
import groovy.util.logging.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
public class PostServiceTest {

    @Autowired
    private JPAPostRepository jpaPostRepository;

    @Autowired
    private PostRepositoryImpl postRepository;


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void querydsl_custom() {
        //given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String input = "2022-01-01 11:22:33";
        LocalDateTime createdDate = LocalDateTime.parse(input, formatter);
        Member member = new Member(1L, "이종훈", "password", "rivs@kakao.com", createdDate, createdDate, createdDate);
        Post post = new Post(1L, "질문" ,"내용", member, createdDate, createdDate);
        Post post1 = new Post(2L, "질문" ,"내용", member, createdDate, createdDate);
        Post post2 = new Post(3L, "빈칸" ,"내용", member, createdDate, createdDate);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("modifiedDate")));
        jpaPostRepository.save(post);
        jpaPostRepository.save(post1);
        jpaPostRepository.save(post2);
        //when
        Page<Post> postPage =  postRepository.findBySubject("질문", pageable);
        List<Post> postList = new ArrayList<>();
        postList.add(post);

        //then
        Assertions.assertThat(postPage.getContent()).hasSize(2);
//        Assertions.assertThat(postPage.get().collect(Collectors.toList())).isEqualTo(postList);

        logger.info("리스트의 사이즈는 {} 입니다", postPage.toList().size());
    }
}
