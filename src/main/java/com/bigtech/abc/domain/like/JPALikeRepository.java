package com.bigtech.abc.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface JPALikeRepository extends JpaRepository<Like, Long> {

    @Query(value = "SELECT * FROM likes l WHERE l.member_id = :memberId AND l.post_id = :postId for update", nativeQuery = true)
    Optional<Like> findByMemberIdAndPostId(@Param("memberId") Long memberId, @Param("postId") Long postId);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);

    @Query("SELECT count(*) FROM Like")
    Integer getLikeCount();

    @Query(value = "select member_id from likes where post_id = :id", nativeQuery = true)
    List<Long> getMemberList(@Param("id") Long id);

}
