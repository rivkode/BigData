//package com.bigtech.abc.data;
//
//import com.bigtech.abc.domain.post.Post;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ListTest {
//
//    @Test
//    void listStringTest() {
//        String a = "a";
//        List<String> stringList = new ArrayList<>();
//
//        for (int i=0; i<500000; i++) {
//            stringList.add(a + i);
//        }
//        System.out.println("stringList Size = " + stringList.size());
//
//        Assertions.assertThat(stringList.size()).isEqualTo(500000);
//    }
//
//    @Test
//    void listPostTest() {
//        String s1 = "2022-01-01 00:00:00.000";
//        String s2 = "2022-12-31 23:59:59.000";
//        Timestamp tp1 = Timestamp.valueOf(s1);
//        Timestamp tp2 = Timestamp.valueOf(s2);
//        Post post = new Post();
//        post.setId(1L);
//        post.setLikes(100);
//        post.setCreatedDate(tp1);
//        post.setModifiedDate(tp2);
//
//        List<Post> postList = new ArrayList<>();
//        for (int i=0; i<500000; i++) {
//            postList.add(post);
//        }
//        System.out.println("postList Size = " + postList.size());
//
//        Assertions.assertThat(postList.size()).isEqualTo(500000);
//
//    }
//}
