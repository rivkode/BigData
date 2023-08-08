package com.bigtech.abc.service.post;

import com.bigtech.abc.common.exception.AppException;
import com.bigtech.abc.common.exception.ErrorCode;
import com.bigtech.abc.domain.follow.JPAFollowRepository;
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

    private final JPAFollowRepository jpaFollowRepository;

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
        /**
         * 팔로워 목록 가져오는 로직
         */
        List<Long> followIdList = jpaFollowRepository.getFollowList(id);
        List<Post> postList = new ArrayList<>();
        // 가져온 팔로워 목록들 기준 팔로워들의 member_id 로
        // post.member_id 와 일치하는지 판단하여 post_id 를 조회하여 가져옴
        for (Long i : followIdList) { // i = 팔로워의 id
            List<Long> postIdList = jpaPostRepository.findPostIdsByMemberId(i); // member id 기준 어떤 post를 작성하였는지 본다
            for (Long j : postIdList) { // memberid = 1 이 작성한 post id 모음, 즉 post id 를 가져온 것
                Post post = jpaPostRepository.findById(j)
                        .orElseThrow(() -> new AppException(ErrorCode.INVALID_DATA, "Post가 없습니다"));
                postList.add(post);
            }
        }
        return postList;
    }

    /**
     * 현재상황
     * 현재 followIdList 들을 for문 돌며
     * 각 followingId 하나마다 post를 작성한 postIdList를 뽑고
     * postIdList 개수만큼 findById로 query를 계속 요청한다
     * <p>
     * 개선사항
     * - postIdList를 batch 를 사용하여 한번의 쿼리로 postId 조회
     * postIdList 개수만큼 돌렸던 쿼리를 -> 한 번의 쿼리로 모든 post를 가져온다
     * <p>
     * - follow 테이블과 post 테이블을 follow의 followId 와 post의 member_id 로 join
     * 새로 생성된 테이블에서 전달받은  followId에 대해 일치하는 followId의 post_id를 가져온다
     * 이때 in query를 사용하여 가져온다
     * join 쿼리는 아래와 같다
     * <p>
     * 위 내용 수정
     * in query 사용하기
     * select *
     * from post
     * where member_id in (select following_id from follow where follower_id = {id})
     * ;
     * <p>
     * 쿼리 결과
     * mysql> select *
     * -> from post
     * -> where member_id in (select following_id from follow where follower_id = 1103914);
     * +---------+-----------------------------------+----------------------------+----------------------------+-----------------------------------+-----------+
     * | post_id | content                           | created_date               | modified_date              | subject                           | member_id |
     * +---------+-----------------------------------+----------------------------+----------------------------+-----------------------------------+-----------+
     * |      28 | 사용자2가 작성한 글               | 2022-01-01 02:22:33.000000 | 2023-08-08 09:03:18.562851 | 사용자2가 작성한 글               |   1103915 |
     * |      29 | 사용자2가 작성한 글 22            | 2022-01-01 02:22:33.000000 | 2023-08-08 09:03:28.206345 | 사용자2가 작성한 글 22            |   1103915 |
     * |      30 | 사용자3이 작성한 글               | 2022-01-01 02:22:33.000000 | 2023-08-08 09:03:51.954556 | 사용자3이 작성한 글               |   1103916 |
     * |      31 | 사용자3이 작성한 글               | 2022-01-01 02:22:33.000000 | 2023-08-08 09:04:00.827796 | 사용자3이 작성한 글 22            |   1103916 |
     * |      32 | 사용자 4가 작성한 글              | 2022-01-01 02:22:33.000000 | 2023-08-08 09:04:23.435535 | 사용자 4가 작성한 글              |   1103917 |
     * |      33 | 사용자 5 가 작성한 글             | 2022-01-01 02:22:33.000000 | 2023-08-08 10:25:59.652770 | 사용자 5 가 작성한 글             |   1103918 |
     * |      34 | 사용자 5 가 작성한 글 555         | 2022-01-01 02:22:33.000000 | 2023-08-08 10:26:09.382383 | 사용자 5 가 작성한 글 555         |   1103918 |
     * +---------+-----------------------------------+----------------------------+----------------------------+-----------------------------------+-----------+
     */
    public List<Post> followingPost(Long memberId) {
        return jpaPostRepository.followPost(memberId);
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

//    public List<PostPageDto> paginationNoOffset(Long postId, String name, int pageSize) {
//        return postRepositoryImpl.paginationNoOffset(postId, name, pageSize);
//    }
}
