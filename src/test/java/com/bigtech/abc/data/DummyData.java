package com.bigtech.abc.data;

import com.bigtech.abc.post.Post;
import com.bigtech.abc.post.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DummyData {

    PostRepository postRepository = new PostRepository();

    @Test
    void makeData() throws SQLException {
        for (int i = 2500000; i <= 3000000; i++) {
            Long id = (long) i;
            String content = "내용 없음";

            String s1 = "2022-01-01 00:00:00.000";
            String s2 = "2022-12-31 23:59:59.000";
            Timestamp tp1 = Timestamp.valueOf(s1);
            Timestamp tp2 = Timestamp.valueOf(s2);

            //save
            Post post = new Post(id, content, 10, tp1, tp2);
            this.postRepository.save(post);
        }

        Post getPost = postRepository.findById(1000000L);

        // ID 값 확인
        Assertions.assertThat(getPost.getId()).isEqualTo(1000000L);

        // 마지막 값 확인
        String sql = "SELECT * FROM post ORDER BY id DESC LIMIT 10";
        List<Post> postList = postRepository.findPostByPage(sql);
        for (Post post : postList) {
            System.out.println("post = " + post);
        }
    }

    @Test
    void makeDataList() throws SQLException {

        List<Post> postList = new ArrayList<>();

        for (int i = 1668630; i <= 2000000; i++) {
            Long id = (long) i;
            String content = "내용 없음";

            String s1 = "2022-01-01 00:00:00.000";
            String s2 = "2022-12-31 23:59:59.000";
            Timestamp tp1 = Timestamp.valueOf(s1);
            Timestamp tp2 = Timestamp.valueOf(s2);

            //save
            Post post = new Post(id, content, 10, tp1, tp2);
            postList.add(post);
        }

        // batch로 한 번에 저장하기
        this.postRepository.saveAll(postList);

        Post getPost = postRepository.findById(2000000L);

        // ID 값 확인
        Assertions.assertThat(getPost.getId()).isEqualTo(2000000L);

        // 마지막 값 확인
        String sql = "SELECT * FROM post ORDER BY id DESC LIMIT 10";
        List<Post> posts = postRepository.findPostByPage(sql);
        for (Post post : posts) {
            System.out.println("post = " + post);
        }
    }
}
