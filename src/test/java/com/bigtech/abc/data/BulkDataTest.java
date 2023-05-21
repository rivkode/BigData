package com.bigtech.abc.data;

import com.bigtech.abc.post.JPAPostRepository;
import com.bigtech.abc.post.Post;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SpringBootTest
public class BulkDataTest {

    @Autowired
    private JPAPostRepository jpaPostRepository;


    @Test
    void bulkPostList() {
        List<Post> postList = new ArrayList<>();
        Faker faker = new Faker(new Locale("ko"));
        String input = "2022-01-01 11:22:33";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdDate = LocalDateTime.parse(input, formatter);

        for (int i=0; i < 1000000; i++) {
            Post post = new Post();
            post.setSubject("Poster : " + faker.name().fullName());
            post.setContent("Poster ip 주소 : " + faker.internet().ipV4Address());
            post.setCreatedDate(createdDate);
            post.setModifiedDate(LocalDateTime.now());
            postList.add(post);
        }
        System.out.println("postList = " + postList.size());

        int batchSize = 1000; // 배치 단위 크기
        int listSize = postList.size();; // 전체 데이터 크기
        for (int i=0; i<listSize; i += batchSize) {
            int endIndex = i + batchSize; // 최소 index를 구한다 save를 할때 batch 단위만큼 subList로 저장하기 위해서
            List<Post> batchList = postList.subList(i, endIndex);
            jpaPostRepository.saveAll(batchList);
            jpaPostRepository.flush();
        }

    }

}
