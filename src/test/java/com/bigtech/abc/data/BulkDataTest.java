package com.bigtech.abc.data;

import com.bigtech.abc.exception.AppException;
import com.bigtech.abc.exception.ErrorCode;
import com.bigtech.abc.like.Like;
import com.bigtech.abc.like.JPALikeRepository;
import com.bigtech.abc.like.LikeService;
import com.bigtech.abc.member.JPAMemberRepository;
import com.bigtech.abc.member.Member;
import com.bigtech.abc.member.MemberService;
import com.bigtech.abc.post.JPAPostRepository;
import com.bigtech.abc.post.Post;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@SpringBootTest
public class BulkDataTest {

    @Autowired
    private JPAPostRepository jpaPostRepository;

    @Autowired
    private JPAMemberRepository jpaMemberRepository;

    @Autowired
    private JPALikeRepository jpaLikeRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    LikeService likeService;


    @Test
    @DisplayName("Post객체 100만개 생성")
    void bulkPostList() {
        List<Post> postList = new ArrayList<>();
        Faker faker = new Faker(new Locale("ko"));
        String input = "2022-01-01 11:22:33";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdDate = LocalDateTime.parse(input, formatter);

        for (int i = 0; i < 1000000; i++) {
            Post post = new Post();
            post.setSubject(faker.address().streetName() + faker.address().streetName() + faker.address().streetName() + faker.address().streetName());
            post.setContent("Poster ip 주소 : " + faker.internet().ipV4Address());
            post.setCreatedDate(createdDate);
            post.setModifiedDate(LocalDateTime.now());
            postList.add(post);
        }
        System.out.println("postList = " + postList.size());

        int batchSize = 1000; // 배치 단위 크기
        int listSize = postList.size();
        ; // 전체 데이터 크기

        int beforepostCount = jpaPostRepository.getPostCount();

        for (int i = 0; i < listSize; i += batchSize) {
            int endIndex = i + batchSize; // 최소 index를 구한다 save를 할때 batch 단위만큼 subList로 저장하기 위해서
            List<Post> batchList = postList.subList(i, endIndex);
            jpaPostRepository.saveAll(batchList);
            jpaPostRepository.flush();
        }

        int afterpostCount = jpaPostRepository.getPostCount();

        Assert.assertEquals("Post객체 100만개 생성", 1000000, afterpostCount - beforepostCount);
    }

    @Test
    @DisplayName("Member객체 100만개 생성")
    void bulkMembers() {
        List<Member> memberList = new ArrayList<>();
        Faker faker = new Faker(new Locale("en"));
        String input = "2022-01-01 11:22:33";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdDate = LocalDateTime.parse(input, formatter);

        for (int i = 0; i < 1000000; i++) {
            String name = faker.name().name();
            String email = faker.internet().emailAddress();
            String password1 = String.valueOf(i + 100);

            Member member = memberService.createMember(name, email, password1, LocalDateTime.now(), createdDate, LocalDateTime.now());
            memberList.add(member);
        }
        System.out.println("memberList = " + memberList.size());

        int batchSize = 1000; // 배치 단위 크기
        int listSize = memberList.size();
        ; // 전체 데이터 크기

        int beforepostCount = jpaMemberRepository.getMemberCount();

        for (int i = 0; i < listSize; i += batchSize) {
            int endIndex = i + batchSize; // 최소 index를 구한다 save를 할때 batch 단위만큼 subList로 저장하기 위해서
            List<Member> batchList = memberList.subList(i, endIndex);
            jpaMemberRepository.saveAll(batchList);
            jpaMemberRepository.flush();
        }

        int afterpostCount = jpaMemberRepository.getMemberCount();

        Assert.assertEquals("Member객체 100만개 생성", 1000000, afterpostCount - beforepostCount);
    }

    @Test
    @DisplayName("이름 email 체크")
    void name() {
        Faker faker = new Faker(new Locale("ko"));
        String name = faker.name().name();
        String email = faker.internet().emailAddress();
        System.out.println("email = " + email);
        System.out.println("name = " + name);
    }
    @Test
    @DisplayName("좋아요 객체 10만개 생성")
    void bulkLikes() {
        /**
         * for 루프를 돌며 한명의 회원이 1개의 게시글에 좋아요를 누른다
         * 즉 like 객체를 만든다
         * like 객체를 만든 후 like 리스트에 넣는다
         * 이때 배치를 사용한다
         * like.create() 에 대해 매번 save를 실행하지 않고 1000번에 대해 list에 저장 후 saveAll을 실행한다
         */
        List<Like> likeList = new ArrayList<>();

        Post post = jpaPostRepository.findById(6L)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Post가 없습니다"));


        for (int i = 1000400; i < 1002400; i++) {
            Long memberId = (long) (i + 1);
            Optional<Member> optionalMember = jpaMemberRepository.findById(memberId);

            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                Like like = Like.create(member, post);
                likeList.add(like);
            } else {
                // Member가 없는 경우에 대한 처리
                // 예를 들어, 다음 반복문을 계속 진행하거나, 다른 동작을 수행할 수 있습니다.
                continue; // 다음 반복문을 실행하도록 계속 진행
            }
        }
        System.out.println("likeList = " + likeList.size());

        int batchSize = 100; // 배치 단위 크기
        int listSize = likeList.size();
        // 전체 데이터 크기

        int beforepostCount = jpaLikeRepository.getLikeCount();

        for (int i = 0; i < listSize; i += batchSize) {
            int endIndex = i + batchSize; // 최소 index를 구한다 save를 할때 batch 단위만큼 subList로 저장하기 위해서
            List<Like> batchList = likeList.subList(i, endIndex);
            jpaLikeRepository.saveAll(batchList);
            jpaLikeRepository.flush();
        }

        int afterpostCount = jpaLikeRepository.getLikeCount();

        Assert.assertEquals("Like객체 10만개 생성", 100000, afterpostCount - beforepostCount);
    }

}
