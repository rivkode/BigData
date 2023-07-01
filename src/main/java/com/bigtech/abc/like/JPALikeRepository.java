package com.bigtech.abc.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JPALikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    @Query("SELECT count(*) FROM Like")
    Integer getLikeCount();

    @Query(value = "select member_id from likes where post_id = :id", nativeQuery = true)
    List<Long> getMemberList(@Param("id") Long id);

}
