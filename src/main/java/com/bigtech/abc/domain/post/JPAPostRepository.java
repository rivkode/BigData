package com.bigtech.abc.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPAPostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query(
            value = "SELECT * FROM post ORDER BY modified_Date DESC LIMIT 100",
            nativeQuery = true)
    List<Post> findByModifiedDate();

    @Query(value = "SELECT post_id FROM post where member_id = :id", nativeQuery = true)
    List<Long> findPostIdsByMemberId(Long id);

    @Query("SELECT count(*) FROM Post")
    Integer getPostCount();

    @Query(
            value = "SELECT * FROM post ORDER BY modified_Date DESC LIMIT 10",
            nativeQuery = true)
    List<Post> QuickfindByModifiedDate(Integer id);

    @Query(
            value = "SELECT id FROM post ORDER BY id DESC LIMIT 1",
            nativeQuery = true)
    Integer LastPostid();

    @Override
    Page<Post> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM post WHERE post_id < :id ORDER BY post_id DESC LIMIT 10", nativeQuery = true)
    List<Post> NoOffsetPage(Long id);

    @Query(value = "SELECT * FROM post WHERE member_id in (SELECT following_id from follow WHERE follower_id =:id)", nativeQuery = true)
    List<Post> followPost(Long id);
}
