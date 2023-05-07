//package com.bigtech.abc.post;
//
//import groovy.util.logging.Slf4j;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.NoSuchElementException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class PostRepositoryTest {
//
//    PostRepository postRepository = new PostRepository();
//
//    @Test
//    void crud() throws SQLException {
//        String s1 = "2022-01-01 00:00:00.000";
//        String s2 = "2022-12-31 23:59:59.000";
//        Timestamp tp1 = Timestamp.valueOf(s1);
//        Timestamp tp2 = Timestamp.valueOf(s2);
//
//        //save
////        Post post = new Post(10L, "첫줄입니다", 10, tp1, tp2);
////        postRepository.save(post);
//
//        //findById
//        Post findPost = postRepository.findById(post.getId());
//        Assertions.assertThat(findPost).isEqualTo(post);
//
//        //update content: 첫줄입니다 -> 둘째줄입니다
//        postRepository.update(post.getId(), "둘째줄입니다");
//        Post updatedPost = postRepository.findById(post.getId());
//        Assertions.assertThat(updatedPost.getContent()).isEqualTo("둘째줄입니다");
//
//        // delete
//        postRepository.delete(post.getId());
//        Assertions.assertThatThrownBy(() -> postRepository.findById(post.getId()))
//                .isInstanceOf(NoSuchElementException.class);
//
//    }
//}